package com.carlkuesters.fifachampions.game.situations;

import com.jme3.math.Vector3f;
import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.Team;

public class PenaltySituation extends BallSituation {

    public PenaltySituation(Team team) {
        super(getStartingPlayer(team));
        this.team = team;
    }
    private Team team;
    private Vector3f ballPosition;
    private float targetShootDirection;
    private float targetGoalkeeperDirection;

    @Override
    public void start() {
        super.start();
        game.setCameraPerspective(getCameraPerspectiveTowardsEnemyGoal(3, 10, 0), 2);
    }

    // TODO: Properly choosing a starting player (based on position?)
    private static PlayerObject getStartingPlayer(Team team) {
        return team.getPlayers().get(team.getPlayers().size() - 1);
    }

    @Override
    public Vector3f getBallPosition() {
        ballPosition = new Vector3f(game.getHalfTimeSideFactor() * team.getSide() * (Game.FIELD_HALF_WIDTH - 11), 0, 0);
        return ballPosition;
    }

    @Override
    public Vector3f getPlayerPosition(PlayerObject playerObject) {
        if (playerObject == startingPlayer) {
            return getBallApproachPosition(getDirectionToOpponentGoal());
        }
        return super.getPlayerPosition(playerObject);
    }

    public PlayerObject getGoalkeeper() {
        Team goalTeam = game.getTeams()[(startingPlayer.getTeam() == game.getTeams()[0]) ? 1 : 0];
        return goalTeam.getGoalkeeper();
    }

    public void setTargetShootDirection(float targetShootDirection) {
        this.targetShootDirection = targetShootDirection;
    }

    public void setTargetGoalkeeperDirection(float targetGoalkeeperDirection) {
        this.targetGoalkeeperDirection = targetGoalkeeperDirection;
    }

    public float getTargetShootDirection() {
        return targetShootDirection;
    }

    public float getTargetGoalkeeperDirection() {
        return targetGoalkeeperDirection;
    }
}
