package com.carlkuesters.fifachampions.game.situations;

import com.jme3.math.Vector3f;
import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.Team;

public class GoalKickSituation extends BallSituation {
    
    public GoalKickSituation(Team team, float horizontalPosition) {
        super(team.getGoalkeeper());
        this.team = team;
        this.horizontalPosition = horizontalPosition;
    }
    private Team team;
    private float horizontalPosition;

    @Override
    public void start() {
        super.start();
        game.setCameraPerspective(getCameraPerspectiveTowardsEnemyGoal(3, 11), 1);
    }

    @Override
    public Vector3f getBallPosition() {
        return new Vector3f((-1 * game.getHalfTimeSideFactor() * team.getSide()) * (Game.FIELD_HALF_WIDTH - 6.05f), 0, horizontalPosition * 8.75f);
    }

    @Override
    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        if (playerObject == startingPlayer) {
            return getBallApproachPosition(getDirectionToOpponentGoal());
        }
        return super.getPlayerPosition(playerObject);
    }
}
