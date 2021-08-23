package com.carlkuesters.fifachampions.game;

import com.jme3.math.Vector3f;

public class AnticipatedPlayer {

    public AnticipatedPlayer(PlayerObject playerObject, Vector3f anticipatedPosition) {
        this.playerObject = playerObject;
        this.anticipatedPosition = anticipatedPosition;
    }
    private PlayerObject playerObject;
    private Vector3f anticipatedPosition;

    public PlayerObject getPlayerObject() {
        return playerObject;
    }

    public Vector3f getAnticipatedPosition() {
        return anticipatedPosition;
    }
}
