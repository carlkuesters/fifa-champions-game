package com.carlkuesters.fifachampions.game;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

public class Ball extends PhysicsObject {

    public Ball() {
        bouncinessGround = 0.5f;
        bouncinessWalls = 0.5f;
    }
    private PlayerObject owner;
    private PlayerObject lastTouchedOwner;
    private float lastTouchReplayTime;
    private Vector3f lastTouchPosition = new Vector3f();
    private LinkedList<OffsidePlayer> lastTouchOffsidePlayers = new LinkedList<>();
    private Vector3f lastPosition = new Vector3f();
    private float rotationProgressInRadians;

    @Override
    protected void updateTransform(Vector3f position, Quaternion rotation, Vector3f velocity, float tpf) {
        lastPosition.set(position);
        super.updateTransform(position, rotation, velocity, tpf);
        if (owner != null) {
            placeInFrontOfOwner(position, velocity);
            velocity.set(position.subtract(lastPosition)).divideLocal(tpf);
        }
        Vector3f movedDistance = position.subtract(lastPosition);
        Vector3f rotationAxis = new Quaternion().fromAngleAxis(-1 * FastMath.HALF_PI, movedDistance).mult(Vector3f.UNIT_Y).normalizeLocal();
        rotationProgressInRadians += (movedDistance.length() * 2);
        Quaternion moveRotation = new Quaternion().fromAngleAxis(rotationProgressInRadians, rotationAxis);
        setDirection(moveRotation.mult(Vector3f.UNIT_Y));
    }

    @Override
    protected boolean isAffectedByFrictionXZ() {
        return (owner == null);
    }

    public void accelerate(Vector3f velocity, boolean canTriggerOffside) {
        this.owner = null;
        this.velocity.set(velocity);
        lastTouchReplayTime = game.getReplayTime();
        lastTouchPosition.set(position);
        lastTouchOffsidePlayers.clear();
        if (canTriggerOffside) {
            Team allyTeam = lastTouchedOwner.getTeam();
            // Trigger offside only from already inside enemy side
            if (Math.signum(position.getX()) == (game.getHalfTimeSideFactor() * allyTeam.getSide())) {
                Team enemyTeam = game.getTeams()[(allyTeam == game.getTeams()[0]) ? 1 : 0];
                float goalX = (game.getHalfTimeSideFactor() * allyTeam.getSide() * Game.FIELD_HALF_WIDTH);
                float distanceToGoalPassingAlly = FastMath.abs(goalX - lastTouchedOwner.getPosition().getX());
                float distanceToGoalSecondLastEnemy = Arrays.stream(enemyTeam.getPlayers())
                        .map(enemyPlayer -> FastMath.abs(goalX - enemyPlayer.getPosition().getX()))
                        .sorted(Comparator.naturalOrder())
                        .skip(1)
                        .findFirst()
                        .get();
                for (PlayerObject allyPlayer : allyTeam.getPlayers()) {
                    if (allyPlayer != owner) {
                        float distanceToGoalAlly = FastMath.abs(goalX - allyPlayer.getPosition().getX());
                        if ((distanceToGoalAlly < distanceToGoalSecondLastEnemy) && (distanceToGoalAlly < distanceToGoalPassingAlly)) {
                            lastTouchOffsidePlayers.add(new OffsidePlayer(allyPlayer, allyPlayer.getPosition().clone()));
                        }
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
                placeInFrontOfOwner(position, velocity);
                velocity.set(0, 0);
            }
            if (canTriggerOffside) {
                OffsidePlayer offsidePlayer = lastTouchOffsidePlayers.stream().filter(op -> op.getPlayerObject() == owner).findAny().orElse(null);
                if (offsidePlayer != null) {
                    game.onOffside(offsidePlayer, lastTouchPosition, lastTouchReplayTime);
                }
            }
        }
        lastTouchOffsidePlayers.clear();
    }

    private void placeInFrontOfOwner(Vector3f position, Vector3f velocity) {
        TempVars tempVars = TempVars.get();
        Vector3f newPosition = tempVars.vect1;
        newPosition.set(owner.getPosition().getX(), 0, owner.getPosition().getZ());
        float directionFactor = 0.7f;
        newPosition.addLocal(directionFactor * owner.getDirection().getX(), 0, directionFactor * owner.getDirection().getZ());
        tryMoveToPosition(position, velocity, newPosition);
        tempVars.release();
    }

    public PlayerObject getOwner() {
        return owner;
    }

    public PlayerObject getLastTouchedOwner() {
        return lastTouchedOwner;
    }
}
