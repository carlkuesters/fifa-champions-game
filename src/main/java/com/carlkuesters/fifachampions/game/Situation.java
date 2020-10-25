package com.carlkuesters.fifachampions.game;

import com.jme3.math.Vector3f;

public abstract class Situation {

    public Situation(PlayerObject startingPlayer, boolean isFromGroundOrHands) {
        this.startingPlayer = startingPlayer;
        this.isFromGroundOrHands = isFromGroundOrHands;
    }
    protected PlayerObject startingPlayer;
    private boolean isFromGroundOrHands;
    protected Game game;

    public abstract Vector3f getBallPosition();

    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        return MathUtil.convertTo3D_XZ(playerObject.getTeam().getIdealLocation(playerObject));
    }

    public Vector3f getPlayerDirection(PlayerObject playerObject) {
        return game.getBall().getPosition().subtract(playerObject.getPosition()).setY(0).normalizeLocal();
    }

    public PlayerObject getStartingPlayer() {
        return startingPlayer;
    }

    public boolean isFromGroundOrHands() {
        return isFromGroundOrHands;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
