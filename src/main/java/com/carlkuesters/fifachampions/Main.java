package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.game.*;
import com.carlkuesters.fifachampions.game.situations.NearFreeKickSituation;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.JoystickAxis;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.simsilica.lemur.*;
import com.simsilica.lemur.style.BaseStyles;

import java.util.HashMap;
import java.util.Map.Entry;

public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.setShowSettings(false);
        app.start();
    }

    public Main() {
        settings = new AppSettings(true);
        settings.setWidth(1600);
        settings.setHeight(900);
        settings.setUseJoysticks(true);
        settings.setFrameRate(60);
    }

    private Game game;

    private HashMap<PlayerObject, Node> playerVisuals = new HashMap<>();
    private HashMap<Controller, Spatial> controllerVisuals = new HashMap<>();
    private Node ballGroundIndicator;
    private Node targetInGoalIndicator;
    private Vector3f targetCameraLocation = new Vector3f();
    private Spatial ballModel;
    private Controller controller1;
    private Label lblGoals;

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator("../assets/", FileLocator.class);

        Team team1 = generateTeam("Team1");
        Team team2 = generateTeam("Team2");
        game = new Game(new Team[]{team1, team2});
        controller1 = new Controller();
        game.addController(controller1);
        controller1.setContext(game, team1);
        Controller controller2 = new Controller();
        game.addController(controller2);
        controller2.setContext(game, team2);
        game.start();

        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-1, -1, -1)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

        PostFilterAppState postFilterAppState = new PostFilterAppState();
        DirectionalLightShadowFilter shadowFilter = new DirectionalLightShadowFilter(assetManager, 2048, 3);
        shadowFilter.setLight(sun);
        shadowFilter.setShadowIntensity(0.4f);
        postFilterAppState.addFilter(shadowFilter);
        stateManager.attach(postFilterAppState);

        Spatial stadium = assetManager.loadModel("models/stadium/stadium.j3o");
        stadium.move(12.765f, 0, -10.06f);
        stadium.rotate(0, FastMath.HALF_PI, 0);
        stadium.scale(1.1775f);
        stadium.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.attachChild(stadium);

        /*Geometry fieldTestBounds = new Geometry("", new Box(Game.FIELD_HALF_WIDTH, Game.GOAL_HEIGHT / 2, Game.FIELD_HALF_HEIGHT));
        fieldTestBounds.setLocalTranslation(0, (Game.GOAL_HEIGHT / 2), 0);
        Material fieldMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md"); 
        fieldMat.setBoolean("UseMaterialColors", true); 
        fieldMat.setColor("Ambient", ColorRGBA.Green); 
        fieldMat.setColor("Diffuse", ColorRGBA.Green);
        fieldMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        fieldTestBounds.setMaterial(fieldMat); 
        rootNode.attachChild(fieldTestBounds);

        for (int i = 0; i < 2; i++) {
            Geometry goalTestBounds = new Geometry("", new Box(Game.GOAL_WIDTH / 2, Game.GOAL_HEIGHT / 2, (Game.GOAL_Z_TOP - Game.GOAL_Z_BOTTOM) / 2));
            int side = ((i == 0) ? 1 : -1);
            float x = (side * (Game.FIELD_HALF_WIDTH + (Game.GOAL_WIDTH / 2)));
            float z = (Game.GOAL_Z_TOP - ((Game.GOAL_Z_TOP - Game.GOAL_Z_BOTTOM) / 2));
            goalTestBounds.setLocalTranslation(x, (Game.GOAL_HEIGHT / 2), z);
            Material goalMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md"); 
            goalMat.setBoolean("UseMaterialColors", true); 
            goalMat.setColor("Ambient", ColorRGBA.Blue); 
            goalMat.setColor("Diffuse", ColorRGBA.Blue);
            goalMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
            goalTestBounds.setMaterial(goalMat); 
            rootNode.attachChild(goalTestBounds);
        }*/

        String teamColorName = "yellow";
        float playerModelOffsetForFeetsOnGround = 1.302f;
        for (Team team : game.getTeams()) {
            for (PlayerObject playerObject : team.getPlayers()) {
                Node playerModel = (Node) assetManager.loadModel("models/player/player.j3o");
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
            Material materialController = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
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
        ballModel = assetManager.loadModel("models/ball/ball.j3o");
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
        Material materialBallGroundIndicator = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
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
        Material materialTargetInGoalIndicator = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        materialTargetInGoalIndicator.setTexture("ColorMap", loadTexture("textures/target_in_goal_indicator.png"));
        materialTargetInGoalIndicator.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        materialTargetInGoalIndicator.getAdditionalRenderState().setDepthTest(false);
        materialTargetInGoalIndicator.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        targetInGoalIndicatorGeometry.setMaterial(materialTargetInGoalIndicator);
        targetInGoalIndicatorGeometry.setQueueBucket(RenderQueue.Bucket.Translucent);
        targetInGoalIndicator = new Node();
        targetInGoalIndicator.attachChild(targetInGoalIndicatorGeometry);
        rootNode.attachChild(targetInGoalIndicator);

        cam.setFrustumPerspective(45, (float) cam.getWidth() / cam.getHeight(), 0.01f, 1000);
        cam.setLocation(new Vector3f(0, 100, 0));
        flyCam.setMoveSpeed(100);
        flyCam.setEnabled(false);

        inputManager.addRawInputListener(new JoystickEventListener());

        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        Container scoreContainer = new Container();
        scoreContainer.setLocalTranslation(20, context.getSettings().getHeight() - 20, 0);
        lblGoals = new Label("");
        scoreContainer.addChild(lblGoals);
        guiNode.attachChild(scoreContainer);
    }

    private Material createTextureMaterial(String diffusePath, String normalPath, String specularPath) {
        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        // textureDiffuse.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("DiffuseMap", loadTexture(diffusePath));
        material.setTexture("NormalMap", loadTexture(normalPath));
        material.setTexture("SpecularMap", loadTexture(specularPath));
        return material;
    }

    private Texture loadTexture(String filePath){
        return assetManager.loadTexture(new TextureKey(filePath, false));
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

    protected class JoystickEventListener implements RawInputListener {
        
        private float axisX;
        private float axisY;

        public void onJoyAxisEvent(JoyAxisEvent evt) {
            JoystickAxis axis = evt.getAxis();
            float value = evt.getValue();
            if (axis == axis.getJoystick().getXAxis()) {
                axisX = value;
            } else if(axis == axis.getJoystick().getYAxis()) {
                axisY = value;
            }
            float x = 0;
            float y = 0;
            float minimumAxisValue = 0.0001f;
            if ((FastMath.abs(axisX) > minimumAxisValue) || (FastMath.abs(axisY) > minimumAxisValue)) {
                float squareToCircleFactor = FastMath.sqrt((axisX * axisX) + (axisY * axisY) - (axisX * axisX * axisY * axisY)) / FastMath.sqrt((axisX * axisX) + (axisY * axisY));
                x = axisX * squareToCircleFactor;
                y = axisY * squareToCircleFactor;
            }
            controller1.setTargetDirection(x, y);
        }

        public void onJoyButtonEvent(JoyButtonEvent evt) {
            controller1.onButtonPressed(evt.getButtonIndex(), evt.isPressed());
        }

        public void beginInput() {}
        public void endInput() {}
        public void onMouseMotionEvent(MouseMotionEvent evt) {}
        public void onMouseButtonEvent(MouseButtonEvent evt) {}
        public void onKeyEvent(KeyInputEvent evt) {}
        public void onTouchEvent(TouchEvent evt) {}
    }
    
    boolean isFirstFrame = true;

    @Override
    public void simpleUpdate(float tpf) {
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
        } else {
            targetInGoalIndicator.setCullHint(Spatial.CullHint.Always);
        }

        CameraPerspective cameraPerspective = game.getCameraPerspective();
        if (cameraPerspective != null) {
            cam.setLocation(cameraPerspective.getPosition());
            cam.lookAtDirection(cameraPerspective.getDirection(), Vector3f.UNIT_Y);
        } else {
            targetCameraLocation.set(0.8f * game.getBall().getPosition().getX(), 0, 0.5f * (game.getBall().getPosition().getZ() + 25));
            targetCameraLocation.addLocal(0, 20, 20);
            cam.setLocation(targetCameraLocation);
            cam.lookAtDirection(new Vector3f(0, -1, -1.25f), Vector3f.UNIT_Y);
        }

        lblGoals.setText(game.getGoals()[0] + " : " + game.getGoals()[1] + " --- " + game.getHalfTimePassedTime() + " (+" + game.getHalfTimePassedOverTime() + ")");
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

    public void enqueueTask(Runnable runnable){
        enqueue(() -> {
            runnable.run();
            return null;
        });
    }
}
