package com.carlkuesters.fifachampions.game;

import com.carlkuesters.fifachampions.game.cooldowns.UnownedBallPickupCooldown;
import com.carlkuesters.fifachampions.game.cooldowns.FightCooldown;
import com.carlkuesters.fifachampions.game.situations.*;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import com.carlkuesters.fifachampions.game.cooldowns.SwitchToPlayerCooldown;

import java.util.Collections;
import java.util.LinkedList;

public class Game implements GameLoopListener {

    public Game(Team[] teams) {
        for (Team team : teams) {
            team.setGame(this);
            for (PlayerObject playerObject : team.getPlayers()) {
                playerObject.setGame(this);
            }
        }
        ball.setGame(this);
        teams[0].setSide(1);
        teams[1].setSide(-1);
        this.teams = teams;
    }
    public static final float HALFTIME_DURATION = 45;
    public static final float FIELD_HALF_WIDTH = 52.5f;
    public static final float FIELD_HALF_HEIGHT = 33.2f;
    public static final float PENALTY_AREA_WIDTH = 16;
    public static final float PENALTY_AREA_HEIGHT = 39.2f;
    public static final float GOAL_WIDTH = 1.9f;
    public static final float GOAL_HEIGHT = 3.05f;
    public static final float GOAL_Z_BOTTOM = -4.5f;
    public static final float GOAL_Z_TOP = 3.9f;
    private static final Vector3f CORNER_KICK_BOTTOM_LEFT = new Vector3f(-1 * FIELD_HALF_WIDTH, 0, -1 * FIELD_HALF_HEIGHT);
    private static final Vector3f CORNER_KICK_TOP_LEFT = new Vector3f(-1 * FIELD_HALF_WIDTH, 0, FIELD_HALF_HEIGHT);
    private static final Vector3f CORNER_KICK_BOTTOM_RIGHT = new Vector3f(FIELD_HALF_WIDTH, 0, -1 * FIELD_HALF_HEIGHT);
    private static final Vector3f CORNER_KICK_TOP_RIGHT = new Vector3f(FIELD_HALF_WIDTH, 0, FIELD_HALF_HEIGHT);
    private float logicTime = 0;
    private int halfTime = 0;
    private float nextOverTimeDuration = 2;
    private float gameTime = 0;
    private float gameOverTime;
    private Team[] teams;
    private Ball ball = new Ball();
    private Vector3f lastBallPosition = new Vector3f();
    private boolean didBallEnterFieldAfterSituation;
    private LinkedList<Controller> controllers = new LinkedList<>();
    private CooldownManager cooldownManager = new CooldownManager();
    private NextSituation nextSituation;
    private Situation situation;

    public void start() {
        setSituation(createKickOffSituation(teams[0]));
    }

    @Override
    public void update(float tpf) {
        logicTime += tpf;
        if (gameTime < HALFTIME_DURATION) {
            gameTime += tpf;
            if (gameTime > HALFTIME_DURATION) {
                gameOverTime = (gameTime - HALFTIME_DURATION);
                gameTime = HALFTIME_DURATION;
            }
        } else if (halfTime < 1) {
            gameOverTime += tpf;
            if (gameOverTime > nextOverTimeDuration) {
                halfTime++;
                if (halfTime < 2) {
                    Team kickOffTeam = teams[halfTime];

                    // Testing: Our team always has kick off
                    kickOffTeam = teams[0];

                    setNextSituation(new NextSituation(createKickOffSituation(kickOffTeam), 4, false));
                } else {
                    // TODO: End game
                }
            }
        }

        for (Controller controller : controllers) {
            controller.update(tpf);
        }

        PlayerObject ballOwner = ball.getOwner();
        if (ballOwner != null) {
            updatePlayerObject(ballOwner, tpf);
        }
        if (situation == null) {
            lastBallPosition.set(ball.getPosition());
            ball.update(tpf);
        }
        for (Team team : teams) {
            for (PlayerObject playerObject : team.getPlayers()) {
                if (playerObject != ballOwner) {
                    updatePlayerObject(playerObject, tpf);
                }
            }
        }

        // Straddling
        for (Team team : teams) {
            for (PlayerObject straddler : team.getPlayers()) {
                if (straddler.isStraddling()) {
                    LinkedList<PlayerObject> playersNearStraddler = getNearPlayers(straddler.getPosition(), 1, 1);
                    for (PlayerObject playerNearStraddler : playersNearStraddler) {
                        if (playerNearStraddler != straddler) {
                            if (!playerNearStraddler.isFalling()) {
                                playerNearStraddler.collapse();
                                if (playerNearStraddler.getTeam() != straddler.getTeam()) {
                                    boolean isFoul = (Math.random() < 0.5);
                                    if (isFoul) {
                                        // TESTING: Turn around foul
                                        PlayerObject tmp = straddler;
                                        straddler = playerNearStraddler;
                                        playerNearStraddler = tmp;

                                        Vector3f foulPosition = playerNearStraddler.getPosition();
                                        Team penaltyAreaTeam = getPenaltyAreaTeam(foulPosition);
                                        if (penaltyAreaTeam == straddler.getTeam()) {
                                            setNextSituation(new NextSituation(new PenaltySituation(playerNearStraddler.getTeam()), 2, true));
                                        } else {
                                            setNextSituation(new NextSituation(new FreeKickSituation(playerNearStraddler, foulPosition), 2, true));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (nextSituation != null) {
            nextSituation.update(tpf);
            if (nextSituation.hasStarted()) {
                setSituation(nextSituation.getSituation());
                nextSituation = null;
            }
        } else if (situation == null) {
            cooldownManager.update(tpf);

            OutSide outSide = getOutside(ball.getPosition());
            if (outSide == null) {
                // Fight around ball
                LinkedList<PlayerObject> playersNearBall = getNearPlayers(ball.getPosition(), 1, 2.5f);
                Collections.shuffle(playersNearBall);
                for (PlayerObject playerNearBall : playersNearBall) {
                    if (playerNearBall != ball.getOwner()) {
                        if (ball.getOwner() == null) {
                            if (cooldownManager.isNotOnCooldown(new UnownedBallPickupCooldown(playerNearBall))) {
                                ball.setOwner(playerNearBall, true);
                            }
                        } else {
                            FightCooldown fightCooldown = new FightCooldown(ball.getOwner(), playerNearBall);
                            if (cooldownManager.isNotOnCooldown(fightCooldown)) {
                                PlayerObject ballWinner = (Math.random() < 0.5 ? ball.getOwner() : playerNearBall);

                                // TESTING: Team #0 always wins
                                ballWinner = ((ball.getOwner().getTeam().getSide() == 1) ? ball.getOwner() : playerNearBall);

                                cooldownManager.putOnCooldown(fightCooldown);
                                ball.setOwner(ballWinner, false);
                            }
                        }
                    }
                }
                // Goalkeeper jump
                for (Team team : teams) {
                    PlayerObject goalkeeper = team.getGoalkeeper();
                    if (!goalkeeper.isGoalkeeperJumping()) {
                        PhysicsPrecomputationResult ballInGoalResult;
                        if (((halfTime == 0) && (team == teams[0]))
                         || ((halfTime == 1) && (team == teams[1]))) {
                            ballInGoalResult = ball.precomputeTransformUntil(result -> isInsideGoalLeft(result.getPosition()));
                        } else {
                            ballInGoalResult = ball.precomputeTransformUntil(result -> isInsideGoalRight(result.getPosition()));
                        }
                        if (ballInGoalResult != null) {
                            Vector3f goalLinePosition = ballInGoalResult.getPosition().clone();
                            goalLinePosition.setX(-1 * getHalfTimeSideFactor() * team.getSide() * FIELD_HALF_WIDTH);
                            goalkeeper.goalkeeperJump(goalLinePosition, ballInGoalResult.getPassedTime());
                        }
                    }
                }
            } else if (didBallEnterFieldAfterSituation) {
                Team goalOutsideTeam = getGoalOutsideTeam(outSide);
                if (isInsideGoal(ball.getPosition())) {
                    setNextSituation(new NextSituation(createKickOffSituation(goalOutsideTeam), 4, true));
                } else {
                    if (goalOutsideTeam != null) {
                        if (ball.getLastTouchedOwner().getTeam() == goalOutsideTeam) {
                            Team cornerKickTeam = ((goalOutsideTeam == teams[0]) ? teams[1] : teams[0]);

                            // Testing: Our team always has corner kick
                            cornerKickTeam = teams[0];

                            Vector3f cornerKickPosition = getCornerKickPosition(ball.getPosition());
                            setNextSituation(new NextSituation(new CornerKickSituation(cornerKickTeam, cornerKickPosition), 2, true));
                        } else {
                            setNextSituation(new NextSituation(new GoalKickSituation(goalOutsideTeam), 2, true));
                        }
                    } else {
                        Team throwInTeam = ((ball.getLastTouchedOwner().getTeam() == teams[0]) ? teams[1] : teams[0]);

                        // Testing: Our team always has throw in
                        throwInTeam = teams[0];

                        // TODO: Properly choosing a starting player (based on position?)
                        PlayerObject throwInPlayer = throwInTeam.getPlayers().get(throwInTeam.getPlayers().size() - 1);
                        Vector3f throwInPosition = lastBallPosition.clone().setY(0);
                        // Go a bit out of field to throw in
                        throwInPosition.addLocal(0, 0, Math.signum(throwInPosition.getZ()));
                        setNextSituation(new NextSituation(new ThrowInSituation(throwInPlayer, throwInPosition), 2, true));
                    }
                }
            }
            didBallEnterFieldAfterSituation = (outSide == null);
        }
    }

    private void updatePlayerObject(PlayerObject playerObject, float tpf) {
        Controller controller = playerObject.getController();
        if (controller != null) {
            if (!controller.isChargingBallButton()) {
                playerObject.setTargetWalkDirection(controller.getTargetDirection());
            }
        } else if (!playerObject.isGoalkeeperJumping()) {
            Vector2f idealLocation;
            if (situation instanceof BallSituation) {
                BallSituation ballSituation = (BallSituation) situation;
                idealLocation = MathUtil.convertTo2D_XZ(ballSituation.getPlayerPosition(playerObject));
            } else {
                idealLocation = playerObject.getTeam().getIdealLocation(playerObject);
            }
            playerObject.setTargetLocation(idealLocation);
            playerObject.lookAt_XZ(ball.getPosition());
        }
        playerObject.update(tpf);
    }

    private KickOffSituation createKickOffSituation(Team startingTeam) {
        PlayerObject startingPlayer = startingTeam.getPlayers().get(startingTeam.getPlayers().size() - 1);        
        return new KickOffSituation(startingPlayer);
    }

    public void onOffside(PlayerObject offsidePlayerObject, Vector3f lastBallTouchPosition) {
        Team freeKickTeam = ((offsidePlayerObject.getTeam() == teams[0]) ? teams[1] : teams[0]);
        // TODO: Properly choosing a starting player (based on position?)
        PlayerObject startingPlayer = freeKickTeam.getPlayers().get(freeKickTeam.getPlayers().size() - 1);
        setNextSituation(new NextSituation(new FreeKickSituation(startingPlayer, lastBallTouchPosition), 2, false));
    }

    private void setNextSituation(NextSituation nextSituation) {
        this.nextSituation = nextSituation;
        if (nextSituation.isBallUnowned()) {
            ball.setOwner(null, false);
        }
    }

    private void setSituation(Situation situation) {
        situation.setGame(this);
        this.situation = situation;
        situation.start();
    }

    public void continueFromBallSituation() {
        if (situation instanceof BallSituation) {
            BallSituation ballSituation = (BallSituation) situation;
            ballSituation.getStartingPlayer().setCanMove(true);
            situation = null;
        }
    }

    public void selectPlayer(PlayerObject playerObject) {
        controllers.stream()
                .filter(controller -> controller.getTeam() == playerObject.getTeam())
                .findAny()
                .ifPresent(newController -> newController.setPlayer(playerObject));
    }

    private LinkedList<PlayerObject> getNearPlayers(Vector3f position, float maximumDistanceXZ, float maximumDistanceY) {
        TempVars tempVars = TempVars.get();
        float maximumDistanceSquaredXZ = (maximumDistanceXZ * maximumDistanceXZ);
        LinkedList<PlayerObject> nearPlayers = new LinkedList<>();
        
        tempVars.vect2d.set(position.getX(), position.getZ());
        for (Team team : teams) {
            for (PlayerObject playerObject : team.getPlayers()) {
                tempVars.vect2d2.set(playerObject.getPosition().getX(), playerObject.getPosition().getZ());
                if ((tempVars.vect2d.distanceSquared(tempVars.vect2d2) <= maximumDistanceSquaredXZ)
                 && (FastMath.abs(playerObject.getPosition().getY() - position.getY()) <= maximumDistanceY)) {
                    nearPlayers.add(playerObject);
                }
            }
        }

        tempVars.release();
        return nearPlayers;
    }

    public float getDistanceToNearestOpponent(Team alliedTeam, Vector3f position) {
        float minimumDistance = Float.MAX_VALUE;
        for (Team team : teams) {
            for (PlayerObject playerObject2 : team.getPlayers()) {
                if (playerObject2.getTeam() != alliedTeam) {
                    float distance = playerObject2.getPosition().distance(position);
                    if (distance < minimumDistance) {
                        minimumDistance = distance;
                    }
                }
            }
        }
        return minimumDistance;
    }

    public AnticipatedPlayer getTargetedPlayer(Team team, PlayerObject playerObject, float inRunFactor) {
        float minimumDistanceSquared = Float.MAX_VALUE;
        AnticipatedPlayer targetedPlayer = null;
        for (PlayerObject playerObject2 : team.getPlayers()) {
            if (playerObject2 != playerObject) {
                Vector3f anticipatedPositionOf2 = playerObject2.getPosition().add(playerObject2.getVelocity().mult(inRunFactor));
                if (FastMath.abs(getAngleToPlayer(playerObject, anticipatedPositionOf2)) < (FastMath.PI / 6)) {
                    float distanceSquared = playerObject.getPosition().distanceSquared(playerObject2.getPosition());
                    if (distanceSquared < minimumDistanceSquared) {
                        targetedPlayer = new AnticipatedPlayer(playerObject2, anticipatedPositionOf2);
                        minimumDistanceSquared = distanceSquared;
                    }
                }
            }
        }
        return targetedPlayer;
    }

    private float getAngleToPlayer(PlayerObject playerObject, Vector3f targetPosition) {
        Vector2f directionFromPlayer = MathUtil.convertTo2D_XZ(playerObject.getDirection()).normalizeLocal();
        Vector2f directionToTarget = MathUtil.convertTo2D_XZ(targetPosition.subtract(playerObject.getPosition())).normalizeLocal();
        return directionFromPlayer.angleBetween(directionToTarget);
    }

    public void switchToNearestSwitchablePlayer(Controller controller) {
        PlayerObject nearestSwitchablePlayer = null;
        float minimumDistanceToBallSquared = Float.MAX_VALUE;
        SwitchToPlayerCooldown switchToNearestPlayerCooldown = null;
        for (PlayerObject playerObject : controller.getTeam().getPlayers()) {
            if ((playerObject.getController() == null) && (!(playerObject.getPlayer() instanceof Goalkeeper))) {
                SwitchToPlayerCooldown switchToPlayerCooldown = new SwitchToPlayerCooldown(playerObject);
                if (cooldownManager.isNotOnCooldown(switchToPlayerCooldown)) {
                    float distanceToBallSquared = playerObject.getPosition().distanceSquared(ball.getPosition());
                    if (distanceToBallSquared < minimumDistanceToBallSquared) {
                        nearestSwitchablePlayer = playerObject;
                        minimumDistanceToBallSquared = distanceToBallSquared;
                        switchToNearestPlayerCooldown = switchToPlayerCooldown;
                    }
                }
            }
        }
        if (nearestSwitchablePlayer != null) {
            controller.setPlayer(nearestSwitchablePlayer);
            cooldownManager.putOnCooldown(switchToNearestPlayerCooldown);
        }
    }

    public static OutSide getOutside(Vector3f position) {
        if (position.getX() < -1 * FIELD_HALF_WIDTH) {
            return OutSide.LEFT;
        } else if (position.getX() > FIELD_HALF_WIDTH) {
            return OutSide.RIGHT;
        } else if (position.getZ() < -1 * FIELD_HALF_HEIGHT) {
            return OutSide.BOTTOM;
        } else if (position.getZ() > FIELD_HALF_HEIGHT) {
            return OutSide.TOP;
        }
        return null;
    }

    private Team getPenaltyAreaTeam(Vector3f position) {
        if (FastMath.abs(position.getX()) > (FIELD_HALF_WIDTH - PENALTY_AREA_WIDTH)
        && (FastMath.abs(position.getZ()) < PENALTY_AREA_HEIGHT)) {
            int teamIndex = ((position.getX() < 0) ? 0 : 1);
            return teams[teamIndex];
        }
        return null;
    }

    private Team getGoalOutsideTeam(OutSide outSide) {
        if (outSide == OutSide.LEFT) {
            return teams[halfTime];
        } else if (outSide == OutSide.RIGHT) {
            return teams[(halfTime + 1) % 2];
        }
        return null;
    }

    private static Vector3f getCornerKickPosition(Vector3f goalOutsidePosition) {
        boolean isRight = (goalOutsidePosition.getX() > 0);
        boolean isTop = (goalOutsidePosition.getZ() > 0);
        if (isRight) {
            return (isTop ? CORNER_KICK_TOP_RIGHT : CORNER_KICK_BOTTOM_RIGHT);
        } else {
            return (isTop ? CORNER_KICK_TOP_LEFT : CORNER_KICK_BOTTOM_LEFT);
        }
    }

    public static boolean isInsideGoal(Vector3f position) {
        return (FastMath.abs(position.getX()) > FIELD_HALF_WIDTH)
            && (FastMath.abs(position.getX()) < FIELD_HALF_WIDTH + GOAL_WIDTH)
            && isInsideGoal_YZ(position);
    }

    private static boolean isInsideGoalLeft(Vector3f position) {
        return (position.getX() < (-1 * FIELD_HALF_WIDTH))
            && (position.getX() > (-1 * (FIELD_HALF_WIDTH + GOAL_WIDTH)))
            && isInsideGoal_YZ(position);
    }

    private static boolean isInsideGoalRight(Vector3f position) {
        return (position.getX() > FIELD_HALF_WIDTH)
            && (position.getX() < FIELD_HALF_WIDTH + GOAL_WIDTH)
            && isInsideGoal_YZ(position);
    }

    private static boolean isInsideGoal_YZ(Vector3f position) {
        return (position.getY() < GOAL_HEIGHT)
            && (position.getZ() > GOAL_Z_BOTTOM)
            && (position.getZ() < GOAL_Z_TOP);
    }

    public float getLogicTime() {
        return logicTime;
    }

    public int getHalfTimeSideFactor() {
        return ((halfTime == 0) ? 1 : -1);
    }

    public Team[] getTeams() {
        return teams;
    }

    public Ball getBall() {
        return ball;
    }

    public void addController(Controller controller) {
        controllers.add(controller);
    }

    public LinkedList<Controller> getControllers() {
        return controllers;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public Situation getSituation() {
        return situation;
    }

    public NextSituation getNextSituation() {
        return nextSituation;
    }
}
