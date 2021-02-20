package com.carlkuesters.fifachampions.game.situations;

import com.jme3.math.Vector3f;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.Team;

public class CornerKickSituation extends BallSituation {

    public CornerKickSituation(Team team, Vector3f ballPosition) {
        super(getStartingPlayer(team));
        this.ballPosition = ballPosition.clone();
    }
    private Vector3f ballPosition;

    // TODO: Properly choosing a starting player (based on position?)
    private static PlayerObject getStartingPlayer(Team team) {
        return team.getPlayers()[team.getPlayers().length - 1];
    }

    @Override
    public Vector3f getBallPosition() {
        return ballPosition;
    }

    @Override
    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        if (playerObject == startingPlayer) {
            // Step a bit out of the field away from the ball
            return ballPosition.add(ballPosition.normalize().mult(0.5f));
        }
        return super.getPlayerPosition(playerObject);
    }
}
