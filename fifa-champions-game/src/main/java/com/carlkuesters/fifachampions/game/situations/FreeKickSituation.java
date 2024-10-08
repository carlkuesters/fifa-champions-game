package com.carlkuesters.fifachampions.game.situations;

import com.jme3.math.Vector3f;
import com.carlkuesters.fifachampions.game.PlayerObject;

public abstract class FreeKickSituation extends BallSituation {
    
    public FreeKickSituation(PlayerObject startingPlayer, Vector3f ballPosition) {
        super(startingPlayer);
        this.startingPlayer = startingPlayer;
        this.ballPosition = ballPosition.clone();
    }
    private static final float MINIMUM_DISTANCE_TO_BALL = 9.15f;
    protected PlayerObject startingPlayer;
    protected Vector3f ballPosition;

    @Override
    protected Vector3f calculateBallPosition() {
        return ballPosition;
    }

    @Override
    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        if (playerObject == startingPlayer) {
            return getStartingPlayerPosition(getDirectionToOpponentGoal());
        }
        Vector3f playerPosition = super.getPlayerPosition(playerObject);
        moveAwayFromBall(playerPosition, MINIMUM_DISTANCE_TO_BALL);
        return playerPosition;
    }

    protected abstract Vector3f getStartingPlayerPosition(Vector3f directionToOpponentGoal);
}
