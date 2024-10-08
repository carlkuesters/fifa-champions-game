package com.carlkuesters.fifachampions.game.situations;

import com.carlkuesters.fifachampions.game.*;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ApproachShootButtonBehaviour;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import lombok.Getter;

public abstract class BallSituation extends Situation {

    public BallSituation(PlayerObject startingPlayer) {
        this.startingPlayer = startingPlayer;
    }
    @Getter
    protected PlayerObject startingPlayer;

    @Override
    public void start() {
        Ball ball = game.getBall();
        ball.setOwner(startingPlayer, false);
        ball.setPosition(calculateBallPosition());
        for (Team team : game.getTeams()) {
            for (PlayerObject playerObject : team.getPlayers()) {
                playerObject.setPosition(getPlayerPosition(playerObject));
            }
        }
        startingPlayer.setDirection(getStartingPlayerDirection());
        startingPlayer.setCanMove(false);
        game.selectPlayer(startingPlayer);
        game.setReplayRecordingEnabled(false);
    }

    protected abstract Vector3f calculateBallPosition();

    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        return MathUtil.convertTo3D_XZ(playerObject.getTeam().getIdealLocation(playerObject));
    }

    public Vector3f getStartingPlayerDirection() {
        return getBallPosition().subtract(startingPlayer.getPosition()).setY(0).normalizeLocal();
    }

    protected Vector3f getBallApproachPosition(Vector3f directionToOpponentGoal) {
        // TODO: Left foots have negated angle
        float approachAngle = (1 + ((true ? -1 : 1) * 0.25f)) * FastMath.PI;
        Vector3f approachDirection = new Quaternion().fromAngleAxis(approachAngle, Vector3f.UNIT_Y).mult(directionToOpponentGoal);
        return getBallPosition().add(approachDirection.multLocal(ApproachShootButtonBehaviour.APPROACH_DURATION * ApproachShootButtonBehaviour.APPROACH_SPEED));
    }

    protected CameraPerspective getCameraPerspectiveTowardsEnemyGoal(float height, float distance, float angle) {
        Vector3f cameraPosition = getBallPosition().add(0, height, 0);
        Vector3f cameraDirection = getCenterOpponentGoal().clone().setY(Game.GOAL_HEIGHT / 2).subtractLocal(cameraPosition).normalizeLocal();
        new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y).multLocal(cameraDirection);
        cameraPosition.subtractLocal(cameraDirection.mult(distance));
        return new CameraPerspective(cameraPosition, cameraDirection);
    }

    protected void moveAwayFromBall(Vector3f position, float minimumDistanceToBall) {
        Vector3f distanceToBall = getBallPosition().subtract(position);
        if (distanceToBall.lengthSquared() < (minimumDistanceToBall * minimumDistanceToBall)) {
            position.set(getBallPosition().subtract(distanceToBall.normalize().multLocal(minimumDistanceToBall)));
        }
    }

    protected void moveOutOfPenaltyArea(Vector3f position, boolean allyOrOpponentPenaltyArea, float additionalDistance) {
        float goalXFactor = (allyOrOpponentPenaltyArea ? -1 : 1) * getStartingPlayerXFactor();
        float goalX = goalXFactor * Game.FIELD_HALF_WIDTH;
        float distanceToGoalX = FastMath.abs(goalX - position.getX());
        float maximumDistanceToGoalX = Game.PENALTY_AREA_WIDTH + additionalDistance;
        if (distanceToGoalX < maximumDistanceToGoalX) {
            position.setX(goalXFactor * (Game.FIELD_HALF_WIDTH - maximumDistanceToGoalX));
        }
    }

    protected Vector3f getDirectionToOpponentGoal() {
        return getCenterOpponentGoal().subtractLocal(getBallPosition()).normalizeLocal();
    }

    private Vector3f getCenterOpponentGoal() {
        return new Vector3f(getOpponentGoalX(), 0, 0);
    }

    private float getOpponentGoalX() {
        return getStartingPlayerXFactor() * Game.FIELD_HALF_WIDTH;
    }

    private float getStartingPlayerXFactor() {
        return game.getHalfTimeSideFactor() * startingPlayer.getTeam().getSide();
    }

    private Vector3f getBallPosition() {
        return game.getBall().getPosition();
    }
}
