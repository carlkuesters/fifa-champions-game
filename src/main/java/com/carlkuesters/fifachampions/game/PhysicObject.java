/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

/**
 *
 * @author Carl
 */
public class PhysicObject extends GameObject {

    private final float gravitation = 9.81f;
    protected float bouncinessGround = 0;
    protected float bouncinessWalls = 0;
    private Vector3f newPosition = new Vector3f();
    protected Vector3f position = new Vector3f();
    protected Vector3f direction = new Vector3f(0, 0, 1);
    protected Vector3f velocity = new Vector3f();

    @Override
    public void update(float tpf) {
        super.update(tpf);
        velocity.setY(velocity.getY() - (gravitation * tpf));
        newPosition.set(position);
        newPosition.addLocal(velocity.mult(tpf));
        tryMoveToPosition(newPosition);
        if (position.getY() < 0) {
            position.setY(0);
            velocity.setY(-1 * bouncinessGround * velocity.getY());
        }
    }

    public void tryMoveToPosition(Vector3f position) {
        PhysicsDirection collidingWallDirection = getCollidingWallDirectionWhenMoving(position);
        if (collidingWallDirection == null) {
            setPosition(position);
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

    private PhysicsDirection getCollidingWallDirectionWhenMoving(Vector3f position) {
        // Walls around the field
        if ((position.getX() < -57.35f) || (position.getX() > 58.15f)) {
            return PhysicsDirection.X;
        } else if (FastMath.abs(position.getZ()) > 38) {
            return PhysicsDirection.Z;
        }
        // Goal nets
        boolean isInGoal = Game.isInsideGoal(this.position);
        boolean willBeInGoal = Game.isInsideGoal(position);
        if ((isInGoal != willBeInGoal) && (Game.getOutside(this.position) != null)) {
            Vector3f outsideGoalPosition = (isInGoal ? position : this.position);
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

    protected void slowDown(float strength, float tpf) {
        Vector3f amountToSubstract = velocity.normalize().multLocal(strength * tpf);
        if (amountToSubstract.lengthSquared() < velocity.lengthSquared()) {
            velocity.subtractLocal(amountToSubstract);
        } else {
            velocity.set(0, 0, 0);
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
