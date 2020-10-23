/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game.situations;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.carlkuesters.fifachampions.game.MathUtil;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.Situation;

/**
 *
 * @author Carl
 */
public class ThrowInSituation extends Situation {
    
    public ThrowInSituation(PlayerObject startingPlayer, Vector3f throwInPosition) {
        super(startingPlayer, false);
        this.throwInPosition = throwInPosition;
    }
    private Vector3f throwInPosition;

    @Override
    public Vector3f getBallPosition() {
        return new Vector3f(throwInPosition.getX(), 1.8f, throwInPosition.getZ());
    }

    @Override
    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        if (playerObject == startingPlayer) {
            return throwInPosition;
        }
        return MathUtil.convertTo3D_XZ(playerObject.getTeam().getIdealLocation(playerObject));
    }

    @Override
    public Vector3f getPlayerDirection(PlayerObject playerObject) {
        if (playerObject == startingPlayer) {
            return new Vector3f(0, 0, -1 * FastMath.sign(throwInPosition.getZ()));
        }
        return throwInPosition.subtract(playerObject.getPosition()).normalizeLocal();
    }
}
