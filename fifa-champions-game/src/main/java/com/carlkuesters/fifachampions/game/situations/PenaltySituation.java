package com.carlkuesters.fifachampions.game.situations;

import com.jme3.math.Vector3f;
import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.Team;
import lombok.Getter;
import lombok.Setter;

public class PenaltySituation extends BallSituation {

    public PenaltySituation(Team team) {
        super(getStartingPlayer(team));
        this.team = team;
    }
    private static final float BALL_DISTANCE_TO_GOAL = 11;
    private static final float MINIMUM_DISTANCE_TO_BALL = 9.15f;
    private Team team;
    @Getter
    @Setter
    private float targetShootDirection;
    @Getter
    @Setter
    private float targetGoalkeeperDirection;

    // TODO: Properly choosing a starting player (based on position?)
    private static PlayerObject getStartingPlayer(Team team) {
        return team.getPlayers()[team.getPlayers().length - 1];
    }

    @Override
    public void start() {
        super.start();
        game.setCameraPerspective(getCameraPerspectiveTowardsEnemyGoal(3, 10, 0), 2);
    }

    @Override
    protected Vector3f calculateBallPosition() {
        return new Vector3f(game.getHalfTimeSideFactor() * team.getSide() * (Game.FIELD_HALF_WIDTH - BALL_DISTANCE_TO_GOAL), 0, 0);
    }

    @Override
    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        if (playerObject == startingPlayer) {
            return getBallApproachPosition(getDirectionToOpponentGoal());
        }
        Vector3f playerPosition = super.getPlayerPosition(playerObject);
        if (playerObject != getGoalkeeper()) {
            moveOutOfPenaltyArea(playerPosition, false, 0);
            moveAwayFromBall(playerPosition, MINIMUM_DISTANCE_TO_BALL);
        }
        return playerPosition;
    }

    public PlayerObject getGoalkeeper() {
        Team goalTeam = game.getTeams()[(startingPlayer.getTeam() == game.getTeams()[0]) ? 1 : 0];
        return goalTeam.getGoalkeeper();
    }
}
