package com.carlkuesters.fifachampions.game;

import com.carlkuesters.fifachampions.game.cinematics.Cinematic;
import com.carlkuesters.fifachampions.game.cinematics.CinematicInfo;
import com.carlkuesters.fifachampions.game.cinematics.cinematics.*;
import com.carlkuesters.fifachampions.game.cooldowns.UnownedBallPickupCooldown;
import com.carlkuesters.fifachampions.game.cooldowns.FightCooldown;
import com.carlkuesters.fifachampions.game.situations.*;
import com.carlkuesters.fifachampions.shared.ArrayUtil;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import com.carlkuesters.fifachampions.game.cooldowns.SwitchToPlayerCooldown;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class Game implements GameLoopListener {

    public Game(Collection<Controller> controllers, GameCreationInfo gameCreationInfo, Function<CinematicInfo<?>, Cinematic> createCinematic) {
        halfTimeDuration = gameCreationInfo.getHalftimeDuration();
        ball.setGame(this);
        this.createCinematic = createCinematic;
        // Teams
        teams = new Team[gameCreationInfo.getTeams().length];
        for (int i = 0; i < teams.length; i++) {
            InitialTeamInfo initialTeamInfo = gameCreationInfo.getTeams()[i];
            String trikotName = initialTeamInfo.getTeamInfo().getTrikotNames()[initialTeamInfo.getTrikotIndex()];
            teams[i] = new Team(initialTeamInfo.getTeamInfo(), trikotName, initialTeamInfo.getFieldPlayers(), initialTeamInfo.getReservePlayers(), initialTeamInfo.getFormation());
        }
        for (Team team : teams) {
            team.setGame(this);
            for (PlayerObject playerObject : team.getPlayers()) {
                playerObject.setGame(this);
            }
            for (PlayerObject playerObject : team.getReservePlayers()) {
                playerObject.setGame(this);
            }
        }
        teams[0].setSide(1);
        teams[1].setSide(-1);
        // Controllers (after everything else has been initialized so initial players can be properly selected)
        this.controllers = controllers;
        for (Controller controller : controllers) {
            controller.resetForNewGame(this);
        }
    }
    public static final float FIELD_HALF_WIDTH = 52.5f;
    public static final float FIELD_HALF_HEIGHT = 33.2f;
    public static final float PENALTY_AREA_WIDTH = 16;
    public static final float PENALTY_AREA_HEIGHT = 39.2f;
    public static final float GOAL_WIDTH = 1.9f;
    public static final float GOAL_HEIGHT = 3.32f;
    public static final float GOAL_DEPTH = 8.6f;
    public static final float MAXIMUM_NEAR_FREE_KICK_DISTANCE = 30;
    private static final Vector3f CORNER_KICK_BOTTOM_LEFT = new Vector3f(-1 * FIELD_HALF_WIDTH, 0, -1 * FIELD_HALF_HEIGHT);
    private static final Vector3f CORNER_KICK_TOP_LEFT = new Vector3f(-1 * FIELD_HALF_WIDTH, 0, FIELD_HALF_HEIGHT);
    private static final Vector3f CORNER_KICK_BOTTOM_RIGHT = new Vector3f(FIELD_HALF_WIDTH, 0, -1 * FIELD_HALF_HEIGHT);
    private static final Vector3f CORNER_KICK_TOP_RIGHT = new Vector3f(FIELD_HALF_WIDTH, 0, FIELD_HALF_HEIGHT);
    private boolean isGameOver;
    private boolean isTimeRunning;
    private float logicTime = 0;
    private int halfTime = 0;
    @Getter
    private float halfTimeDuration;
    private float halfTimePassedTime = 0;
    private float halfTimePassedOverTime;
    private float nextOverTimeDuration = 0;
    private Collection<Controller> controllers;
    private Team[] teams;
    private Ball ball = new Ball();
    private Vector3f lastBallPosition = new Vector3f();
    private boolean didBallEnterFieldAfterSituation;
    private CooldownManager cooldownManager = new CooldownManager();
    private NextSituation nextSituation;
    private Situation situation;
    private int[] goals = new int[2];
    @Getter
    private CameraPerspective cameraPerspective;
    private float cameraPerspectiveRemainingNonSituationDuration;
    private Function<CinematicInfo<?>, Cinematic> createCinematic;
    private LinkedList<Cinematic> cinematics = new LinkedList<>();
    @Getter
    private boolean audienceHyped;
    private LinkedList<EnqueuedAction> enqueuedActions = new LinkedList<>();
    @Setter
    private boolean replayRecordingEnabled;
    @Getter
    private float replayTime = 0;

    @Override
    public void update(float tpf) {
        if (logicTime == 0) {
            playCinematic(new GameIntroCinematicInfo());
            setSituation(createKickOffSituation(teams[0]));
        }

        logicTime += tpf;
        if (isTimeRunning) {
            if (halfTimePassedTime < halfTimeDuration) {
                halfTimePassedTime += tpf;
                if (halfTimePassedTime > halfTimeDuration) {
                    halfTimePassedOverTime = (halfTimePassedTime - halfTimeDuration);
                    halfTimePassedTime = halfTimeDuration;
                }
            } else {
                halfTimePassedOverTime += tpf;
                if (halfTimePassedOverTime > nextOverTimeDuration) {
                    Situation halfTimeEndSituation = ((halfTime == 0) ? new HalftimeSituation() : new GameOverSituation());
                    setNextSituation(new NextSituation(halfTimeEndSituation, 4, false));
                }
            }
            ball.getLastTouchedOwner().getTeam().getStatistics().addTimeWithBall(tpf);
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
        } else {
            situation.update(tpf);
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
                    LinkedList<PlayerObject> playersNearStraddler = getNearPlayers(straddler.getPosition(), 1, 0.5f);
                    for (PlayerObject playerNearStraddler : playersNearStraddler) {
                        if (playerNearStraddler != straddler) {
                            if (!playerNearStraddler.isFalling()) {
                                playerNearStraddler.collapse();
                                if (playerNearStraddler.getTeam() != straddler.getTeam()) {
                                    float foulChance = PlayerSkillUtil.getValue(0.9f, 0.5f, straddler.getPlayer().getFieldPlayerSkills().getFootDuel());
                                    if (Math.random() < foulChance) {
                                        // TESTING: Turn around foul
                                        PlayerObject tmp = straddler;
                                        straddler = playerNearStraddler;
                                        playerNearStraddler = tmp;

                                        Vector3f foulPosition = playerNearStraddler.getPosition();
                                        Team penaltyAreaTeam = getPenaltyAreaTeam(foulPosition);
                                        if (penaltyAreaTeam == straddler.getTeam()) {
                                            setNextSituation(new NextSituation(new PenaltySituation(playerNearStraddler.getTeam()), 2, true));
                                        } else {
                                            if (getDistanceToGoalLine(foulPosition, straddler.getTeam()) <= MAXIMUM_NEAR_FREE_KICK_DISTANCE) {
                                                setNextSituation(new NextSituation(new NearFreeKickSituation(playerNearStraddler, foulPosition), 2, true));
                                            } else {
                                                setNextSituation(new NextSituation(new FarFreeKickSituation(playerNearStraddler, foulPosition), 2, true));
                                            }
                                        }

                                        straddler.getTeam().getStatistics().addFoul();
                                        audienceHyped = true;
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
        } else if (situation != null) {
            if (cinematics.size() > 0) {
                Cinematic cinematic = cinematics.getFirst();
                if (cinematic.isFinished()) {
                    finishActiveCinematic();
                }
            }
        } else {
            cooldownManager.update(tpf);

            OutSide outSide = getOutside(ball.getPosition());
            if (outSide == null) {
                // Fight around ball
                LinkedList<PlayerObject> playersNearBall = getNearPlayers(ball.getPosition(), 1, 2);
                Collections.shuffle(playersNearBall);
                for (PlayerObject playerNearBall : playersNearBall) {
                    if (playerNearBall != ball.getOwner()) {
                        if (ball.getOwner() == null) {
                            if (cooldownManager.isNotOnCooldown(new UnownedBallPickupCooldown(playerNearBall))) {
                                ball.setOwner(playerNearBall, true);
                                cameraPerspective = null;
                            }
                        } else {
                            FightCooldown fightCooldown = new FightCooldown(ball.getOwner(), playerNearBall);
                            if (cooldownManager.isNotOnCooldown(fightCooldown)) {
                                boolean isBallOwnerWinning = PlayerSkillUtil.isWinning(
                                    ball.getOwner().getPlayer(),
                                    playerNearBall.getPlayer(),
                                    player -> player.getFieldPlayerSkills().getFootDuel()
                                );
                                PlayerObject ballWinner;
                                PlayerObject ballLoser;
                                if (isBallOwnerWinning) {
                                    ballWinner = ball.getOwner();
                                    ballLoser = playerNearBall;
                                } else {
                                    ballWinner = playerNearBall;
                                    ballLoser = ball.getOwner();
                                }
                                ball.setOwner(ballWinner, false);
                                cooldownManager.putOnCooldown(fightCooldown);
                                ballWinner.getTeam().getStatistics().addFight(true);
                                ballLoser.getTeam().getStatistics().addFight(false);
                            }
                        }
                    }
                }
                // Goalkeeper jump
                for (Team team : teams) {
                    PlayerObject goalkeeper = team.getGoalkeeper();
                    if (!goalkeeper.isGoalkeeperJumping()) {
                        PhysicsPrecomputationResult ballInGoalResult = precomputeBallTransformUntilInsideGoal(team);
                        if (ballInGoalResult != null) {
                            float reactionTime = PlayerSkillUtil.getValue(1, 0.2f, goalkeeper.getPlayer().getGoalkeeperSkills().getReflexes());
                            if (ballInGoalResult.getPassedTime() < 1) {
                                Vector3f goalLinePosition = ballInGoalResult.getPosition().clone();
                                goalLinePosition.setX(-1 * getHalfTimeSideFactor() * team.getSide() * FIELD_HALF_WIDTH);
                                float maximumWrongPositionPrediction = PlayerSkillUtil.getValue(2, 1, goalkeeper.getPlayer().getGoalkeeperSkills().getAgility());
                                float wrongPredictionY = (FastMath.nextRandomFloat() * maximumWrongPositionPrediction);
                                float wrongPredictionZ = (FastMath.nextRandomFloat() * maximumWrongPositionPrediction);
                                goalLinePosition.addLocal(0, wrongPredictionY, wrongPredictionZ);
                                GoalkeeperJump goalkeeperJump = goalkeeper.getGoalkeeperJump(goalLinePosition, ballInGoalResult.getPassedTime());

                                float maximumJumpStrength = PlayerSkillUtil.getValue(5, 20, goalkeeper.getPlayer().getGoalkeeperSkills().getJumpStrength());
                                // Last effort jump at maximum jump strength for visual effect
                                if (ballInGoalResult.getPassedTime() < reactionTime) {
                                    goalkeeperJump.getInitialVelocity().normalizeLocal().multLocal(maximumJumpStrength);
                                    goalkeeper.executeGoalkeeperJump(goalkeeperJump);
                                } else if (goalkeeperJump.getInitialVelocity().lengthSquared() <= (maximumJumpStrength * maximumJumpStrength)) {
                                    goalkeeper.executeGoalkeeperJump(goalkeeperJump);
                                }
                            }
                        }
                    }
                }
            } else if (isBallEligibleToGoingOutside(outSide)) {
                Team goalOutsideTeam = getGoalOutsideTeam(outSide);
                if (isInsideGoal(ball.getPosition())) {
                    int scoringTeamIndex = ((goalOutsideTeam == teams[0]) ? 1 : 0);
                    goals[scoringTeamIndex]++;
                    nextOverTimeDuration += 1;

                    // Testing: Our team always has kick off after goal
                    goalOutsideTeam = teams[0];

                    setNextSituation(new NextSituation(createKickOffSituation(goalOutsideTeam), 4, true));
                    playCinematic(new GoalCinematicInfo());
                    audienceHyped = true;
                } else {
                    if (goalOutsideTeam != null) {
                        if (ball.getLastTouchedOwner().getTeam() == goalOutsideTeam) {
                            Team cornerKickTeam = ((goalOutsideTeam == teams[0]) ? teams[1] : teams[0]);

                            // Testing: Our team always has corner kick
                            cornerKickTeam = teams[0];

                            Vector3f cornerKickPosition = getCornerKickPosition(ball.getPosition());
                            setNextSituation(new NextSituation(new CornerKickSituation(cornerKickTeam, cornerKickPosition), 2, true));
                            cornerKickTeam.getStatistics().addCorner();
                        } else {
                            // Testing: Our team always has goal kick
                            goalOutsideTeam = teams[0];

                            float horizontalPosition = Math.signum(ball.getPosition().getZ());
                            setNextSituation(new NextSituation(new GoalKickSituation(goalOutsideTeam, horizontalPosition), 2, true));
                        }
                    } else {
                        Team throwInTeam = ((ball.getLastTouchedOwner().getTeam() == teams[0]) ? teams[1] : teams[0]);

                        // Testing: Our team always has throw in
                        throwInTeam = teams[0];

                        // TODO: Properly choosing a starting player (based on position?)
                        PlayerObject throwInPlayer = throwInTeam.getPlayers()[throwInTeam.getPlayers().length - 1];
                        Vector3f throwInPosition = lastBallPosition.clone().setY(0).setZ(Math.signum(lastBallPosition.getZ()) * FIELD_HALF_HEIGHT);
                        // Go a bit out of field to throw in
                        throwInPosition.addLocal(0, 0, Math.signum(throwInPosition.getZ()));
                        setNextSituation(new NextSituation(new ThrowInSituation(throwInPlayer, throwInPosition), 2, true));
                    }
                }
            }
            didBallEnterFieldAfterSituation = (outSide == null);
        }

        if ((situation == null) && (cameraPerspective != null) && (cameraPerspectiveRemainingNonSituationDuration > 0)) {
            cameraPerspectiveRemainingNonSituationDuration -= tpf;
            if (cameraPerspectiveRemainingNonSituationDuration <= 0) {
                cameraPerspectiveRemainingNonSituationDuration = 0;
                cameraPerspective = null;
            }
        }

        checkEnqueuedActions(tpf);

        if (shouldRecordReplayFrames()) {
            replayTime += tpf;
        }
    }

    private void updatePlayerObject(PlayerObject playerObject, float tpf) {
        Controller controller = playerObject.getController();
        if ((controller != null) && (getActiveCinematic() == null)) {
            boolean canSteer = true;
            if (situation instanceof GoalKickSituation goalKickSituation) {
                if (playerObject == goalKickSituation.getStartingPlayer()) {
                    goalKickSituation.setTargetAngleDirection(-1 * controller.getTargetDirection().getX());
                    canSteer = false;
                }
            } else if (situation instanceof NearFreeKickSituation nearFreeKickSituation) {
                if (playerObject == nearFreeKickSituation.getStartingPlayer()) {
                    nearFreeKickSituation.setTargetCursorDirection(controller.getTargetDirection());
                    canSteer = false;
                }
            } else  if (situation instanceof PenaltySituation penaltySituation) {
                float targetDirection = Math.signum(controller.getTargetDirection().getX());
                if (playerObject == penaltySituation.getStartingPlayer()) {
                    penaltySituation.setTargetShootDirection(targetDirection);
                } else if (playerObject == penaltySituation.getGoalkeeper()) {
                    penaltySituation.setTargetGoalkeeperDirection(targetDirection);
                }
                canSteer = false;
            }
            if (canSteer && !controller.getButtons().isChargingBallButton()) {
                playerObject.setTargetWalkDirection(controller.getTargetDirection());
            }
        } else if (!playerObject.isGoalkeeperJumping()) {
            Vector2f idealLocation;
            if (situation instanceof BallSituation ballSituation) {
                idealLocation = MathUtil.convertTo2D_XZ(ballSituation.getPlayerPosition(playerObject));
            } else {
                idealLocation = playerObject.getTeam().getIdealLocation(playerObject);
            }
            playerObject.setTargetLocation(idealLocation);
        }
        playerObject.update(tpf);
    }

    private KickOffSituation createKickOffSituation(Team startingTeam) {
        PlayerObject startingPlayer = startingTeam.getPlayers()[startingTeam.getPlayers().length - 1];
        return new KickOffSituation(startingPlayer);
    }

    private boolean isBallEligibleToGoingOutside(OutSide outSide) {
        if (didBallEnterFieldAfterSituation) {
            return true;
        }
        int axis = (((outSide == OutSide.LEFT) || (outSide == OutSide.RIGHT)) ? 0 : 2);
        // Ball is moving away from center on said axis (e.g. throw-in from outside field away from the field)
        return (Math.signum(ball.getPosition().get(axis)) == Math.signum(ball.getVelocity().get(axis)));
    }

    public void onOffside(OffsidePlayer offsidePlayer, Vector3f lastBallTouchPosition, float lastBallTouchReplayTime) {
        Team offsidePlayerTeam = offsidePlayer.getPlayerObject().getTeam();
        Team freeKickTeam = ((offsidePlayerTeam == teams[0]) ? teams[1] : teams[0]);
        // TODO: Properly choosing a starting player (based on position?)
        PlayerObject startingPlayer = freeKickTeam.getPlayers()[freeKickTeam.getPlayers().length - 1];
        setNextSituation(new NextSituation(new FarFreeKickSituation(startingPlayer, lastBallTouchPosition), 2, false));
        playCinematic(new OffsideCinematicInfo(new OffsideCinematicInfoData(offsidePlayer.getOffsidePosition().getX(), offsidePlayerTeam.getSide(), lastBallTouchReplayTime)));
        offsidePlayerTeam.getStatistics().addOffside();
    }

    public PhysicsPrecomputationResult precomputeBallTransformUntilInsideGoal(Team goalTeam) {
        if (((halfTime == 0) && (goalTeam == teams[0]))
         || ((halfTime == 1) && (goalTeam == teams[1]))) {
            return ball.precomputeTransformUntil(result -> isInsideGoalLeft(result.getPosition()));
        } else {
            return ball.precomputeTransformUntil(result -> isInsideGoalRight(result.getPosition()));
        }
    }

    public void startSecondHalftime() {
        halfTime = 1;
        halfTimePassedTime = 0;
        halfTimePassedOverTime = 0;
        // Testing: Our team also has the second kick off
        setSituation(createKickOffSituation(teams[0]));
    }

    private void setNextSituation(NextSituation nextSituation) {
        this.nextSituation = nextSituation;
        if (nextSituation.isBallUnowned()) {
            ball.setOwner(null, false);
        }
        isTimeRunning = false;
    }

    private void setSituation(Situation situation) {
        situation.setGame(this);
        this.situation = situation;
        cameraPerspective = null;
        audienceHyped = false;
        if (cinematics.isEmpty()) {
            startSituation();
        }
    }

    private void startSituation() {
        applyPlayerSwitches();
        cooldownManager.clear();
        situation.start();
        for (Controller controller : controllers) {
            controller.resetPlayer();
        }
        situation.customizePlayerSelection();
    }

    private void applyPlayerSwitches() {
        for (Team team : teams) {
            LinkedList<PlayerSwitch> playerSwitches = team.getPlayerSwitches();
            while (playerSwitches.size() > 0) {
                PlayerSwitch playerSwitch = playerSwitches.get(0);
                PlayerObject fieldPlayer = playerSwitch.getFieldPlayer();
                PlayerObject reservePlayer = playerSwitch.getReservePlayer();
                reservePlayer.setPosition(fieldPlayer.getPosition());
                reservePlayer.setRotation(fieldPlayer.getRotation());
                ArrayUtil.swap(team.getPlayers(), fieldPlayer, team.getReservePlayers(), reservePlayer);
                team.removePlayerSwitch(playerSwitch);
            }
        }
    }

    public void continueFromSituation() {
        if (situation != null) {
            setPlayersCanMove(true);
            situation = null;
            isTimeRunning = true;
        }
    }

    public void setPlayersCanMove(boolean canMove) {
        for (Team team : teams) {
            for (PlayerObject playerObject : team.getPlayers()) {
                playerObject.setCanMove(canMove);
            }
        }
    }

    public void selectPlayer(PlayerObject playerObject) {
        for (Controller controller : controllers) {
            if (controller.getTeam() == playerObject.getTeam()) {
                controller.setPlayer(playerObject);
                break;
            }
        }
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

    public PlayerObject getNearestOpponent(Team alliedTeam, Vector3f position) {
        float minimumDistance = Float.MAX_VALUE;
        PlayerObject nearestOpponent = null;
        for (Team team : teams) {
            for (PlayerObject playerObject2 : team.getPlayers()) {
                if (playerObject2.getTeam() != alliedTeam) {
                    float distance = playerObject2.getPosition().distance(position);
                    if (distance < minimumDistance) {
                        minimumDistance = distance;
                        nearestOpponent = playerObject2;
                    }
                }
            }
        }
        return nearestOpponent;
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

    public void switchToNextBallNearestSwitchablePlayer(Controller controller) {
        PlayerObject newPlayer = switchToBallNearestSwitchablePlayer(controller, playerObject -> !playerObject.isGoalkeeper() && cooldownManager.isNotOnCooldown(new SwitchToPlayerCooldown(playerObject)));
        if (newPlayer != null) {
            cooldownManager.putOnCooldown(new SwitchToPlayerCooldown(newPlayer));
        }
    }

    public void switchToBallNearestPlayer(Controller controller) {
        switchToBallNearestSwitchablePlayer(controller, _ -> true);
    }

    private PlayerObject switchToBallNearestSwitchablePlayer(Controller controller, Predicate<PlayerObject> isSwitchable) {
        PlayerObject nearestSwitchablePlayer = null;
        float minimumDistanceToBallSquared = Float.MAX_VALUE;
        for (PlayerObject playerObject : controller.getTeam().getPlayers()) {
            if ((playerObject.getController() == null) && isSwitchable.test(playerObject)) {
                float distanceToBallSquared = playerObject.getPosition().distanceSquared(ball.getPosition());
                if (distanceToBallSquared < minimumDistanceToBallSquared) {
                    nearestSwitchablePlayer = playerObject;
                    minimumDistanceToBallSquared = distanceToBallSquared;
                }
            }
        }
        if (nearestSwitchablePlayer != null) {
            controller.setPlayer(nearestSwitchablePlayer);
        }
        return nearestSwitchablePlayer;
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
            && (position.getZ() > (GOAL_DEPTH / -2))
            && (position.getZ() < (GOAL_DEPTH / 2));
    }

    public float getDistanceToGoalLine(Vector3f position, Team goalTeam) {
        return FastMath.abs(position.getX() - (-1 * getHalfTimeSideFactor() * goalTeam.getSide() * FIELD_HALF_WIDTH));
    }

    public void enqueue(EnqueuedAction enqueuedAction) {
        enqueuedActions.add(enqueuedAction);
    }

    private void checkEnqueuedActions(float tpf) {
        if (enqueuedActions.size() > 0) {
            LinkedList<EnqueuedAction> currentEnqueuedActions = new LinkedList<>(enqueuedActions);
            for (EnqueuedAction enqueuedAction : currentEnqueuedActions) {
                float remainingDelay = (enqueuedAction.getRemainingDelay() - tpf);
                if (remainingDelay > 0) {
                    enqueuedAction.setRemainingDelay(remainingDelay);
                } else {
                    enqueuedAction.getRunnable().run();
                    enqueuedActions.remove(enqueuedAction);
                }
            }
        }
    }

    public void onGameOver() {
        isGameOver = true;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public float getLogicTime() {
        return logicTime;
    }

    public float getPassedTime() {
        float passedTime = halfTimePassedTime;
        if (halfTime == 1) {
            passedTime += halfTimeDuration;
        }
        return passedTime;
    }

    public float getHalfTimePassedOverTime() {
        return halfTimePassedOverTime;
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

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public Situation getSituation() {
        return situation;
    }

    public NextSituation getNextSituation() {
        return nextSituation;
    }

    public int[] getGoals() {
        return goals;
    }

    public void setCameraPerspective(CameraPerspective cameraPerspective, float maximumNonSituationDuration) {
        this.cameraPerspective = cameraPerspective;
        this.cameraPerspectiveRemainingNonSituationDuration = maximumNonSituationDuration;
    }

    public void playCinematic(CinematicInfo<?> cinematicInfo) {
        Cinematic cinematic = createCinematic.apply(cinematicInfo);
        cinematics.add(0, cinematic);
    }

    public void finishActiveCinematic() {
        cinematics.removeFirst();
        if (cinematics.isEmpty()) {
            startSituation();
        }
    }

    public Cinematic getActiveCinematic() {
        return (((situation != null) && (cinematics.size() > 0)) ? cinematics.getFirst() : null);
    }

    public boolean shouldRecordReplayFrames() {
        return (!isGameOver && replayRecordingEnabled && (getActiveCinematic() == null));
    }
}
