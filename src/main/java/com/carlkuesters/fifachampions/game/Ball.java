/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.Comparator;
import java.util.LinkedList;

/**
 *
 * @author Carl
 */
public class Ball extends PhysicObject {

    public Ball() {
        bouncinessGround = 0.5f;
        bouncinessWalls = 0.5f;
    }
    private PlayerObject owner;
    private PlayerObject lastTouchedOwner;
    private Vector3f lastTouchedPosition = new Vector3f();
    private LinkedList<PlayerObject> lastTouchedOffsidePlayers = new LinkedList<PlayerObject>();
    private Vector3f lastPosition = new Vector3f();
    private float rotationProgressInRadians;

    @Override
    public void update(float tpf) {
        lastPosition.set(position);
        super.update(tpf);
        if (owner != null) {
            placeInFrontOfOwner();
            velocity.set(position.subtract(lastPosition)).divideLocal(tpf);
        } else {
            float slowDownStrength = ((position.getY() > 0) ? 3 : 4);
            slowDown(slowDownStrength, tpf);
        }
        Vector3f movedDistance = position.subtract(lastPosition);
        Vector3f rotationAxis = new Quaternion().fromAngleAxis(-1 * FastMath.HALF_PI, movedDistance).mult(Vector3f.UNIT_Y).normalizeLocal();
        rotationProgressInRadians += (movedDistance.length() * 2);
        Quaternion moveRotation = new Quaternion().fromAngleAxis(rotationProgressInRadians, rotationAxis);
        direction.set(moveRotation.mult(Vector3f.UNIT_Y));
    }

    public void accelerate(Vector3f velocity, boolean canTriggerOffside) {
        this.owner = null;
        this.velocity.set(velocity);
        lastTouchedPosition.set(position);
        lastTouchedOffsidePlayers.clear();
        if (canTriggerOffside) {
            Team allyTeam = lastTouchedOwner.getTeam();
            Team enemyTeam = game.getTeams()[(allyTeam == game.getTeams()[0]) ? 1 : 0];
            // TODO: Consider halftime
            float goalX = ((allyTeam == game.getTeams()[0]) ? 1 : -1) * Game.FIELD_HALF_WIDTH;
            float distanceToGoalPassingAlly = FastMath.abs(goalX - lastTouchedOwner.getPosition().getX());
            float distanceToGoalSecondLastEnemy = enemyTeam.getPlayers().stream()
                    .map(enemyPlayer -> FastMath.abs(goalX - enemyPlayer.getPosition().getX()))
                    .sorted(Comparator.naturalOrder())
                    .skip(1)
                    .findFirst()
                    .get();
            for (PlayerObject allyPlayer : allyTeam.getPlayers()) {
                if (allyPlayer != owner) {
                    float distanceToGoalAlly = FastMath.abs(goalX - allyPlayer.getPosition().getX());
                    if ((distanceToGoalAlly < distanceToGoalSecondLastEnemy) && (distanceToGoalAlly < distanceToGoalPassingAlly)) {
                        lastTouchedOffsidePlayers.add(allyPlayer);
                    }
                }
            }
        }
    }

    public void setOwner(PlayerObject owner, boolean canTriggerOffside) {
        this.owner = owner;
        if (owner != null) {
            lastTouchedOwner = owner;
            game.selectPlayer(owner);
            owner.getTeam().getGoalkeeper().setIsPressuring(false);
            if (!owner.onBallPickUp()) {
                placeInFrontOfOwner();
                velocity.set(0, 0);
            }
            if (lastTouchedOffsidePlayers.contains(owner)) {
                game.onOffside(owner, lastTouchedPosition);
            }
        }
        lastTouchedOffsidePlayers.clear();
    }

    private void placeInFrontOfOwner() {
        TempVars tempVars = TempVars.get();
        Vector3f newPosition = tempVars.vect1;
        newPosition.set(owner.getPosition().getX(), 0, owner.getPosition().getZ());
        float directionFactor = 0.7f;
        newPosition.addLocal(directionFactor * owner.getDirection().getX(), 0, directionFactor * owner.getDirection().getZ());
        tryMoveToPosition(newPosition);
        tempVars.release();
    }

    public PlayerObject getOwner() {
        return owner;
    }

    public PlayerObject getLastTouchedOwner() {
        return lastTouchedOwner;
    }
}
