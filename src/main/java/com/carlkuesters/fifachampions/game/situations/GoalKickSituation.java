package com.carlkuesters.fifachampions.game.situations;

import com.jme3.math.FastMath;
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
    private float targetAngle;
    private float targetAngleDirection;

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        targetAngle = Math.max(-1 * FastMath.HALF_PI, Math.min(targetAngle + (targetAngleDirection * 1 * FastMath.HALF_PI * tpf), FastMath.HALF_PI));
        game.setCameraPerspective(getCameraPerspectiveTowardsEnemyGoal(3, 11, targetAngle), 1);
    }

    @Override
    public Vector3f getBallPosition() {
        return new Vector3f((-1 * game.getHalfTimeSideFactor() * team.getSide()) * (Game.FIELD_HALF_WIDTH - 5.2f), 0, horizontalPosition * 8.75f);
    }

    @Override
    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        if (playerObject == startingPlayer) {
            return getBallApproachPosition(getDirectionToOpponentGoal());
        }
        return super.getPlayerPosition(playerObject);
    }

    public void setTargetAngleDirection(float targetAngleDirection) {
        this.targetAngleDirection = targetAngleDirection;
    }

    public Vector3f getTargetDirection() {
        return game.getCameraPerspective().getDirection().clone().setY(0);
    }
}
