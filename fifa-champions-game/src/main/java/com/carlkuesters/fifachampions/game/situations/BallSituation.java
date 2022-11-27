package com.carlkuesters.fifachampions.game.situations;

import com.carlkuesters.fifachampions.game.*;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ApproachShootButtonBehaviour;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public abstract class BallSituation extends Situation {

    public BallSituation(PlayerObject startingPlayer) {
        this.startingPlayer = startingPlayer;
    }
    protected PlayerObject startingPlayer;

    @Override
    public void start() {
        Ball ball = game.getBall();
        ball.setOwner(startingPlayer, false);
        ball.setPosition(getBallPosition());
        for (Team team : game.getTeams()) {
            for (PlayerObject playerObject : team.getPlayers()) {
                playerObject.setPosition(getPlayerPosition(playerObject));
            }
        }
        startingPlayer.setDirection(getStartingPlayerDirection());
        startingPlayer.setCanMove(false);
        game.selectPlayer(startingPlayer);
    }

    protected CameraPerspective getCameraPerspectiveTowardsEnemyGoal(float height, float distance, float angle) {
        Vector3f cameraPosition = getBallPosition().add(0, height, 0);
        Vector3f cameraDirection = getCenterOpponentGoal().clone().setY(Game.GOAL_HEIGHT / 2).subtractLocal(cameraPosition).normalizeLocal();
        new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y).multLocal(cameraDirection);
        cameraPosition.subtractLocal(cameraDirection.mult(distance));
        return new CameraPerspective(cameraPosition, cameraDirection);
    }

    public abstract Vector3f getBallPosition();

    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        return MathUtil.convertTo3D_XZ(playerObject.getTeam().getIdealLocation(playerObject));
    }

    protected Vector3f getBallApproachPosition(Vector3f directionToOpponentGoal) {
        // TODO: Left foots have negated angle
        float approachAngle = (1 + ((true ? -1 : 1) * 0.25f)) * FastMath.PI;
        Vector3f approachDirection = new Quaternion().fromAngleAxis(approachAngle, Vector3f.UNIT_Y).mult(directionToOpponentGoal);
        return getBallPosition().add(approachDirection.multLocal(ApproachShootButtonBehaviour.APPROACH_DURATION * ApproachShootButtonBehaviour.APPROACH_SPEED));
    }

    protected Vector3f getDirectionToOpponentGoal() {
        return getCenterOpponentGoal().subtractLocal(getBallPosition()).normalizeLocal();
    }

    private Vector3f getCenterOpponentGoal() {
        return new Vector3f(game.getHalfTimeSideFactor() * startingPlayer.getTeam().getSide() * Game.FIELD_HALF_WIDTH, 0, 0);
    }

    public Vector3f getStartingPlayerDirection() {
        return game.getBall().getPosition().subtract(startingPlayer.getPosition()).setY(0).normalizeLocal();
    }

    public PlayerObject getStartingPlayer() {
        return startingPlayer;
    }
}
