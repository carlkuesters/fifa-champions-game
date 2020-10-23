/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game.situations;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.carlkuesters.fifachampions.game.MathUtil;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.Situation;

/**
 *
 * @author Carl
 */
public class KickOffSituation extends Situation {
    
    public KickOffSituation(PlayerObject startingPlayer) {
        super(startingPlayer, true);
    }

    @Override
    public Vector3f getBallPosition() {
        return Vector3f.ZERO;
    }

    @Override
    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        if (playerObject == startingPlayer) {
            return new Vector3f(0.7f * playerObject.getTeam().getSide(), 0, 0);
        }
        Vector2f position = playerObject.getTeam().getIdealLocation(playerObject);
        position = playerObject.getTeam().transformLocationToOwnSide(position);
        return MathUtil.convertTo3D_XZ(position);
    }

    @Override
    public Vector3f getPlayerDirection(PlayerObject playerObject) {
        float x = playerObject.getTeam().getSide();
        if (playerObject == startingPlayer) {
            x *= -1;
        }
        return new Vector3f(x, 0, 0);
    }
}
