package com.carlkuesters.fifachampions.game;

import com.jme3.math.Vector2f;

public class Formation {

    public Formation(Vector2f... positions) {
        this.positions = positions;
    }
    private Vector2f[] positions;

    public Vector2f getLocation(int playerIndex) {
        return positions[playerIndex];
    }
}
