/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game;

import com.jme3.math.Vector2f;

/**
 *
 * @author Carl
 */
public class Formation {

    public Formation(Vector2f[] positions) {
        this.positions = positions;
    }
    private Vector2f[] positions;

    public Vector2f getLocation(int playerIndex) {
        return positions[playerIndex];
    }
}
