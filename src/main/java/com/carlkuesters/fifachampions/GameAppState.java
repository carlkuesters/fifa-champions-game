package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.game.*;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ChargedButtonBehaviour;
import com.carlkuesters.fifachampions.game.situations.NearFreeKickSituation;
import com.carlkuesters.fifachampions.joystick.GameJoystickSubListener;
import com.carlkuesters.fifachampions.menu.GameOverIngameMenuAppState;
import com.carlkuesters.fifachampions.menu.PauseIngameMenuAppState;
import com.carlkuesters.fifachampions.visuals.MaterialFactory;
import com.carlkuesters.fifachampions.visuals.PlayerVisual;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.Joystick;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class GameAppState extends BaseDisplayAppState {

    @Getter
    private Game game;

    private Node rootNode;
    private Node guiNode;
    private HashMap<PlayerObject, PlayerVisual> playerVisuals = new HashMap<>();
    private HashMap<Controller, Node> controllerVisuals = new HashMap<>();
    @Getter
    private HashMap<Integer, Controller> controllers;
    private Node ballGroundIndicator;
    private Node targetInGoalIndicator;
    private Vector3f targetCameraLocation = new Vector3f();
    private Spatial ballModel;
    private Label lblGoals;
    private Container playerContainer;
    private Label optimalStrengthIndicator;
    private ProgressBar pbrStrength;
    private float displayedStrength;
    private float remainingDisplayedStrengthDuration;
    private PlayerObject displayedStrengthPlayerObject;
    boolean isFirstFrame = true;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        rootNode = new Node();
        guiNode = new Node();

        GameCreationInfo gameCreationInfo = mainApplication.getGameCreationInfo();
        Team[] teams = new Team[gameCreationInfo.getTeams().length];
        for (int i = 0; i < teams.length; i++) {
            InitialTeamInfo initialTeamInfo = gameCreationInfo.getTeams()[i];
            String trikotName = initialTeamInfo.getTeamInfo().getTrikotNames()[initialTeamInfo.getTrikotIndex()];
            teams[i] = new Team(initialTeamInfo.getTeamInfo(), trikotName, initialTeamInfo.getFieldPlayers(), initialTeamInfo.getReservePlayers(), initialTeamInfo.getFormation());
        }
        game = new Game(teams, gameCreationInfo.getHalftimeDuration());
        controllers = new HashMap<>();
        for (Joystick joystick : mainApplication.getInputManager().getJoysticks()) {
            Controller controller = new Controller(game);
            Team controllerTeam = null;
            int teamSide = gameCreationInfo.getControllerTeamSides().get(joystick.getJoyId());
            if (teamSide == -1) {
                controllerTeam = game.getTeams()[0];
            } else if (teamSide == 1) {
                controllerTeam = game.getTeams()[1];
            }
            controller.setTeam(controllerTeam);
            game.addController(controller);
            controllers.put(joystick.getJoyId(), controller);
        }
        mainApplication.getJoystickListener().setGameSubListener(new GameJoystickSubListener(controllers, () -> {
            stateManager.getState(PauseIngameMenuAppState.class).setEnabled(true);
        }));

        game.start();

        for (Team team : game.getTeams()) {
            for (PlayerObject playerObject : team.getPlayers()) {
                createPlayerVisual(playerObject);
            }
            for (PlayerObject playerObject : team.getReservePlayers()) {
                createPlayerVisual(playerObject);
            }
        }

        ColorRGBA controllerColor = ColorRGBA.Blue;
        for (Controller controller : game.getControllers()) {
            Spatial controllerVisual = new Geometry("player", new Box(0.1f, 0.2f, 0.1f));
            Material materialController = new Material(mainApplication.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
            materialController.setBoolean("UseMaterialColors", true);
            materialController.setColor("Ambient", controllerColor);
            materialController.setColor("Diffuse", controllerColor);
            controllerVisual.setMaterial(materialController);
            controllerVisual.move(0, 2.3f, 0);
            Node node = new Node();
            node.attachChild(controllerVisual);
            rootNode.attachChild(node);
            controllerVisuals.put(controller, node);
            controllerColor = ColorRGBA.Red;
        }

        Node ballNode = new Node();
        ballModel = mainApplication.getAssetManager().loadModel("models/ball/ball.j3o");
        // Target circumference = 2 * (69cm / 2*Pi) = 21.9633821467
        ballModel.setLocalScale(0.001606f);
        ballModel.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        Material materialBall = new Material(mainApplication.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        materialBall.setTexture("DiffuseMap", MaterialFactory.loadTexture(mainApplication.getAssetManager(), "models/ball/resources/ball_D.png"));
        materialBall.setTexture("NormalMap", MaterialFactory.loadTexture(mainApplication.getAssetManager(), "models/ball/resources/ball_N.png"));
        materialBall.setTexture("SpecularMap", MaterialFactory.loadTexture(mainApplication.getAssetManager(), "models/ball/resources/ball_SP.png"));
        ballNode.setLocalTranslation(0, 0.432f, 0);
        ballNode.attachChild(ballModel);
        Geometry ballGeometry = (Geometry) ballNode.getChild("ball");
        ballGeometry.setMaterial(materialBall);
        rootNode.attachChild(ballNode);
        float groundHeight = (ballNode.getLocalTranslation().getY() - (JMonkeyUtil.getSpatialDimension(ballModel).getY() / 2));

        float ballGroundIndicatorSize = 0.5f;
        Geometry ballGroundIndicatorGeometry = new Geometry(null, new Quad(ballGroundIndicatorSize, ballGroundIndicatorSize));
        ballGroundIndicatorGeometry.setLocalTranslation((ballGroundIndicatorSize / -2), groundHeight, (ballGroundIndicatorSize / 2));
        ballGroundIndicatorGeometry.rotate(JMonkeyUtil.getQuaternion_X(-90));
        Material materialBallGroundIndicator = new Material(mainApplication.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        materialBallGroundIndicator.setTexture("ColorMap", MaterialFactory.loadTexture(mainApplication.getAssetManager(), "textures/ball_ground_indicator.png"));
        materialBallGroundIndicator.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        materialBallGroundIndicator.getAdditionalRenderState().setDepthTest(false);
        ballGroundIndicatorGeometry.setMaterial(materialBallGroundIndicator);
        ballGroundIndicatorGeometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        ballGroundIndicator = new Node();
        ballGroundIndicator.attachChild(ballGroundIndicatorGeometry);
        rootNode.attachChild(ballGroundIndicator);

        float targetInGoalIndicatorSize = 1.5f;
        Geometry targetInGoalIndicatorGeometry = new Geometry(null, new Quad(targetInGoalIndicatorSize, targetInGoalIndicatorSize));
        targetInGoalIndicatorGeometry.setLocalTranslation(0, (targetInGoalIndicatorSize / -4), (targetInGoalIndicatorSize / -2));
        targetInGoalIndicatorGeometry.rotate(JMonkeyUtil.getQuaternion_Y(-90));
        Material materialTargetInGoalIndicator = new Material(mainApplication.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        materialTargetInGoalIndicator.setTexture("ColorMap", MaterialFactory.loadTexture(mainApplication.getAssetManager(), "textures/target_in_goal_indicator.png"));
        materialTargetInGoalIndicator.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        materialTargetInGoalIndicator.getAdditionalRenderState().setDepthTest(false);
        materialTargetInGoalIndicator.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        targetInGoalIndicatorGeometry.setMaterial(materialTargetInGoalIndicator);
        targetInGoalIndicatorGeometry.setQueueBucket(RenderQueue.Bucket.Translucent);
        targetInGoalIndicator = new Node();
        targetInGoalIndicator.attachChild(targetInGoalIndicatorGeometry);
        rootNode.attachChild(targetInGoalIndicator);

        Container scoreContainer = new Container();
        scoreContainer.setLocalTranslation(20, mainApplication.getContext().getSettings().getHeight() - 20, 0);
        lblGoals = new Label("");
        scoreContainer.addChild(lblGoals);
        guiNode.attachChild(scoreContainer);

        optimalStrengthIndicator = new Label("");
        optimalStrengthIndicator.setBackground(new IconComponent("textures/optimal_strength_indicator.png"));
        optimalStrengthIndicator.setLocalTranslation(new Vector3f(0, 70, 2));
        optimalStrengthIndicator.setLocalScale(0.085f);
        guiNode.attachChild(optimalStrengthIndicator);

        playerContainer = new Container();
        playerContainer.setLocalTranslation(20, 61, 0);
        playerContainer.setPreferredSize(new Vector3f(200, 20, 1));
        pbrStrength = new ProgressBar();
        playerContainer.addChild(pbrStrength);
        guiNode.attachChild(playerContainer);

        mainApplication.getRootNode().attachChild(rootNode);
        mainApplication.getGuiNode().attachChild(guiNode);

        // TODO: Why do I have to cast here?
        PauseIngameMenuAppState pauseIngameMenuAppState = (PauseIngameMenuAppState) getAppState(PauseIngameMenuAppState.class);
        for (int teamIndex = 0; teamIndex < teams.length; teamIndex++) {
            TeamInfo teamInfo = game.getTeams()[teamIndex].getTeamInfo();
            pauseIngameMenuAppState.setTeam(teamIndex, teamInfo);
        }
    }

    private PlayerVisual createPlayerVisual(PlayerObject playerObject) {
        PlayerVisual playerVisual = new PlayerVisual(mainApplication.getAssetManager());
        String trikotName = (playerObject.isGoalkeeper() ? "thinstripes" : playerObject.getTeam().getTrikotName());
        playerVisual.setTrikot(trikotName);
        playerVisuals.put(playerObject, playerVisual);
        return playerVisual;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        // Skip logic on initial very long frame that loads the models
        if (isFirstFrame) {
            isFirstFrame = false;
            return;
        }
        game.update(tpf);

        for (Team team : game.getTeams()) {
            for (PlayerObject playerObject : team.getPlayers()) {
                updatePlayerVisual(playerObject);
                rootNode.attachChild(playerVisuals.get(playerObject).getWrapperNode());
            }
            for (PlayerObject playerObject : team.getReservePlayers()) {
                rootNode.detachChild(playerVisuals.get(playerObject).getWrapperNode());
            }
        }

        for (Map.Entry<Controller, Node> entry : controllerVisuals.entrySet()) {
            Controller controller = entry.getKey();
            Node controllerVisual = entry.getValue();
            if (controller.getPlayerObject() != null) {
                controllerVisual.setLocalTranslation(controller.getPlayerObject().getPosition());
                controllerVisual.setCullHint(Spatial.CullHint.Inherit);
            } else {
                controllerVisual.setCullHint(Spatial.CullHint.Always);
            }
        }

        updateTransform(game.getBall(), ballModel);

        PhysicsPrecomputationResult ballOnGroundResult = null;
        if ((game.getSituation() == null) && (game.getNextSituation() == null)) {
            ballOnGroundResult = game.getBall().precomputeTransformUntil(result -> result.getPosition().getY() < 0.2f);
        }
        if ((ballOnGroundResult != null) && (ballOnGroundResult.getPassedTime() > 0)) {
            ballGroundIndicator.setLocalTranslation(ballOnGroundResult.getPosition());
            ballGroundIndicator.setCullHint(Spatial.CullHint.Inherit);
        } else {
            ballGroundIndicator.setCullHint(Spatial.CullHint.Always);
        }

        if (game.getSituation() instanceof NearFreeKickSituation) {
            NearFreeKickSituation nearFreeKickSituation = (NearFreeKickSituation) game.getSituation();
            targetInGoalIndicator.setLocalTranslation(nearFreeKickSituation.getTargetInGoalPosition());
            targetInGoalIndicator.setCullHint(Spatial.CullHint.Inherit);

            float optimalStrengthIndicatorX = 11 + (nearFreeKickSituation.getOptimalShootStrength() * 195);
            optimalStrengthIndicator.setLocalTranslation(optimalStrengthIndicator.getLocalTranslation().setX(optimalStrengthIndicatorX));
            optimalStrengthIndicator.setCullHint(Spatial.CullHint.Inherit);
        } else {
            targetInGoalIndicator.setCullHint(Spatial.CullHint.Always);
            optimalStrengthIndicator.setCullHint(Spatial.CullHint.Always);
        }

        // TODO: UI for multiple controllers
        Controller controller1 = game.getControllers().get(0);
        if (controller1.getPlayerObject() != null) {
            ChargedButtonBehaviour chargingButtonBehaviour = controller1.getChargingButtonBehaviour();
            if (chargingButtonBehaviour != null) {
                displayedStrength = chargingButtonBehaviour.getCurrentChargeStrength();
                remainingDisplayedStrengthDuration = 2;
                displayedStrengthPlayerObject = controller1.getPlayerObject();
            } else if (remainingDisplayedStrengthDuration > 0) {
                remainingDisplayedStrengthDuration -= tpf;
                if ((remainingDisplayedStrengthDuration <= 0) || (controller1.getPlayerObject() != displayedStrengthPlayerObject)) {
                    displayedStrength = 0;
                    remainingDisplayedStrengthDuration = 0;
                    displayedStrengthPlayerObject = null;
                }
            }
            pbrStrength.setProgressPercent(displayedStrength);
            playerContainer.setCullHint(Spatial.CullHint.Inherit);
        } else {
            playerContainer.setCullHint(Spatial.CullHint.Always);
        }

        Camera camera = mainApplication.getCamera();
        CameraPerspective cameraPerspective = game.getCameraPerspective();
        if (cameraPerspective != null) {
            camera.setLocation(cameraPerspective.getPosition());
            camera.lookAtDirection(cameraPerspective.getDirection(), Vector3f.UNIT_Y);
        } else {
            targetCameraLocation.set(0.8f * game.getBall().getPosition().getX(), 0, 0.5f * (game.getBall().getPosition().getZ() + 25));
            targetCameraLocation.addLocal(0, 20, 20);
            camera.setLocation(targetCameraLocation);
            camera.lookAtDirection(new Vector3f(0, -1, -1.25f), Vector3f.UNIT_Y);
        }

        float passedTime = game.getHalfTimePassedTime();
        if (game.getHalfTime() == 1) {
            passedTime += game.getHalfTimeDuration();
        }
        String time = getFormattedTime(passedTime) + " (+" + getFormattedTime(game.getHalfTimePassedOverTime()) + ")";
        lblGoals.setText(game.getGoals()[0] + " : " + game.getGoals()[1] + " --- " + time);
        // TODO: Why do I have to cast here?
        PauseIngameMenuAppState pauseIngameMenuAppState = (PauseIngameMenuAppState) getAppState(PauseIngameMenuAppState.class);
        pauseIngameMenuAppState.setTime(time);
        pauseIngameMenuAppState.setScore(game.getGoals()[0], game.getGoals()[1]);

        if (game.isGameOver()) {
            mainApplication.getStateManager().detach(this);
            mainApplication.getStateManager().getState(PauseIngameMenuAppState.class).setEnabled(false);
            mainApplication.getStateManager().getState(GameOverIngameMenuAppState.class).setEnabled(true);
        }
    }

    private void updatePlayerVisual(PlayerObject playerObject) {
        PlayerVisual playerVisual = playerVisuals.get(playerObject);
        updateTransform(playerObject, playerVisual.getModelNode());
        // Run Animation
        float velocity = playerObject.getVelocity().length();
        PlayerAnimation playerAnimation = playerObject.getAnimation();
        if (isNullOrDefaultRunAnimation(playerAnimation)) {
            playerAnimation = getPlayerDefaultRunAnimation(velocity);
        }
        playerVisual.playAnimation(playerAnimation);
    }

    private String getFormattedTime(float time) {
        int secondsPerHalfTime = (45 * 60);
        int seconds = (int) ((time / game.getHalfTimeDuration()) * secondsPerHalfTime);
        int minutes = (seconds / 60);
        seconds -= (minutes * 60);
        return getFormattedMinutesOrSeconds(minutes) + ":" + getFormattedMinutesOrSeconds(seconds);
    }

    private String getFormattedMinutesOrSeconds(int value) {
        return ((value < 10) ? "0" : "") + value;
    }

    private void updateTransform(PhysicsObject physicsObject, Spatial spatial) {
        spatial.setLocalTranslation(physicsObject.getPosition());
        spatial.setLocalRotation(physicsObject.getRotation());
    }

    private boolean isNullOrDefaultRunAnimation(PlayerAnimation playerAnimation) {
        return ((playerAnimation == null)
             || (playerAnimation == PlayerVisual.RUN_ANIMATION_FAST)
             || (playerAnimation == PlayerVisual.RUN_ANIMATION_MEDIUM)
             || (playerAnimation == PlayerVisual.RUN_ANIMATION_SLOW)
             || (playerAnimation == PlayerVisual.IDLE_ANIMATION));
    }

    private PlayerAnimation getPlayerDefaultRunAnimation(float velocity) {
        if (velocity > 8) {
            return PlayerVisual.RUN_ANIMATION_FAST;
        } else if (velocity > 3) {
            return PlayerVisual.RUN_ANIMATION_MEDIUM;
        } else if (velocity > MathUtil.EPSILON) {
            return PlayerVisual.RUN_ANIMATION_SLOW;
        }
        return PlayerVisual.IDLE_ANIMATION;
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getRootNode().detachChild(rootNode);
        mainApplication.getGuiNode().detachChild(guiNode);
        mainApplication.getJoystickListener().setGameSubListener(null);
    }
}
