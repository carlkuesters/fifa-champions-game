package com.carlkuesters.fifachampions.game.situations;

import com.carlkuesters.fifachampions.game.PlayerObject;
import com.jme3.math.Vector3f;

public class FarFreeKickSituation extends FreeKickSituation {

    public FarFreeKickSituation(PlayerObject startingPlayer, Vector3f ballPosition) {
        super(startingPlayer, ballPosition);
    }

    @Override
    protected Vector3f getStartingPlayerPosition(Vector3f directionToOpponentGoal) {
        return ballPosition.subtract(directionToOpponentGoal);
    }
}
