package com.carlkuesters.fifachampions.game.situations;

import com.carlkuesters.fifachampions.game.*;
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

    protected CameraPerspective getCameraPerspectiveTowardsEnemyGoal(float y, float distance) {
        Vector3f cameraPosition = getBallPosition().add(0, y, 0);
        Vector3f cameraDirection = getCenterOpponentGoal().clone().setY(Game.GOAL_HEIGHT / 2).subtractLocal(cameraPosition).normalizeLocal();
        cameraPosition.subtractLocal(cameraDirection.mult(distance));
        return new CameraPerspective(cameraPosition, cameraDirection);
    }

    protected Vector3f getCenterOpponentGoal() {
        return new Vector3f(game.getHalfTimeSideFactor() * startingPlayer.getTeam().getSide() * Game.FIELD_HALF_WIDTH, 0, 0);
    }

    public abstract Vector3f getBallPosition();

    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        return MathUtil.convertTo3D_XZ(playerObject.getTeam().getIdealLocation(playerObject));
    }

    public Vector3f getStartingPlayerDirection() {
        return game.getBall().getPosition().subtract(startingPlayer.getPosition()).setY(0).normalizeLocal();
    }

    public PlayerObject getStartingPlayer() {
        return startingPlayer;
    }
}
