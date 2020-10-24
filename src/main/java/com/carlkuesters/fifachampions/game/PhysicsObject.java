package com.carlkuesters.fifachampions.game;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

import java.util.function.Predicate;

public class PhysicsObject extends GameObject {

    private static final float PRECOMPUTE_TIME_PER_FRAME = (1f / 60);
    protected final float gravitation = 9.81f;
    protected float bouncinessGround = 0;
    protected float bouncinessWalls = 0;
    private Vector3f tmpPrecomputedPosition = new Vector3f();
    private Vector3f tmpPrecomputedDirection = new Vector3f();
    private Vector3f tmpPrecomputedVelocity = new Vector3f();
    private Vector3f tmpNewPosition = new Vector3f();
    protected Vector3f position = new Vector3f();
    protected Vector3f direction = new Vector3f(0, 0, 1);
    protected Vector3f velocity = new Vector3f();

    @Override
    public void update(float tpf) {
        super.update(tpf);
        updateTransform(position, direction, velocity, tpf);
    }

    public PhysicsPrecomputationResult precomputePositionUntil(Predicate<PhysicsPrecomputationResult> endCondition) {
        tmpPrecomputedPosition.set(position);
        tmpPrecomputedDirection.set(direction);
        tmpPrecomputedVelocity.set(velocity);
        PhysicsPrecomputationResult precomputationResult = new PhysicsPrecomputationResult(tmpPrecomputedPosition, tmpPrecomputedDirection, tmpPrecomputedVelocity);
        float passedTime = 0;
        while (passedTime < 5) {
            passedTime += PRECOMPUTE_TIME_PER_FRAME;
            updateTransform(tmpPrecomputedPosition, tmpPrecomputedDirection, tmpPrecomputedVelocity, PRECOMPUTE_TIME_PER_FRAME);
            precomputationResult.setPosition(tmpPrecomputedPosition);
            precomputationResult.setDirection(tmpPrecomputedDirection);
            precomputationResult.setVelocity(tmpPrecomputedVelocity);
            precomputationResult.setPassedTime(passedTime);
            if (endCondition.test(precomputationResult)) {
                return precomputationResult;
            }
        }
        return null;
    }

    protected void updateTransform(Vector3f position, Vector3f direction, Vector3f velocity, float tpf) {
        velocity.setY(velocity.getY() - (gravitation * tpf));
        tmpNewPosition.set(position);
        tmpNewPosition.addLocal(velocity.mult(tpf));
        tryMoveToPosition(position, velocity, tmpNewPosition);
        if (position.getY() < 0) {
            position.setY(0);
            velocity.setY(-1 * bouncinessGround * velocity.getY());
        }
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

    protected void slowDown(Vector3f velocity, float strength, int axis, float tpf) {
        float currentValue = velocity.get(axis);
        float currentValueAbsolute = FastMath.abs(currentValue);
        float amountToSubtractAbsolute = (strength * tpf);
        float amountToSubtract = Math.signum(currentValue) * amountToSubtractAbsolute;
        if (amountToSubtractAbsolute < currentValueAbsolute) {
            velocity.set(axis, currentValue - amountToSubtract);
        } else {
            velocity.set(axis, 0);
        }
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public void lookAt_XZ(Vector3f position) {
        direction.set(position.getX(), 0, position.getZ()).subtractLocal(this.position.getX(), 0, this.position.getZ()).normalizeLocal();
    }

    public void setDirection(Vector3f direction) {
        this.direction.set(direction);
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity.set(velocity);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public Vector3f getVelocity() {
        return velocity;
    }
}
