package com.carlkuesters.fifachampions.game.situations;

import com.carlkuesters.fifachampions.game.*;
import com.jme3.math.Vector3f;

public abstract class BallSituation extends Situation {

    public BallSituation(PlayerObject startingPlayer, boolean isFromGroundOrHands) {
        this.startingPlayer = startingPlayer;
        this.isFromGroundOrHands = isFromGroundOrHands;
    }
    protected PlayerObject startingPlayer;
    private boolean isFromGroundOrHands;

    @Override
    public void start() {
        Ball ball = game.getBall();
        ball.setOwner(startingPlayer, false);
        ball.setPosition(getBallPosition());
        for (Team team : game.getTeams()) {
            for (PlayerObject playerObject : team.getPlayers()) {
                playerObject.setPosition(getPlayerPosition(playerObject));
                playerObject.setDirection(getPlayerDirection(playerObject));
            }
        }
        startingPlayer.setCanMove(false);
        game.selectPlayer(startingPlayer);
    }

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
}
