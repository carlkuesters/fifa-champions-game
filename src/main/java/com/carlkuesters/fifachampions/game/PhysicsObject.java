package com.carlkuesters.fifachampions.game;

import com.carlkuesters.fifachampions.game.math.Parabole;
import com.carlkuesters.fifachampions.game.math.ParaboleUtil;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;

import java.util.List;
import java.util.function.Predicate;

public class PhysicsObject extends GameObject {

    private static final float PRECOMPUTE_TIME_PER_FRAME = (1f / 60);
    protected float gravitation = 9.81f;
    protected float frictionXZ_Ground = 4;
    protected float frictionXZ_Air = 3;
    protected float bouncinessGround = 0;
    protected float bouncinessWalls = 0;
    private Vector3f tmpPrecomputedPosition = new Vector3f();
    private Quaternion tmpPrecomputedRotation = new Quaternion();
    private Vector3f tmpPrecomputedVelocity = new Vector3f();
    private Vector3f tmpNewPosition = new Vector3f();
    protected Vector3f position = new Vector3f();
    protected Quaternion rotation = new Quaternion();
    protected Vector3f velocity = new Vector3f();

    @Override
    public void update(float tpf) {
        super.update(tpf);
        updateTransform(position, rotation, velocity, tpf);
    }

    public PhysicsPrecomputationResult precomputeTransformUntil(Predicate<PhysicsPrecomputationResult> endCondition) {
        tmpPrecomputedPosition.set(position);
        tmpPrecomputedRotation.set(rotation);
        tmpPrecomputedVelocity.set(velocity);
        PhysicsPrecomputationResult precomputationResult = new PhysicsPrecomputationResult(tmpPrecomputedPosition, tmpPrecomputedRotation, tmpPrecomputedVelocity);
        float passedTime = 0;
        while (!endCondition.test(precomputationResult)) {
            passedTime += PRECOMPUTE_TIME_PER_FRAME;
            if (passedTime > 5) {
                return null;
            }
            updateTransform(tmpPrecomputedPosition, tmpPrecomputedRotation, tmpPrecomputedVelocity, PRECOMPUTE_TIME_PER_FRAME);
            precomputationResult.setPosition(tmpPrecomputedPosition);
            precomputationResult.setRotation(tmpPrecomputedRotation);
            precomputationResult.setVelocity(tmpPrecomputedVelocity);
            precomputationResult.setPassedTime(passedTime);
        }
        return precomputationResult;
    }

    protected void updateTransform(Vector3f position, Quaternion rotation, Vector3f velocity, float tpf) {
        tmpNewPosition.set(position);
        tmpNewPosition.addLocal(velocity.mult(tpf));
        tryMoveToPosition(position, velocity, tmpNewPosition);
        if (position.getY() > 0) {
            velocity.setY(velocity.getY() - (gravitation * tpf));
        } else if (position.getY() < 0) {
            position.setY(0);
            velocity.setY(-1 * bouncinessGround * velocity.getY());
        }
        if (isAffectedByFrictionXZ()) {
            float frictionXZ = ((position.getY() > 0) ? frictionXZ_Air : frictionXZ_Ground);
            float velocityToSubtractX = (Math.signum(velocity.getX()) * frictionXZ * tpf);
            float velocityToSubtractZ = (Math.signum(velocity.getZ()) * frictionXZ * tpf);
            if (FastMath.abs(velocityToSubtractX) < FastMath.abs(velocity.getX())) {
                velocity.setX(velocity.getX() - velocityToSubtractX);
            } else {
                velocity.setX(0);
            }
            if (FastMath.abs(velocityToSubtractZ) < FastMath.abs(velocity.getZ())) {
                velocity.setZ(velocity.getZ() - velocityToSubtractZ);
            } else {
                velocity.setZ(0);
            }
        }
    }

    protected boolean isAffectedByFrictionXZ() {
        return true;
    }

    public void tryMoveToPosition(Vector3f position, Vector3f velocity, Vector3f targetPosition) {
        PhysicsDirection collidingWallDirection = getCollidingWallDirectionWhenMoving(position, targetPosition);
        if (collidingWallDirection == null) {
            position.set(targetPosition);
        } else {
            switch (collidingWallDirection) {
                case X:
                    velocity.setX(-1 * bouncinessWalls * velocity.getX());
                    break;
                case Y:
                    velocity.setY(-1 * bouncinessWalls * velocity.getY());
                    break;
                case Z:
                    velocity.setZ(-1 * bouncinessWalls * velocity.getZ());
                    break;
            }
        }
    }

    private static PhysicsDirection getCollidingWallDirectionWhenMoving(Vector3f oldPosition, Vector3f newPosition) {
        // Walls around the field
        if ((newPosition.getX() < -57.35f) || (newPosition.getX() > 58.15f)) {
            return PhysicsDirection.X;
        } else if (FastMath.abs(newPosition.getZ()) > 38) {
            return PhysicsDirection.Z;
        }
        // Goal nets
        boolean isInGoal = Game.isInsideGoal(oldPosition);
        boolean willBeInGoal = Game.isInsideGoal(newPosition);
        if ((isInGoal != willBeInGoal) && (Game.getOutside(oldPosition) != null)) {
            Vector3f outsideGoalPosition = (isInGoal ? newPosition : oldPosition);
            if (FastMath.abs(outsideGoalPosition.getX()) > (Game.FIELD_HALF_WIDTH + Game.GOAL_WIDTH)) {
                return PhysicsDirection.X;
            } else if (outsideGoalPosition.getY() > Game.GOAL_HEIGHT) {
                return PhysicsDirection.Y;
            } else if ((outsideGoalPosition.getZ() < Game.GOAL_Z_BOTTOM) || (outsideGoalPosition.getZ() > Game.GOAL_Z_TOP)) {
                return PhysicsDirection.Z;
            }
        }
        return null;
    }

    protected void slowDown(Vector3f velocity, float strength, float tpf) {
        Vector3f amountToSubtract = velocity.normalize().multLocal(strength * tpf);
        if (amountToSubtract.lengthSquared() < velocity.lengthSquared()) {
            velocity.subtractLocal(amountToSubtract);
        } else {
            velocity.set(0, 0, 0);
        }
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public void lookAt_XZ(Vector3f position) {
        TempVars tempVars = TempVars.get();
        setDirection(tempVars.vect1.set(position.getX(), 0, position.getZ()).subtractLocal(this.position.getX(), 0, this.position.getZ()).normalizeLocal());
        tempVars.release();
    }

    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity.set(velocity);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public Vector3f getDirection() {
        TempVars tempVars = TempVars.get();
        Vector3f direction = rotation.multLocal(tempVars.vect1.set(Vector3f.UNIT_Z));
        tempVars.release();
        return direction;
    }

    public void setDirection(Vector3f direction) {
        rotation.lookAt(direction, Vector3f.UNIT_Y);
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public Vector3f getInitialVelocity_ByTargetVelocityX(Vector3f targetPosition, float targetVelocityX) {
        float paraboleAirFrictionFactorX = Math.signum(targetPosition.getX() - position.getX());
        Parabole paraboleX = getParabole_SlowFactor_ByTargetVelocity(position.getX(), targetPosition.getX(), targetVelocityX, paraboleAirFrictionFactorX * frictionXZ_Air);
        float initialVelocityX = paraboleX.getFirstDerivative(0);
        float duration = paraboleX.getCalculatedValue("x2");
        Parabole paraboleY = getParabole_SlowFactor_ByDuration(position.getY(), targetPosition.getY(), duration, gravitation);
        float initialVelocityY = paraboleY.getFirstDerivative(0);
        float paraboleAirFrictionFactorZ = Math.signum(targetPosition.getZ() - position.getZ());
        Parabole paraboleZ = getParabole_SlowFactor_ByDuration(position.getZ(), targetPosition.getZ(), duration, paraboleAirFrictionFactorZ * frictionXZ_Air);
        float initialVelocityZ = paraboleZ.getFirstDerivative(0);

        return new Vector3f(initialVelocityX, initialVelocityY, initialVelocityZ);
    }

    public static Parabole getParabole_SlowFactor_ByDuration(float currentValue, float targetValue, float duration, float slowFactor) {
        float x1 = 0;
        float y1 = currentValue;
        float x2 = duration;
        float y2 = targetValue;
        float a = (slowFactor / -2);

        return ParaboleUtil.getParabole_X1_Y1_X2_Y2_A(x1, y1, x2, y2, a);
    }

    public static Parabole getParabole_SlowFactor_ByTargetVelocity(float currentValue, float targetValue, float targetVelocity, float slowFactor) {
        float x1 = 0;
        float y1 = currentValue;
        float y2 = targetValue;
        float firstDerivativeX2 = targetVelocity;
        float a = (slowFactor / -2);

        List<Parabole> paraboles = ParaboleUtil.getParaboles_X1_Y1_Y2_FDX2_A(x1, y1, y2, firstDerivativeX2, a);
        return ParaboleUtil.getQuadraticFormulaResult(paraboles, true);
    }

    public static Parabole getParabole_PerfectStop(float currentValue, float targetValue, float flyDuration) {
        float x1 = 0;
        float y1 = currentValue;
        float x2 = flyDuration;
        float y2 = targetValue;
        float firstDerivativeX2 = 0;

        return ParaboleUtil.getParabole_X1_Y1_X2_Y2_FDX2(x1, y1, x2, y2, firstDerivativeX2);
    }
}
