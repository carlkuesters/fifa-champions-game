package com.carlkuesters.fifachampions.game.situations;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.carlkuesters.fifachampions.game.PlayerObject;

public class ThrowInSituation extends BallSituation {
    
    public ThrowInSituation(PlayerObject startingPlayer, Vector3f throwInPosition) {
        super(startingPlayer);
        this.throwInPosition = throwInPosition;
    }
    private Vector3f throwInPosition;

    @Override
    protected Vector3f calculateBallPosition() {
        return new Vector3f(throwInPosition.getX(), 1.8f, throwInPosition.getZ());
    }

    @Override
    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        if (playerObject == startingPlayer) {
            return throwInPosition;
        }
        return super.getPlayerPosition(playerObject);
    }

    @Override
    public Vector3f getStartingPlayerDirection() {
        return new Vector3f(0, 0, -1 * FastMath.sign(throwInPosition.getZ()));
    }
}
