package com.carlkuesters.fifachampions.game;

import com.jme3.animation.LoopMode;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.carlkuesters.fifachampions.game.cooldowns.UnownedBallPickupCooldown;

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
    private boolean isSprinting;
    private boolean isPressuring;
    private boolean isStraddling;
    private float remainingFallingDuration;
    private boolean isGoalkeeperJumping;
    private float goalkeeperJumpSlowFactorZ;
    private PlayerAnimation animation;

    @Override
    public void update(float tpf) {
        if (remainingFreezeTime > 0) {
            remainingFreezeTime -= tpf;
            if (remainingFreezeTime <= 0) {
                remainingFreezeTime = 0;
            }
        }
        if (canMove && (remainingFreezeTime == 0)) {
            float oldTargetDistanceSquared = Float.MAX_VALUE;
            if ((remainingFallingDuration == 0) && (!isGoalkeeperJumping)) {
                float speed = (isSprinting ? 10 : 6);
                boolean wantsToMove = true;
                if (isPressuring) {
                    targetLocation = MathUtil.convertTo2D_XZ(game.getBall().getPosition());
                }
                if (targetLocation != null) {
                    Vector2f targetDistance = targetLocation.subtract(position.getX(), position.getZ());
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
                slowDown(velocity, goalkeeperJumpSlowFactorZ, 2, tpf);
                if (velocity.lengthSquared() <= 0) {
                    isGoalkeeperJumping = false;
                    setAnimation(null);
                }
            } else {
                if (targetLocation != null) {
                    float newTargetDistanceSquared = targetLocation.subtract(position.getX(), position.getZ()).lengthSquared();
                    if ((newTargetDistanceSquared - oldTargetDistanceSquared) > -1 * MathUtil.EPSILON) {
                        position.set(targetLocation.getX(), 0, targetLocation.getY());
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
        game.continueFromSituation();
        Vector3f passDirection = getPreparedPassDirection(inRunFactor);
        float effectiveStrength = (13 + (4 * strength));
        float effectiveSlope = (2 + (3 * strength));
        Vector3f ballVelocity = passDirection.mult(effectiveStrength).add(0, effectiveSlope, 0);
        accelerateBall(ballVelocity, false);
    }

    public void shoot(float strength) {
        game.continueFromSituation();
        turnIntoControllerTargetDirection();
        float effectiveStrength = (18 + (11 * strength));
        float effectiveSlope = (2 + (11 * strength));
        Vector3f ballVelocity = direction.mult(effectiveStrength).addLocal(0, effectiveSlope, 0);
        accelerateBall(ballVelocity, true);
        setAnimation(new PlayerAnimation("run_kick_end", 1));
    }

    public void header(float strength) {
        turnIntoControllerTargetDirection();
        float effectiveStrength = (14 + (9 * strength));
        Vector3f ballVelocity = direction.mult(effectiveStrength);
        accelerateBall(ballVelocity, true);
        setAnimation(new PlayerAnimation("header_end", 0.5f));
    }

    public void flank(float strength) {
        game.continueFromSituation();
        Vector3f passDirection = getPreparedPassDirection(0);
        float effectiveStrength = (12 + (12 * strength));
        float effectiveSlope = (5 + (6 * strength));
        Vector3f ballVelocity = passDirection.mult(effectiveStrength).add(0, effectiveSlope, 0);
        accelerateBall(ballVelocity, true);
    }

    private Vector3f getPreparedPassDirection(float inRunFactor) {
        turnIntoControllerTargetDirection();
        AnticipatedPlayer anticipatedPlayer = game.getTargetedPlayer(team, this, inRunFactor);
        Vector3f passDirection;
        if (anticipatedPlayer != null) {
            PlayerObject targetedPlayer = anticipatedPlayer.getPlayerObject();
            passDirection = targetedPlayer.getPosition().subtract(position).normalizeLocal();
            if (targetedPlayer.getController() == null) {
                controller.setPlayer(targetedPlayer);
            }
        } else {
            passDirection = direction.clone();
        }
        return passDirection;
    }

    private void turnIntoControllerTargetDirection() {
        targetWalkDirection.set(controller.getTargetDirection());
        turnIntoWalkDirection();
    }

    private void turnIntoWalkDirection() {
        if (targetWalkDirection.lengthSquared() > MathUtil.EPSILON_SQUARED) {
            direction.setX(targetWalkDirection.getX());
            direction.setZ(targetWalkDirection.getY());
            direction.normalizeLocal();
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
            velocity.set(direction.mult(straddleVelocity));
            this.remainingFallingDuration = (1 + (straddleVelocity / 9));
            setAnimation(new PlayerAnimation("tackle", (1.1f * remainingFallingDuration), LoopMode.DontLoop));
        }
    }

    public void collapse() {
        velocity.multLocal(0.5f);
        this.remainingFallingDuration = 1;
        setAnimation(new PlayerAnimation("collapse", (1.1f * remainingFallingDuration), LoopMode.DontLoop));
    }

    public void goalkeeperJump(Vector3f targetPosition, float jumpDuration) {
        isGoalkeeperJumping = true;

        // y = ax²+bx+c
        // y' = 2ax+b
        // y'' = 2a = -gravity
        // ----------
        // y1=a(x1)²+bx1+c <=> c=y1-a(x1)²-bx1
        // y2=a(x2)²+bx2+c <=> c=y2-a(x2)²-bx2
        // ----------
        // => y1-a(x1)²-bx1 = y2-a(x2)²-bx2
        // <=> y1-a(x1)²-y2+a(x2)² = b(x1-x2)
        // <=> b={y1-a(x1)²-y2+a(x2)²}/(x1-x2)
        // with x1=0
        // <=> b={y1-y2+a(x2)²}/-x2
        // ----------
        // => initialVelocity.y = y'(x1) = 2ax1+b
        // with x1=0
        // => initialVelocity.y = b
        // ----------
        float y1 = position.getY();
        float x2 = jumpDuration;
        float y2 = targetPosition.getY();

        float a = ((-1 * gravitation) / 2);
        float b = (y1 - y2 + (a * x2 * x2)) / (-1 * x2);

        float initialVelocityY = b;

        // y = Ax²+Bx+C
        // y' = 2Ax+B
        // y'' = 2A = slowFactor
        // ----------
        // y(0) = 0
        // A*0 + B*0 + C = 0
        // C = 0
        // ----------
        // y'(jumpDuration) = 0
        // 2A*jumpDuration + B = 0
        // B = -2A*jumpDuration
        // ----------
        // y(jumpDuration) = absoluteDistanceZ
        // A*(jumpDuration^2) + B*jumpDuration + C = absoluteDistanceZ
        // A*(jumpDuration^2) + B*jumpDuration = absoluteDistanceZ
        // A*(jumpDuration^2) + (-2A*jumpDuration)*jumpDuration = absoluteDistanceZ
        // A*(jumpDuration^2) - 2A*(jumpDuration^2) = absoluteDistanceZ
        // -A*(jumpDuration^2) = absoluteDistanceZ
        // A = -absoluteDistanceZ / jumpDuration^2
        // ----------
        // => initialVelocity.z = y'(0) = 2A*0 + B = B
        float distanceZ = (targetPosition.getZ() - position.getZ());
        float absoluteDistanceZ = FastMath.abs(distanceZ);
        float A = ((-1 * absoluteDistanceZ) / (jumpDuration * jumpDuration));
        float B = (-2 * A * jumpDuration);

        float initialVelocityZ = Math.signum(distanceZ) * B;
        goalkeeperJumpSlowFactorZ = FastMath.abs(2 * A);

        Vector3f initialVelocity = new Vector3f(
            0,
            initialVelocityY,
            initialVelocityZ
        );

        velocity.set(initialVelocity);
        setAnimation(new PlayerAnimation("goalkeeper_jump", 1, LoopMode.DontLoop));
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

    public void setTargetLocation(Vector2f targetLocation) {
        if (this.targetLocation == null) {
            this.targetLocation = new Vector2f();
        }
        this.targetLocation.set(targetLocation);
    }

    public void setTargetWalkDirection(Vector2f targetWalkDirection) {
        this.targetWalkDirection.set(targetWalkDirection);
    }

    public Vector2f getTargetWalkDirection() {
        return targetWalkDirection;
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
