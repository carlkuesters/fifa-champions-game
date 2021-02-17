package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.*;
import com.carlkuesters.fifachampions.game.situations.PenaltySituation;
import com.jme3.math.Vector3f;

public class PenaltyShootButtonBehaviour extends ApproachShootButtonBehaviour<PenaltySituation> {

    public PenaltyShootButtonBehaviour() {
        maxChargedDuration = 0;
    }

    @Override
    protected void shoot(PenaltySituation penaltySituation, PlayerObject playerObject, float strength) {
        Game game = playerObject.getGame();
        Ball ball = game.getBall();
        // Shooter
        Vector3f shootGoalLinePosition = getGoalLinePosition(game, playerObject.getTeam(), penaltySituation.getTargetShootDirection());
        int targetVelocityX = (game.getHalfTimeSideFactor() * playerObject.getTeam().getSide() * 20);
        Vector3f ballVelocity = ball.getInitialVelocity_ByTargetVelocityX(shootGoalLinePosition,targetVelocityX);
        playerObject.shoot(ballVelocity);
        // Goalkeeper
        PlayerObject goalkeeper = penaltySituation.getGoalkeeper();
        PhysicsPrecomputationResult ballInGoalResult = game.precomputeBallTransformUntilInsideGoal(goalkeeper.getTeam());
        Vector3f goalkeeperGoalLinePosition = getGoalLinePosition(game, playerObject.getTeam(), penaltySituation.getTargetGoalkeeperDirection());
        GoalkeeperJump goalkeeperJump = goalkeeper.getGoalkeeperJump(goalkeeperGoalLinePosition, ballInGoalResult.getPassedTime());
        goalkeeper.executeGoalkeeperJump(goalkeeperJump);
    }

    private Vector3f getGoalLinePosition(Game game, Team shootingTeam, float targetDirection) {
        int goalSideFactor = (game.getHalfTimeSideFactor() * shootingTeam.getSide());
        return new Vector3f(goalSideFactor * Game.FIELD_HALF_WIDTH, Game.GOAL_HEIGHT / 2, goalSideFactor * targetDirection * 3);
    }
}
