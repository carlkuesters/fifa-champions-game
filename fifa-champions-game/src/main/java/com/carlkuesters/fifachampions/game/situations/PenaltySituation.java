package com.carlkuesters.fifachampions.game.situations;

import com.jme3.math.FastMath;
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
    private Team team;
    private Vector3f ballPosition;
    @Getter
    @Setter
    private float targetShootDirection;
    @Getter
    @Setter
    private float targetGoalkeeperDirection;

    @Override
    public void start() {
        super.start();
        game.setCameraPerspective(getCameraPerspectiveTowardsEnemyGoal(3, 10, 0), 2);
    }

    // TODO: Properly choosing a starting player (based on position?)
    private static PlayerObject getStartingPlayer(Team team) {
        return team.getPlayers()[team.getPlayers().length - 1];
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
        // Move out of penalty area
        Vector3f position = super.getPlayerPosition(playerObject);
        if ((playerObject != getGoalkeeper()) && (FastMath.abs(getOpponentGoalX() - position.getX()) < Game.PENALTY_AREA_WIDTH)) {
            position.setX(getStartingPlayerXFactor() * (Game.FIELD_HALF_WIDTH - Game.PENALTY_AREA_WIDTH - 0.2f));
        }
        return position;
    }

    public PlayerObject getGoalkeeper() {
        Team goalTeam = game.getTeams()[(startingPlayer.getTeam() == game.getTeams()[0]) ? 1 : 0];
        return goalTeam.getGoalkeeper();
    }
}
