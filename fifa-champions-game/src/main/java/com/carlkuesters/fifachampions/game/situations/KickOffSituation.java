package com.carlkuesters.fifachampions.game.situations;

import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.game.MathUtil;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class KickOffSituation extends BallSituation {
    
    public KickOffSituation(PlayerObject startingPlayer) {
        super(startingPlayer);
    }

    @Override
    public void start() {
        super.start();
        game.setPlayersCanMove(false);
    }

    @Override
    protected Vector3f calculateBallPosition() {
        return Vector3f.ZERO;
    }

    @Override
    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        if (playerObject == startingPlayer) {
            return new Vector3f(game.getHalfTimeSideFactor() * playerObject.getTeam().getSide() * 0.7f, 0, 0);
        }
        // Transfer ideal location to own side
        Vector2f position = playerObject.getTeam().getIdealLocation(playerObject);
        position.subtractLocal(game.getHalfTimeSideFactor() * playerObject.getTeam().getSide() * Game.FIELD_HALF_WIDTH, 0);
        position.multLocal(new Vector2f(0.5f, 1));
        return MathUtil.convertTo3D_XZ(position);
    }

    @Override
    public Vector3f getStartingPlayerDirection() {
        return new Vector3f(-1 * game.getHalfTimeSideFactor() * startingPlayer.getTeam().getSide(), 0, 0);
    }
}
