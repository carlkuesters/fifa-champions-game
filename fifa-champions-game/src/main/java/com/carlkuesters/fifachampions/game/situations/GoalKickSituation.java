package com.carlkuesters.fifachampions.game.situations;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.Team;
import lombok.Setter;

public class GoalKickSituation extends BallSituation {
    
    public GoalKickSituation(Team team, float horizontalPosition) {
        super(team.getGoalkeeper());
        this.team = team;
        this.horizontalPosition = horizontalPosition;
    }
    private Team team;
    private float horizontalPosition;
    private float targetAngle;
    @Setter
    private float targetAngleDirection;

    @Override
    protected Vector3f calculateBallPosition() {
        // So the ball aligns visually with the 5m line
        float ballOffsetX = 0.18f;
        return new Vector3f((-1 * game.getHalfTimeSideFactor() * team.getSide()) * (Game.FIELD_HALF_WIDTH - (5 + ballOffsetX)), 0, horizontalPosition * 8.75f);
    }

    @Override
    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        if (playerObject == startingPlayer) {
            return getBallApproachPosition(getDirectionToOpponentGoal());
        }
        // Move out of penalty area
        Vector3f playerPosition = super.getPlayerPosition(playerObject);
        if (playerObject != startingPlayer) {
            moveOutOfPenaltyArea(playerPosition, true, 1);
        }
        return playerPosition;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        targetAngle = Math.max(-1 * FastMath.HALF_PI, Math.min(targetAngle + (targetAngleDirection * 1 * FastMath.HALF_PI * tpf), FastMath.HALF_PI));
        game.setCameraPerspective(getCameraPerspectiveTowardsEnemyGoal(3, 11, targetAngle), 1);
    }

    public Vector3f getTargetDirection() {
        return game.getCameraPerspective().getDirection().clone().setY(0);
    }
}
