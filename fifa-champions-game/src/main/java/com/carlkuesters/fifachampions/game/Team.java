package com.carlkuesters.fifachampions.game;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Random;

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
                    Vector2f normalizedFormationLocation = formationLocation.divide(Game.FIELD_HALF_WIDTH);
                    Vector2f ballLocation = MathUtil.convertTo2D_XZ(game.getBall().getPosition());
                    Vector2f normalizedBallLocation = ballLocation.divide(Game.FIELD_HALF_WIDTH);
                    boolean isBallOwned = (game.getBall().getOwner() != null);
                    boolean isBallAllied = (isBallOwned && (game.getBall().getOwner().getTeam() == this));

                    // A bit of human error
                    Random random = new Random(playerObject.hashCode());
                    Vector2f randomError = new Vector2f(random.nextFloat(), random.nextFloat());
                    randomError.subtractLocal(0.5f, 0.5f).multLocal(2);
                    idealLocation.addLocal(randomError);

                    // Stand behind opponent to protect own goal
                    float xAddition = ((game.getHalfTimeSideFactor() * side) * -0.5f);
                    idealLocation.setX(idealLocation.getX() + xAddition);

                    // Make field more narrow towards the goal
                    float yFactor = Math.min(1.4f - FastMath.abs(normalizedFormationLocation.getX()), 1);
                    idealLocation.setY(idealLocation.getY() * yFactor);

                    // Move horizontally with the ball
                    Vector2f normalizedSourceToBall = normalizedBallLocation.subtract(normalizedFormationLocation);
                    float normalizedDeltaX = (0.05f * normalizedBallLocation.getX()) + (normalizedSourceToBall.getX() / 2.5f) + (Math.signum(normalizedBallLocation.getX()) * 0.1f);
                    float deltaX = (normalizedDeltaX * Game.FIELD_HALF_WIDTH);
                    idealLocation.setX(idealLocation.getX() + deltaX);

                    // Ball owned by ally: Move away from nearest opponent
                    // Ball owned by opponent: Move towards nearest opponent
                    Vector2f dangerPosition;
                    if (isBallAllied) {
                        PlayerObject nearestOpponent = game.getNearestOpponent(this, MathUtil.convertTo3D_XZ(idealLocation));
                        dangerPosition = MathUtil.convertTo2D_XZ(nearestOpponent.getPosition());
                    } else {
                        dangerPosition = ballLocation;
                    }
                    float dangerDistance = idealLocation.distance(dangerPosition);
                    float dangerRange = 20;
                    if (dangerDistance < dangerRange) {
                        float dangerRangeProgress = (1 - (dangerDistance / dangerRange));
                        float dangerFactor = Math.min(dangerRangeProgress, 0.7f);
                        Vector2f dangerCorrection = dangerPosition.subtract(idealLocation).multLocal(dangerFactor);
                        if (isBallAllied) {
                            dangerCorrection.multLocal(-0.2f);
                        }
                        idealLocation.addLocal(dangerCorrection);
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
