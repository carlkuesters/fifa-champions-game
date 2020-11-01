package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.*;
import com.carlkuesters.fifachampions.game.situations.PenaltySituation;
import com.jme3.math.Vector3f;

public class PenaltyShootButtonBehaviour extends ApproachShootButtonBehaviour<PenaltySituation> {

    @Override
    protected void shoot(PenaltySituation penaltySituation, PlayerObject playerObject, float strength) {
        Game game = playerObject.getGame();
        Ball ball = game.getBall();
        Vector3f shootGoalLinePosition = getGoalLinePosition(game, playerObject.getTeam(), penaltySituation.getTargetShootDirection());
        Vector3f ballVelocity = ball.getInitialVelocity_ByTargetVelocityX(shootGoalLinePosition, 20);
        playerObject.shoot(ballVelocity);
        Team goalTeam = game.getTeams()[(playerObject.getTeam() == game.getTeams()[0]) ? 1 : 0];
        PhysicsPrecomputationResult ballInGoalResult = game.precomputeBallTransformUntilInsideGoal(goalTeam);
        Vector3f goalkeeperGoalLinePosition = getGoalLinePosition(game, playerObject.getTeam(), penaltySituation.getTargetGoalkeeperDirection());
        GoalkeeperJump goalkeeperJump = goalTeam.getGoalkeeper().getGoalkeeperJump(goalkeeperGoalLinePosition, ballInGoalResult.getPassedTime());
        goalTeam.getGoalkeeper().executeGoalkeeperJump(goalkeeperJump);
    }

    private Vector3f getGoalLinePosition(Game game, Team shootingTeam, float targetDirection) {
        return new Vector3f(game.getHalfTimeSideFactor() * shootingTeam.getSide() * Game.FIELD_HALF_WIDTH, Game.GOAL_HEIGHT / 2, targetDirection * 3);
    }
}
