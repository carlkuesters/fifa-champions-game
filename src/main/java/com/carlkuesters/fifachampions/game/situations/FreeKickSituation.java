package com.carlkuesters.fifachampions.game.situations;

import com.jme3.math.Vector3f;
import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.Situation;

public class FreeKickSituation extends Situation {
    
    public FreeKickSituation(PlayerObject startingPlayer, Vector3f ballPosition) {
        super(startingPlayer, true);
        this.startingPlayer = startingPlayer;
        this.ballPosition = ballPosition.clone();
    }
    private static final float MINIMUM_DISTANCE_TO_BALL = 10;
    private PlayerObject startingPlayer;
    private Vector3f ballPosition;

    @Override
    public Vector3f getBallPosition() {
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
        Vector3f distanceToBall = ballPosition.subtract(playerPosition);
        if (distanceToBall.lengthSquared() < (MINIMUM_DISTANCE_TO_BALL * MINIMUM_DISTANCE_TO_BALL)) {
            playerPosition.set(ballPosition.subtract(distanceToBall.normalize().multLocal(MINIMUM_DISTANCE_TO_BALL)));
        }
        return playerPosition;
    }
}
