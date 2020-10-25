package com.carlkuesters.fifachampions.game.situations;

import com.jme3.math.Vector3f;
import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.Situation;
import com.carlkuesters.fifachampions.game.Team;

public class GoalKickSituation extends Situation {
    
    public GoalKickSituation(Team team) {
        super(team.getGoalkeeper(), true);
        this.team = team;
    }
    private Team team;

    @Override
    public Vector3f getBallPosition() {
        return new Vector3f((-1 * game.getHalfTimeSideFactor() * team.getSide()) * (Game.FIELD_HALF_WIDTH - 5), 0, 0);
    }

    @Override
    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        if (playerObject == startingPlayer) {
            return new Vector3f((-1 * game.getHalfTimeSideFactor() * team.getSide()) * (Game.FIELD_HALF_WIDTH - 4.5f), 0, 0);
        }
        return super.getPlayerPosition(playerObject);
    }
}
