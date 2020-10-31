package com.carlkuesters.fifachampions.game.situations;

import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.Team;
import com.carlkuesters.fifachampions.game.buttons.behaviours.NearFreeKickShootButtonBehaviour;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class NearFreeKickSituation extends FreeKickSituation {

    public NearFreeKickSituation(PlayerObject startingPlayer, Vector3f ballPosition) {
        super(startingPlayer, ballPosition);
    }
    private float optimalShootStrength;
    private Vector3f targetInGoalPosition;
    private Vector2f targetCursorDirection = new Vector2f();

    @Override
    public void start() {
        super.start();
        Team opponentTeam = game.getTeams()[(startingPlayer.getTeam().getSide() == 1) ? 1 : 0];
        float nearFreeKickProximity = (game.getDistanceToGoalLine(ballPosition, opponentTeam) / (Game.FIELD_HALF_WIDTH - Game.MAXIMUM_NEAR_FREE_KICK_DISTANCE));
        optimalShootStrength = ((1 + nearFreeKickProximity) / 3);
        targetInGoalPosition = new Vector3f(game.getHalfTimeSideFactor() * startingPlayer.getTeam().getSide() * Game.FIELD_HALF_WIDTH, Game.GOAL_HEIGHT / 2, 0);
        game.setCameraPerspective(getCameraPerspectiveTowardsEnemyGoal(4, 14), 2);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        float maximumMovedCursorDistance = (tpf * (0.5f * (Game.GOAL_Z_TOP - Game.GOAL_Z_BOTTOM)));
        float movedCursorDistanceY = (-1 * targetCursorDirection.getY() * maximumMovedCursorDistance);
        float movedCursorDistanceZ = (game.getHalfTimeSideFactor() * targetCursorDirection.getX() * maximumMovedCursorDistance);
        float newTargetInGoalPositionY = Math.max(0, Math.min(targetInGoalPosition.getY() + movedCursorDistanceY, Game.GOAL_HEIGHT));
        float newTargetInGoalPositionZ = Math.max(Game.GOAL_Z_BOTTOM, Math.min(targetInGoalPosition.getZ() + movedCursorDistanceZ, Game.GOAL_Z_TOP));
        targetInGoalPosition.setY(newTargetInGoalPositionY);
        targetInGoalPosition.setZ(newTargetInGoalPositionZ);
    }

    @Override
    protected Vector3f getStartingPlayerPosition(Vector3f directionToOpponentGoal) {
        // TODO: Left foots have negated angle
        float approachAngle = (1 + ((true ? -1 : 1) * 0.25f)) * FastMath.PI;
        Vector3f approachDirection = new Quaternion().fromAngleAxis(approachAngle, Vector3f.UNIT_Y).mult(directionToOpponentGoal);
        return ballPosition.add(approachDirection.multLocal(NearFreeKickShootButtonBehaviour.APPROACH_DURATION * NearFreeKickShootButtonBehaviour.APPROACH_SPEED));
    }

    public float getOptimalShootStrength() {
        return optimalShootStrength;
    }

    public void setTargetCursorDirection(Vector2f targetCursorDirection) {
        this.targetCursorDirection.set(targetCursorDirection);
    }

    public Vector3f getTargetInGoalPosition() {
        return targetInGoalPosition;
    }
}
