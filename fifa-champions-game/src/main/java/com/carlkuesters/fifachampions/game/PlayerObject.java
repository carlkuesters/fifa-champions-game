package com.carlkuesters.fifachampions.game;

import com.carlkuesters.fifachampions.game.math.Parabole;
import com.jme3.animation.LoopMode;
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
    private static final float MINIMUM_ROTATED_WALK_DIRECTION = 0.001f;
    private Team team;
    private Player player;
    private Controller controller;
    private Vector2f targetWalkDirection = new Vector2f();
    private Vector2f targetLocation = new Vector2f();
    private Vector3f lastWalkTurnedPosition = new Vector3f();
    private boolean canMove = true;
    private float remainingFreezeTime;
    private Float forcedSpeed;
    private boolean isSprinting;
    private boolean isPressuring;
    private boolean isStraddling;
    private float remainingFallingDuration;
    private boolean isGoalkeeperJumping;
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
        if (canMove && (remainingFreezeTime == 0)) {
            Vector2f effectiveTargetLocation = targetLocation;
            float oldTargetDistanceSquared = Float.MAX_VALUE;
            if ((remainingFallingDuration == 0) && (!isGoalkeeperJumping)) {
                float speed;
                if (forcedSpeed != null) {
                    speed = forcedSpeed;
                } else {
                    speed = PlayerSkillUtil.getValue(3, 6, player.getFieldPlayerSkills().getMaximumSpeed());
                    if (isSprinting) {
                        speed *= 1.667f;
                    }
                }
                boolean wantsToMove = true;
                if (isPressuring) {
                    Vector3f ballPosition = game.getBall().getPosition();
                    Vector3f positionNearBall = ballPosition.add(position.subtract(ballPosition).normalizeLocal().multLocal(1.5f));
                    effectiveTargetLocation = MathUtil.convertTo2D_XZ(positionNearBall);
                }
                if (effectiveTargetLocation != null) {
                    Vector2f targetDistance = effectiveTargetLocation.subtract(position.getX(), position.getZ());
                    oldTargetDistanceSquared = targetDistance.lengthSquared();
                    targetWalkDirection.set(targetDistance).normalizeLocal();
                    wantsToMove = (oldTargetDistanceSquared > MathUtil.EPSILON_SQUARED);
                }
                if (wantsToMove) {
                    velocity.setX(targetWalkDirection.getX());
                    velocity.setZ(targetWalkDirection.getY());
                    velocity.normalizeLocal().multLocal(targetWalkDirection.length() * speed);
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
                    setAnimation(null);
                }
            } else if (isGoalkeeperJumping) {
                if (velocity.lengthSquared() <= 0) {
                    isGoalkeeperJumping = false;
                    setDirection(new Vector3f(game.getHalfTimeSideFactor() * team.getSide(), 0, 0));
                    setAnimation(null);
                }
            } else {
                if (effectiveTargetLocation != null) {
                    float newTargetDistanceSquared = effectiveTargetLocation.subtract(position.getX(), position.getZ()).lengthSquared();
                    if ((newTargetDistanceSquared - oldTargetDistanceSquared) > -1 * MathUtil.EPSILON) {
                        position.set(effectiveTargetLocation.getX(), 0, effectiveTargetLocation.getY());
                    }
                }
                float distanceToLastTurnedPosition = this.position.distanceSquared(lastWalkTurnedPosition);
                if (distanceToLastTurnedPosition >= MINIMUM_ROTATED_WALK_DIRECTION) {
                    turnIntoWalkDirection();
                    lastWalkTurnedPosition.set(position);
                }
            }
        }
        if (animation != null) {
            animation.update(tpf);
            if (animation.isFinished()) {
                animation = null;
            }
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
        game.continueFromBallSituation();
        Vector3f passDirection = getPreparedPassDirection(inRunFactor);
        float effectiveStrength = (0.45f + (0.5f * strength));
        Vector3f ballVelocity = passDirection.multLocal(effectiveStrength * 25);
        accelerateBall(ballVelocity, true);
        if (game.getBall().getPosition().getY() > 0.5f) {
            setAnimation(new PlayerAnimation("header_end", 0.5f));
        } else {
            setAnimation(new PlayerAnimation("short_pass_end", 1));
        }
    }

    public void throwInDirect(float strength) {
        throwIn(strength, 0);
    }

    public void throwInInRun(float strength) {
        throwIn(strength, strength * 2);
    }

    private void throwIn(float strength, float inRunFactor) {
        game.continueFromBallSituation();
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
        game.continueFromBallSituation();
        accelerateBall(ballVelocity, true);
    }

    public void header(float strength) {
        float effectiveStrength = (14 + (strength * PlayerSkillUtil.getValue(0, 9, player.getFieldPlayerSkills().getShootingStrength())));
        Vector3f ballVelocity = getDirection().mult(effectiveStrength);
        accelerateBall(ballVelocity, true);
    }

    public void flank(float strength) {
        game.continueFromBallSituation();
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
        targetWalkDirection.set(controller.getTargetDirection());
        turnIntoWalkDirection();
    }

    private void turnIntoWalkDirection() {
        if (targetWalkDirection.lengthSquared() > MathUtil.EPSILON_SQUARED) {
            Vector3f newDirection = getDirection().clone();
            newDirection.setX(targetWalkDirection.getX());
            newDirection.setZ(targetWalkDirection.getY());
            newDirection.normalizeLocal();
            setDirection(newDirection);
        }
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
            setAnimation(new PlayerAnimation("tackle", (1.1f * remainingFallingDuration), LoopMode.DontLoop));
        }
    }

    public void collapse() {
        velocity.multLocal(0.5f);
        this.remainingFallingDuration = 1;
        setAnimation(new PlayerAnimation("collapse", (1.1f * remainingFallingDuration), LoopMode.DontLoop));
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
        setAnimation(new PlayerAnimation("goalkeeper_jump", goalkeeperJump.getJumpDuration(), LoopMode.DontLoop));
    }

    public boolean onBallPickUp() {
        if (controller != null) {
            return controller.triggerCurrentOrRecentBallCharge();
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
        targetWalkDirection.set(0, 0);
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
    }

    public void setTargetWalkDirection(Vector2f targetWalkDirection) {
        this.targetWalkDirection.set(targetWalkDirection);
    }

    public Vector2f getTargetWalkDirection() {
        return targetWalkDirection;
    }

    public void setForcedSpeed(Float forcedSpeed) {
        this.forcedSpeed = forcedSpeed;
    }

    public void setIsSprinting(boolean isSprinting) {
        this.isSprinting = isSprinting;
    }

    public boolean isSprinting() {
        return isSprinting;
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

    public void setAnimation(PlayerAnimation animation) {
        this.animation = animation;
    }

    public PlayerAnimation getAnimation() {
        return animation;
    }
}
