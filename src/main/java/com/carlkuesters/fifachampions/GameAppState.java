package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.game.*;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ChargedButtonBehaviour;
import com.carlkuesters.fifachampions.game.situations.NearFreeKickSituation;
import com.carlkuesters.fifachampions.joystick.GameJoystickSubListener;
import com.carlkuesters.fifachampions.menu.FormationMenuAppState;
import com.carlkuesters.fifachampions.menu.GameOverIngameMenuAppState;
import com.carlkuesters.fifachampions.menu.PauseIngameMenuAppState;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.TextureKey;
import com.jme3.input.Joystick;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;

import java.util.HashMap;
import java.util.Map.Entry;

public class GameAppState extends BaseDisplayAppState {

    private Game game;

    private Node rootNode;
    private Node guiNode;
    private HashMap<PlayerObject, Node> playerVisuals = new HashMap<>();
    private HashMap<Controller, Node> controllerVisuals = new HashMap<>();
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

        // TODO: Create via menu and pass here
        GameCreationInfo gameCreationInfo = new GameCreationInfo();
        gameCreationInfo.setTeams(new Team[] {
            generateTeam("Team1"),
            generateTeam("Team2")
        });
        HashMap<Integer, Integer> controllerTeams = new HashMap<>();
        int teamIndex = 0;
        for (Joystick joystick : mainApplication.getInputManager().getJoysticks()) {
            controllerTeams.put(joystick.getJoyId(), teamIndex);
            teamIndex++;
        }
        gameCreationInfo.setControllerTeams(controllerTeams);

        game = new Game(gameCreationInfo.getTeams());

        HashMap<Integer, Controller> controllers = new HashMap<>();
        for (Joystick joystick : mainApplication.getInputManager().getJoysticks()) {
            Controller controller = new Controller();
            int controllerTeamIndex = gameCreationInfo.getControllerTeams().get(joystick.getJoyId());
            Team controllerTeam = gameCreationInfo.getTeams()[controllerTeamIndex];
            controller.setContext(game, controllerTeam);
            game.addController(controller);
            controllers.put(joystick.getJoyId(), controller);
        }
        mainApplication.getJoystickListener().setGameSubListener(new GameJoystickSubListener(controllers, () -> {
            stateManager.getState(PauseIngameMenuAppState.class).setEnabled(true);
        }));

        game.start();

        String teamColorName = "yellow";
        float playerModelOffsetForFeetsOnGround = 1.302f;
        for (Team team : game.getTeams()) {
            for (PlayerObject playerObject : team.getPlayers()) {
                Node playerModel = (Node) mainApplication.getAssetManager().loadModel("models/player/player.j3o");
                playerModel.scale(0.0106f);
                float halfPlayerModelHeight = (JMonkeyUtil.getSpatialDimension(playerModel).getY() / 2);
                // Center player model on y axis
                playerModel.move(0, (playerModelOffsetForFeetsOnGround - halfPlayerModelHeight), 0);
                playerModel.rotate(0, -1 * FastMath.HALF_PI, 0);
                AnimControl animControl = playerModel.getControl(AnimControl.class);
                animControl.createChannel();
                // Head
                Material materialHead = createTextureMaterial(
                        "models/player/resources/WSP_head_D.png",
                        "models/player/resources/WSP_head_N.png",
                        "models/player/resources/WSP_head_SP.png"
                );
                Geometry head = (Geometry) playerModel.getChild("head");
                head.setMaterial(materialHead);
                // Body
                String trikotName = ((playerObject.getPlayer() instanceof Goalkeeper) ? "thinstripes" : teamColorName);
                Material materialBody = createTextureMaterial(
                        "models/player/resources/WSP_body_" + trikotName + "_D.png",
                        "models/player/resources/WSP_body_N.png",
                        "models/player/resources/WSP_body_SP.png"
                );
                Geometry body = (Geometry) playerModel.getChild("body");
                body.setMaterial(materialBody);
                playerModel.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                // Add model to visual
                Node playerVisual = new Node();
                playerVisual.attachChild(playerModel);
                // Add visual to wrapper
                Node playerWrapper = new Node();
                playerWrapper.setLocalTranslation(0, halfPlayerModelHeight, 0);
                playerWrapper.attachChild(playerVisual);
                // Add wrapper to root
                rootNode.attachChild(playerWrapper);
                playerVisuals.put(playerObject, playerVisual);
            }
            teamColorName = "red";
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
        Material materialBall = createTextureMaterial(
                "models/ball/resources/ball_D.png",
                "models/ball/resources/ball_N.png",
                "models/ball/resources/ball_SP.png"
        );
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
        materialBallGroundIndicator.setTexture("ColorMap", loadTexture("textures/ball_ground_indicator.png"));
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
        materialTargetInGoalIndicator.setTexture("ColorMap", loadTexture("textures/target_in_goal_indicator.png"));
        materialTargetInGoalIndicator.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        materialTargetInGoalIndicator.getAdditionalRenderState().setDepthTest(false);
        materialTargetInGoalIndicator.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        targetInGoalIndicatorGeometry.setMaterial(materialTargetInGoalIndicator);
        targetInGoalIndicatorGeometry.setQueueBucket(RenderQueue.Bucket.Translucent);
        targetInGoalIndicator = new Node();
        targetInGoalIndicator.attachChild(targetInGoalIndicatorGeometry);
        rootNode.attachChild(targetInGoalIndicator);

        Camera cam = mainApplication.getCamera();
        cam.setLocation(new Vector3f(0, 100, 0));

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
    }

    private Material createTextureMaterial(String diffusePath, String normalPath, String specularPath) {
        Material material = new Material(mainApplication.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        // textureDiffuse.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("DiffuseMap", loadTexture(diffusePath));
        material.setTexture("NormalMap", loadTexture(normalPath));
        material.setTexture("SpecularMap", loadTexture(specularPath));
        return material;
    }

    private Texture loadTexture(String filePath){
        return mainApplication.getAssetManager().loadTexture(new TextureKey(filePath, false));
    }

    private Team generateTeam(String teamName) {
        Player[] players = new Player[11];
        players[0] = new Goalkeeper("Goalkeeper");
        for (int i = 1; i < players.length; i++) {
            players[i] = new Player(teamName + "-Player #" + i);
        }
        return new Team(players, new Formation(new Vector2f[]{
                new Vector2f(-1, 0),

                new Vector2f(-0.7f, -0.75f),
                new Vector2f(-0.7f, -0.25f),
                new Vector2f(-0.7f, 0.25f),
                new Vector2f(-0.7f, 0.75f),

                new Vector2f(0, -0.75f),
                new Vector2f(0, -0.25f),
                new Vector2f(0, 0.25f),
                new Vector2f(0, 0.75f),

                new Vector2f(0.7f, -0.5f),
                new Vector2f(0.7f, 0.5f)
        }));
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

        for (Entry<PlayerObject, Node> playerEntry : playerVisuals.entrySet()) {
            PlayerObject playerObject = playerEntry.getKey();
            Node playerVisual = playerEntry.getValue();
            updateTransform(playerObject, playerVisual);
            if (playerObject.getController() != null) {
                Spatial controllerVisual = controllerVisuals.get(playerObject.getController());
                controllerVisual.setLocalTranslation(playerObject.getPosition());
            }
            // Run Animation
            float velocity = playerObject.getVelocity().length();
            PlayerAnimation playerAnimation = playerObject.getAnimation();
            if (isNullOrDefaultRunAnimation(playerAnimation)) {
                playerAnimation = getPlayerDefaultRunAnimation(velocity);
            }
            AnimChannel animChannel = playerVisual.getChild(0).getControl(AnimControl.class).getChannel(0);
            if (!playerAnimation.getName().equals(animChannel.getAnimationName())) {
                animChannel.setAnim(playerAnimation.getName());
            }
            animChannel.setSpeed(animChannel.getAnimMaxTime() / playerAnimation.getLoopDuration());
            animChannel.setLoopMode(playerAnimation.getLoopMode());
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
            passedTime += Game.HALFTIME_DURATION;
        }
        String time = getFormattedTime(passedTime) + " (+" + getFormattedTime(game.getHalfTimePassedOverTime()) + ")";
        lblGoals.setText(game.getGoals()[0] + " : " + game.getGoals()[1] + " --- " + time);
        // TODO: Why do I have to cast here?
        PauseIngameMenuAppState pauseIngameMenuAppState = (PauseIngameMenuAppState) getAppState(PauseIngameMenuAppState.class);
        pauseIngameMenuAppState.setTime(time);
        pauseIngameMenuAppState.setScore(game.getGoals()[0], game.getGoals()[1]);

        // TODO: Why do I have to cast here?
        FormationMenuAppState formationMenuAppState = (FormationMenuAppState) getAppState(FormationMenuAppState.class);
        for (Team team : game.getTeams()) {
            for (int playerIndex = 0; playerIndex < team.getPlayers().size(); playerIndex++) {
                Vector2f formationLocation = team.getFormation().getLocation(playerIndex);
                formationMenuAppState.setFormationPlayerPosition(team.getSide(), playerIndex, formationLocation);
            }
        }

        if (game.isGameOver()) {
            mainApplication.getStateManager().detach(this);
            mainApplication.getStateManager().getState(PauseIngameMenuAppState.class).setEnabled(false);
            mainApplication.getStateManager().getState(GameOverIngameMenuAppState.class).setEnabled(true);
        }
    }

    private String getFormattedTime(float time) {
        int secondsPerHalfTime = (45 * 60);
        int seconds = (int) ((time / Game.HALFTIME_DURATION) * secondsPerHalfTime);
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

    private PlayerAnimation RUN_ANIMATION_FAST = new PlayerAnimation("run_fast", 0.7f);
    private PlayerAnimation RUN_ANIMATION_MEDIUM = new PlayerAnimation("run_medium", 1.17f);
    private PlayerAnimation RUN_ANIMATION_SLOW = new PlayerAnimation("run_slow", 1.59f);
    private PlayerAnimation IDLE_ANIMATION = new PlayerAnimation("idle", 4);

    private boolean isNullOrDefaultRunAnimation(PlayerAnimation playerAnimation) {
        return ((playerAnimation == null)
                || (playerAnimation == RUN_ANIMATION_FAST)
                || (playerAnimation == RUN_ANIMATION_MEDIUM)
                || (playerAnimation == RUN_ANIMATION_SLOW)
                || (playerAnimation == IDLE_ANIMATION));
    }

    private PlayerAnimation getPlayerDefaultRunAnimation(float velocity) {
        if (velocity > 8) {
            return RUN_ANIMATION_FAST;
        } else if (velocity > 3) {
            return RUN_ANIMATION_MEDIUM;
        } else if (velocity > MathUtil.EPSILON) {
            return RUN_ANIMATION_SLOW;
        }
        return IDLE_ANIMATION;
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getRootNode().detachChild(rootNode);
        mainApplication.getGuiNode().detachChild(guiNode);
        mainApplication.getJoystickListener().setGameSubListener(null);
    }
}
