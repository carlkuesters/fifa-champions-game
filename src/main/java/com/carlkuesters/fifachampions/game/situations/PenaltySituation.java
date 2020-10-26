package com.carlkuesters.fifachampions.game.situations;

import com.jme3.math.Vector3f;
import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.game.Goalkeeper;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.Team;

public class PenaltySituation extends BallSituation {

    public PenaltySituation(Team team) {
        super(getStartingPlayer(team), true);
        this.team = team;
    }
    private Team team;
    private Vector3f ballPosition;

    // TODO: Properly choosing a starting player (based on position?)
    private static PlayerObject getStartingPlayer(Team team) {
        return team.getPlayers().get(team.getPlayers().size() - 1);
    }

    @Override
    public Vector3f getBallPosition() {
        ballPosition = new Vector3f(game.getHalfTimeSideFactor() * team.getSide() * (Game.FIELD_HALF_WIDTH - 11), 0, 0);
        return ballPosition;
    }

    @Override
    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        if (playerObject == startingPlayer) {
            Vector3f centerOpponentGoal = new Vector3f(game.getHalfTimeSideFactor() * startingPlayer.getTeam().getSide() * Game.FIELD_HALF_WIDTH, 0, 0);
            Vector3f directionToOpponentGoal = centerOpponentGoal.subtract(ballPosition).normalizeLocal();
            return ballPosition.subtract(directionToOpponentGoal.mult(0.5f));
        }
        Vector3f playerPosition = super.getPlayerPosition(playerObject);
        if (!(playerObject.getPlayer() instanceof Goalkeeper)) {
            float maximumAbsoluteX = (Game.FIELD_HALF_WIDTH - Game.PENALTY_AREA_WIDTH);
            // Move just a little bit away from the penalty area, so the players are not directly on the line
            maximumAbsoluteX -= 0.25f;
            playerPosition.setX(Math.max(-1 * maximumAbsoluteX, Math.min(playerPosition.getX(), maximumAbsoluteX)));
        }
        return playerPosition;
    }
}
