package com.carlkuesters.fifachampions.game;

import com.carlkuesters.fifachampions.game.math.Parabole;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.carlkuesters.fifachampions.game.cooldowns.UnownedBallPickupCooldown;
import lombok.Getter;
import lombok.Setter;

public class PlayerObject extends PhysicsObject {

    public PlayerObject(Team team, Player player) {
        this.team = team;
        this.player = player;
    }
    private Team team;
    private Player player;
    private Controller controller;
    private Vector2f currentDirection = new Vector2f();
    private Vector2f targetDirection = new Vector2f(0, 1);
    private Vector2f targetLocation = new Vector2f();
    private float targetSpeedFactor = 1;
    @Getter
    private boolean isTurning;
    private boolean canMove = true;
    private float remainingFreezeTime;
    private Float forcedSpeed;
    private boolean isSprinting;
    private boolean isPressuring;
    private boolean isStraddling;
    private float remainingFallingDuration;
    private boolean isGoalkeeperJumping;
    @Getter
    @Setter
    private PlayerAnimation animation;
    @Getter
    @Setter
    private boolean markedForSwitch;

    @Override
    public void update(float tpf) {
        if (remainingFreezeTime > 0) {
            remainingFreezeTime -= tpf;
            if (remainingFreezeTime <= 0) {
                remainingFreezeTime = 0;
            }
        }
        isTurning = false;
        if (canMove && (remainingFreezeTime == 0)) {
            // Effective target location and direction
            Vector2f effectiveTargetLocation = targetLocation;
            if (isPressuring) {
                Vector3f ballPosition = game.getBall().getPosition();
                Vector3f positionNearBall = ballPosition.add(position.subtract(ballPosition).normalizeLocal().multLocal(1.5f));
                effectiveTargetLocation = MathUtil.convertTo2D_XZ(positionNearBall);
            }
            Vector2f oldTargetDistance = null;
            Float oldTargetDistanceSquared = null;
            if (effectiveTargetLocation != null) {
                oldTargetDistance = effectiveTargetLocation.subtract(position.getX(), position.getZ());
                oldTargetDistanceSquared = oldTargetDistance.lengthSquared();
            }
            Vector2f effectiveTargetDirection = targetDirection;
            if ((oldTargetDistance != null) && (oldTargetDistance.lengthSquared() > 0)) {
                effectiveTargetDirection = oldTargetDistance.normalize();
            }

            if ((remainingFallingDuration == 0) && (!isGoalkeeperJumping)) {
                // Turning
                if (targetSpeedFactor > 0) {
                    Vector3f currentDirection3f = getDirection();
                    Vector2f currentDirection2f = new Vector2f(currentDirection3f.getX(), currentDirection3f.getZ());
                    float angle = MathUtil.getSmallestAngleBetween(currentDirection2f, effectiveTargetDirection);
                    isTurning = (FastMath.abs(angle) > MathUtil.EPSILON);
                    if (isTurning) {
                        currentDirection.set(currentDirection2f);
                        float turnSpeed = targetSpeedFactor * PlayerSkillUtil.getValue(19, 21, player.getFieldPlayerSkills().getAcceleration());
                        float turnAngle = Math.signum(angle) * turnSpeed * tpf;
                        currentDirection.rotateAroundOrigin(turnAngle, false);

                        float newAngle = MathUtil.getSmallestAngleBetween(currentDirection, effectiveTargetDirection);
                        isTurning = (Math.signum(newAngle) == Math.signum(angle));
                    }
                    if (!isTurning) {
                        currentDirection.set(effectiveTargetDirection);
                    }
                }
                updateRotationToCurrentDirection();

                // Moving (Not moving while turning also gets rid of the "endlessly circling around the target" problem)
                if ((!isTurning) && ((oldTargetDistanceSquared == null) || (oldTargetDistanceSquared > MathUtil.EPSILON_SQUARED))) {
                    float moveSpeed;
                    if (forcedSpeed != null) {
                        moveSpeed = forcedSpeed;
                    } else {
                        moveSpeed = targetSpeedFactor * PlayerSkillUtil.getValue(3, 5.5f, player.getFieldPlayerSkills().getMaximumSpeed());
                        if (isSprinting) {
                            moveSpeed *= 1.667f;
                        }
                    }
                    velocity.setX(currentDirection.getX());
                    velocity.setZ(currentDirection.getY());
                    velocity.normalizeLocal().multLocal(moveSpeed);
                } else {
                    velocity.set(0, 0, 0);
                }
            }
            super.update(tpf);
            if (remainingFallingDuration > 0) {
                slowDown(velocity, 7, tpf);
                remainingFallingDuration -= tpf;
                if (remainingFallingDuration <= 0) {
                    isStraddling = false;
                    remainingFallingDuration = 0;
                    animation = null;
                }
            } else if (isGoalkeeperJumping) {
                if (velocity.lengthSquared() <= 0) {
                    isGoalkeeperJumping = false;
                    setDirection(new Vector3f(game.getHalfTimeSideFactor() * team.getSide(), 0, 0));
                    animation = null;
                }
            } else {
                // Turning the player can temporarily move him further away from his target position than before,
                // causing him to teleport with the current if-further-away-than-before-target-is-reached logic
                if ((oldTargetDistanceSquared != null) && (!isTurning)) {
                    float newTargetDistanceSquared = effectiveTargetLocation.subtract(position.getX(), position.getZ()).lengthSquared();
                    if (newTargetDistanceSquared > oldTargetDistanceSquared) {
                        position.set(effectiveTargetLocation.getX(), 0, effectiveTargetLocation.getY());
                    }
                }
            }
        }

        setDefaultAnimationIfNeeded();
        animation.update(tpf);
        if (animation.isFinished()) {
            animation = null;
            setDefaultAnimationIfNeeded();
        }
    }

    @Override
    protected boolean isAffectedByFrictionXZ() {
        return isGoalkeeperJumping;
    }

    public void passDirect(float strength) {
        pass(strength, 0);
    }

    public void passInRun(float strength) {
        pass(strength, strength * 2);
    }

    private void pass(float strength, float inRunFactor) {
        game.continueFromSituation();
        Vector3f passDirection = getPreparedPassDirection(inRunFactor);
        float effectiveStrength = (0.45f + (0.5f * strength));
        Vector3f ballVelocity = passDirection.multLocal(effectiveStrength * 25);
        accelerateBall(ballVelocity, true);
        if (game.getBall().getPosition().getY() > 0.5f) {
            animation = new PlayerAnimation("header_end", 0.5f);
        } else {
            animation = new PlayerAnimation("short_pass_end", 1);
        }
    }

    public void throwInDirect(float strength) {
        throwIn(strength, 0);
    }

    public void throwInInRun(float strength) {
        throwIn(strength, strength * 2);
    }

    private void throwIn(float strength, float inRunFactor) {
        game.continueFromSituation();
        Vector3f passDirection = getPreparedPassDirection(inRunFactor);
        float effectiveStrength = (13 + (4 * strength));
        float effectiveSlope = (2 + (3 * strength));
        Vector3f ballVelocity = passDirection.mult(effectiveStrength).add(0, effectiveSlope, 0);
        accelerateBall(ballVelocity, false);
    }

    public void shoot(float strength) {
        float effectiveStrength = (18 + (strength * PlayerSkillUtil.getValue(0, 11, player.getFieldPlayerSkills().getShootingStrength())));
        float effectiveSlope = (2 + (11 * strength));
        Vector3f ballVelocity = getDirection().mult(effectiveStrength).addLocal(0, effectiveSlope, 0);
        shoot(ballVelocity);
    }

    public void shoot(Vector3f ballVelocity) {
        game.continueFromSituation();
        accelerateBall(ballVelocity, true);
    }

    public void header(float strength) {
        float effectiveStrength = (14 + (strength * PlayerSkillUtil.getValue(0, 9, player.getFieldPlayerSkills().getShootingStrength())));
        Vector3f ballVelocity = getDirection().mult(effectiveStrength);
        accelerateBall(ballVelocity, true);
    }

    public void flank(float strength) {
        game.continueFromSituation();
        Vector3f passDirection = getPreparedPassDirection(0);
        float effectiveStrength = (12 + (strength * PlayerSkillUtil.getValue(0, 12, player.getFieldPlayerSkills().getShootingStrength())));
        float effectiveSlope = (5 + (6 * strength));
        Vector3f ballVelocity = passDirection.mult(effectiveStrength).add(0, effectiveSlope, 0);
        accelerateBall(ballVelocity, true);
    }

    private Vector3f getPreparedPassDirection(float inRunFactor) {
        AnticipatedPlayer anticipatedPlayer = game.getTargetedPlayer(team, this, inRunFactor);
        Vector3f passDirection;
        if (anticipatedPlayer != null) {
            PlayerObject targetedPlayer = anticipatedPlayer.getPlayerObject();
            passDirection = targetedPlayer.getPosition().subtract(position).normalizeLocal();
            if (targetedPlayer.getController() == null) {
                controller.setPlayer(targetedPlayer);
            }
        } else {
            passDirection = getDirection().clone();
        }
        return passDirection;
    }

    public void turnIntoControllerTargetDirection() {
        setTargetDirection(controller.getTargetDirection().normalize());
        currentDirection.set(targetDirection);
        updateRotationToCurrentDirection();
    }

    private void updateRotationToCurrentDirection() {
        Vector3f newDirection = getDirection().clone();
        newDirection.setX(currentDirection.getX());
        newDirection.setZ(currentDirection.getY());
        newDirection.normalizeLocal();
        setDirection(newDirection);
    }

    private void accelerateBall(Vector3f ballVelocity, boolean canTriggerOffside) {
        game.getBall().accelerate(ballVelocity, canTriggerOffside);
        game.getCooldownManager().putOnCooldown(new UnownedBallPickupCooldown(this));
    }

    public void straddle() {
        if (remainingFallingDuration == 0) {
            isStraddling = true;
            float straddleVelocity = Math.max(3, velocity.length());
            velocity.set(getDirection().mult(straddleVelocity));
            this.remainingFallingDuration = (1 + (straddleVelocity / 9));
            animation = new PlayerAnimation("tackle", (1.1f * remainingFallingDuration));
        }
    }

    public void collapse() {
        velocity.multLocal(0.5f);
        this.remainingFallingDuration = 1;
        animation = new PlayerAnimation("collapse", (1.1f * remainingFallingDuration));
    }

    public GoalkeeperJump getGoalkeeperJump(Vector3f targetPosition, float jumpDuration) {
        Parabole paraboleY = getParabole_SlowFactor_ByDuration(position.getY(), targetPosition.getY(), jumpDuration, gravitation);
        float initialVelocityY = paraboleY.getFirstDerivative(0);
        Parabole paraboleZ = getParabole_PerfectStop(position.getZ(), targetPosition.getZ(), jumpDuration);
        float initialVelocityZ = paraboleZ.getFirstDerivative(0);
        float frictionXZ_Air = FastMath.abs(paraboleZ.getSecondDerivative());
        Vector3f initialVelocity = new Vector3f(0, initialVelocityY, initialVelocityZ);

        Vector3f distanceToTarget_XYZ = targetPosition.subtract(position);
        Vector2f directionToTarget_ZY = new Vector2f(distanceToTarget_XYZ.getZ(), distanceToTarget_XYZ.getY()).normalizeLocal();
        float jumpAngle = -1 * game.getHalfTimeSideFactor() * directionToTarget_ZY.angleBetween(MathUtil.UNIT_2D_Y);
        Quaternion rotation = new Quaternion();
        rotation.lookAt(new Vector3f(game.getHalfTimeSideFactor() * team.getSide(), 0, 0), Vector3f.UNIT_Y);
        rotation.multLocal(new Quaternion().fromAngleAxis(jumpAngle, Vector3f.UNIT_Z));

        return new GoalkeeperJump(initialVelocity, rotation, frictionXZ_Air, jumpDuration);
    }

    public void executeGoalkeeperJump(GoalkeeperJump goalkeeperJump) {
        isGoalkeeperJumping = true;
        velocity.set(goalkeeperJump.getInitialVelocity());
        rotation.set(goalkeeperJump.getRotation());
        frictionXZ_Air = goalkeeperJump.getFrictionXZ_Air();
        animation = new PlayerAnimation("goalkeeper_jump", goalkeeperJump.getJumpDuration());
    }

    public boolean onBallPickUp() {
        if (controller != null) {
            return controller.getButtons().triggerCurrentOrRecentBallCharge();
        }
        return false;
    }

    public boolean isOwningBall() {
        return (game.getBall().getOwner() == this);
    }

    public Team getTeam() {
        return team;
    }

    public Player getPlayer() {
        return player;
    }

    public void setController(Controller controller) {
        this.controller = controller;
        targetLocation = null;
        targetSpeedFactor = 1;
        if (controller != null) {
            isSprinting = controller.isSprinting();
        } else {
            isSprinting = false;
            isPressuring = false;
        }
    }

    public Controller getController() {
        return controller;
    }

    public boolean isGoalkeeper() {
        return (this == team.getGoalkeeper());
    }

    public void setTargetLocation(Vector2f targetLocation) {
        if (targetLocation == null) {
            this.targetLocation = null;
        } else {
            if (this.targetLocation == null) {
                this.targetLocation = new Vector2f();
            }
            this.targetLocation.set(targetLocation);
        }
        Vector3f directionToBall = game.getBall().getPosition().subtract(position).normalizeLocal();
        setTargetDirection(MathUtil.convertTo2D_XZ(directionToBall));
        targetSpeedFactor = 1;
    }

    public void setTargetWalkDirection(Vector2f targetWalkDirection) {
        targetLocation = null;
        setTargetDirection(targetWalkDirection.normalize());
        targetSpeedFactor = targetWalkDirection.length();
    }

    private void setTargetDirection(Vector2f targetDirection) {
        if (targetDirection.lengthSquared() > 0) {
            this.targetDirection.set(targetDirection);
        }
    }

    public void setForcedSpeed(Float forcedSpeed) {
        this.forcedSpeed = forcedSpeed;
    }

    public void setIsSprinting(boolean isSprinting) {
        this.isSprinting = isSprinting;
    }

    public void setIsPressuring(boolean isPressuring) {
        this.isPressuring = isPressuring;
    }

    public boolean isStraddling() {
        return isStraddling;
    }

    public boolean isFalling() {
        return (remainingFallingDuration > 0);
    }

    public boolean isGoalkeeperJumping() {
        return isGoalkeeperJumping;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
        if (!canMove) {
            velocity.set(0, 0, 0);
        }
    }

    public void freeze(float freezeTime) {
        this.remainingFreezeTime = freezeTime;
    }

    private void setDefaultAnimationIfNeeded() {
        // The default animations are the only looped ones at the moment
        if ((animation == null) || animation.isLoop()) {
            PlayerAnimation defaultAnimation = getDefaultAnimation();
            if ((animation == null) || (!defaultAnimation.getName().equals(animation.getName()))) {
                animation = defaultAnimation;
            }
        }
    }

    private PlayerAnimation getDefaultAnimation() {
        float velocityLength = velocity.length();
        if (velocityLength > 8) {
            return PlayerAnimations.createRunFast();
        } else if ((velocityLength > 3) || isTurning) {
            return PlayerAnimations.createRunMedium();
        } else if (velocityLength > MathUtil.EPSILON) {
            return PlayerAnimations.createRunSlow();
        }
        return PlayerAnimations.createIdle();
    }
}
