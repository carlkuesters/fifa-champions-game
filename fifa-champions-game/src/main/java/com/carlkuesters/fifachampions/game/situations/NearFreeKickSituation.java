package com.carlkuesters.fifachampions.game.situations;

import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.Team;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import lombok.Getter;
import lombok.Setter;

public class NearFreeKickSituation extends FreeKickSituation {

    public NearFreeKickSituation(PlayerObject startingPlayer, Vector3f ballPosition) {
        super(startingPlayer, ballPosition);
    }
    @Getter
    private float optimalShootStrength;
    @Getter
    private Vector3f targetInGoalPosition = new Vector3f();
    @Setter
    private Vector2f targetCursorDirection = new Vector2f();

    @Override
    public void start() {
        super.start();
        Team opponentTeam = game.getTeams()[(startingPlayer.getTeam().getSide() == 1) ? 1 : 0];
        float nearFreeKickProximity = (game.getDistanceToGoalLine(ballPosition, opponentTeam) / (Game.FIELD_HALF_WIDTH - Game.MAXIMUM_NEAR_FREE_KICK_DISTANCE));
        optimalShootStrength = ((1 + nearFreeKickProximity) / 3);
        targetInGoalPosition.set(game.getHalfTimeSideFactor() * startingPlayer.getTeam().getSide() * Game.FIELD_HALF_WIDTH, Game.GOAL_HEIGHT / 2, 0);
        game.setCameraPerspective(getCameraPerspectiveTowardsEnemyGoal(4, 14, 0), 2);
    }

    @Override
    protected Vector3f getStartingPlayerPosition(Vector3f directionToOpponentGoal) {
        return getBallApproachPosition(directionToOpponentGoal);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        float maximumMovedCursorDistance = (tpf * (Game.GOAL_DEPTH / 2));
        float movedCursorDistanceY = (-1 * targetCursorDirection.getY() * maximumMovedCursorDistance);
        float movedCursorDistanceZ = (game.getHalfTimeSideFactor() * targetCursorDirection.getX() * maximumMovedCursorDistance);
        float newTargetInGoalPositionY = Math.max(0, Math.min(targetInGoalPosition.getY() + movedCursorDistanceY, Game.GOAL_HEIGHT));
        float newTargetInGoalPositionZ = Math.max((Game.GOAL_DEPTH / -2), Math.min(targetInGoalPosition.getZ() + movedCursorDistanceZ, (Game.GOAL_DEPTH / 2)));
        targetInGoalPosition.setY(newTargetInGoalPositionY);
        targetInGoalPosition.setZ(newTargetInGoalPositionZ);
    }
}
