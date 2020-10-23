/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game;

import com.jme3.math.Vector3f;

/**
 *
 * @author Carl
 */
public abstract class Situation {

    public Situation(PlayerObject startingPlayer, boolean isFromGroundOrHands) {
        this.startingPlayer = startingPlayer;
        this.isFromGroundOrHands = isFromGroundOrHands;
    }
    protected PlayerObject startingPlayer;
    private boolean isFromGroundOrHands;

    public abstract Vector3f getBallPosition();

    public abstract Vector3f getPlayerPosition(PlayerObject playerObject);

    public abstract Vector3f getPlayerDirection(PlayerObject playerObject);

    public PlayerObject getStartingPlayer() {
        return startingPlayer;
    }

    public boolean isFromGroundOrHands() {
        return isFromGroundOrHands;
    }
}
