/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game.situations;

import com.jme3.math.Vector3f;
import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.game.MathUtil;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.Situation;
import com.carlkuesters.fifachampions.game.Team;

/**
 *
 * @author Carl
 */
public class GoalKickSituation extends Situation {
    
    public GoalKickSituation(Team team) {
        super(team.getGoalkeeper(), true);
        this.team = team;
    }
    private Team team;

    @Override
    public Vector3f getBallPosition() {
        return new Vector3f((-1 * team.getSide()) * (Game.FIELD_HALF_WIDTH - 5), 0, 0);
    }

    @Override
    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        if (playerObject == startingPlayer) {
            return new Vector3f((-1 * team.getSide()) * (Game.FIELD_HALF_WIDTH - 4.5f), 0, 0);
        }
        return MathUtil.convertTo3D_XZ(playerObject.getTeam().getIdealLocation(playerObject));
    }

    @Override
    public Vector3f getPlayerDirection(PlayerObject playerObject) {
        return new Vector3f(playerObject.getTeam().getSide(), 0, 0);
    }
}
