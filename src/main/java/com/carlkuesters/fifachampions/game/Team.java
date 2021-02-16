package com.carlkuesters.fifachampions.game;

import com.jme3.math.Vector2f;
import java.util.ArrayList;

public class Team {

    public Team(String name, Player[] players, Formation formation) {
        this.name = name;
        for (Player player : players) {
            this.players.add(new PlayerObject(this, player));
        }
        this.formation = formation;
    }
    private String name;
    private Game game;
    private int side;
    private ArrayList<PlayerObject> players = new ArrayList<>();
    private Formation formation;

    public String getName() {
        return name;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public int getSide() {
        return side;
    }

    public ArrayList<PlayerObject> getPlayers() {
        return players;
    }

    public Formation getFormation() {
        return formation;
    }

    public PlayerObject getGoalkeeper() {
        return players.stream()
                .filter(playerObject -> playerObject.getPlayer() instanceof Goalkeeper)
                .findAny().get();
    }

    public Vector2f getIdealLocation(PlayerObject playerObject) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) == playerObject) {
                Vector2f formationLocation = getIdealLocation_FormationOnly(i);
                Vector2f idealLocation = new Vector2f(formationLocation);
                if (!(playerObject.getPlayer() instanceof Goalkeeper)) {

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
}
