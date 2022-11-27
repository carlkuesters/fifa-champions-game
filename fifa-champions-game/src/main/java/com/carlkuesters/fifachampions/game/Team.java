package com.carlkuesters.fifachampions.game;

import com.jme3.math.Vector2f;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

public class Team {

    public Team(TeamInfo teamInfo, String trikotName, Player[] players, Player[] reservePlayers, Formation formation) {
        this.teamInfo = teamInfo;
        this.trikotName = trikotName;
        this.players = new PlayerObject[players.length];
        for (int i = 0; i < players.length; i++) {
            this.players[i]  = new PlayerObject(this, players[i]);
        }
        this.reservePlayers = new PlayerObject[reservePlayers.length];
        for (int i = 0; i < reservePlayers.length; i++) {
            this.reservePlayers[i]  = new PlayerObject(this, reservePlayers[i]);
        }
        this.formation = formation;
        playerSwitches = new LinkedList<>();
    }
    @Getter
    private TeamInfo teamInfo;
    @Getter
    private String trikotName;
    private Game game;
    private int side;
    @Getter
    private PlayerObject[] players;
    @Getter
    private PlayerObject[] reservePlayers;
    @Setter
    private Formation formation;
    @Getter
    private LinkedList<PlayerSwitch> playerSwitches;

    public void setGame(Game game) {
        this.game = game;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public int getSide() {
        return side;
    }

    public Formation getFormation() {
        return formation;
    }

    public PlayerObject getGoalkeeper() {
        return players[0];
    }

    public Vector2f getIdealLocation(PlayerObject playerObject) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == playerObject) {
                Vector2f formationLocation = getIdealLocation_FormationOnly(i);
                Vector2f idealLocation = new Vector2f(formationLocation);
                if (!playerObject.isGoalkeeper()) {

                    boolean isBallOwned = (game.getBall().getOwner() != null);
                    boolean isBallAllied = (isBallOwned && (game.getBall().getOwner().getTeam() == this));
                    Vector2f ballLocation = MathUtil.convertTo2D_XZ(game.getBall().getPosition());

                    float maximumScore = Float.NEGATIVE_INFINITY;
                    float tileSize = 2;
                    Vector2f tmpLocation = new Vector2f();
                    for (float x = (-1 * Game.FIELD_HALF_WIDTH); x <= Game.FIELD_HALF_WIDTH; x += tileSize) {
                        for (float y = (-1 * Game.FIELD_HALF_HEIGHT); y <= Game.FIELD_HALF_HEIGHT; y += tileSize) {
                            tmpLocation.set(x, y);

                            float score = 0;

                            float weightedDistanceToFormation = 1 * tmpLocation.distance(formationLocation);
                            if (isBallAllied) {
                                float weightedDistanceToOpponent = 1 * game.getDistanceToNearestOpponent(playerObject.getTeam(), MathUtil.convertTo3D_XZ(tmpLocation));
                                score -= Math.max(weightedDistanceToFormation, weightedDistanceToOpponent);
                            } else {
                                float weightedDistanceToBall = 1 * tmpLocation.distance(ballLocation);
                                score -= Math.max(weightedDistanceToFormation, weightedDistanceToBall);
                            }

                            if (score > maximumScore) {
                                idealLocation.set(tmpLocation);
                                maximumScore = score;
                            }
                        }
                    }

                }

                return idealLocation;
            }
        }
        throw new IllegalArgumentException();
    }

    private Vector2f getIdealLocation_FormationOnly(int playerIndex) {
        Vector2f formationLocation = formation.getLocation(playerIndex).mult(game.getHalfTimeSideFactor() * side);
        return new Vector2f(Game.FIELD_HALF_WIDTH * formationLocation.getX(), Game.FIELD_HALF_HEIGHT * formationLocation.getY());
    }

    public void addPlayerSwitch(PlayerSwitch playerSwitch) {
        playerSwitch.getFieldPlayer().setMarkedForSwitch(true);
        playerSwitch.getReservePlayer().setMarkedForSwitch(true);
        playerSwitches.add(playerSwitch);
    }

    public void removePlayerSwitch(PlayerSwitch playerSwitch) {
        playerSwitches.remove(playerSwitch);
        playerSwitch.getFieldPlayer().setMarkedForSwitch(false);
        playerSwitch.getReservePlayer().setMarkedForSwitch(false);
    }
}
