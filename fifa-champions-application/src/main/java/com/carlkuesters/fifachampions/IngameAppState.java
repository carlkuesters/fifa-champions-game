package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.game.*;
import com.carlkuesters.fifachampions.game.situations.NearFreeKickSituation;
import com.carlkuesters.fifachampions.menu.GameOverIngameMenuAppState;
import com.carlkuesters.fifachampions.visuals.*;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;

import java.util.HashMap;
import java.util.Map;

public class IngameAppState extends BaseDisplayAppState {

    private Node rootNode;
    private Node guiNode;
    private HashMap<Controller, Node> controllerVisuals = new HashMap<>();
    private Node ballGroundIndicator;
    private Node targetInGoalIndicator;
    private ScoreContainer scoreContainer;
    private ControlledPlayerContainer[] controlledPlayerContainers;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        rootNode = new Node();
        guiNode = new Node();

        GameAppState gameAppState = getAppState(GameAppState.class);
        Game game = gameAppState.getGame();

        gameAppState.setDisplayVisuals(true);
        gameAppState.setSynchronizeVisuals(true);

        ColorRGBA controllerColor = ColorRGBA.Blue;
        for (Controller controller : getAppState(ControllerAppState.class).getControllers().values()) {
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

        BallVisual ballVisual = gameAppState.getBallVisual();
        float groundHeight = (ballVisual.getBallNode().getLocalTranslation().getY() - (JMonkeyUtil.getSpatialDimension(ballVisual.getBallModel()).getY() / 2));

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

        scoreContainer = new ScoreContainer();
        scoreContainer.setTeams(game.getTeams()[0].getTeamInfo(), game.getTeams()[1].getTeamInfo());
        scoreContainer.getNode().setLocalTranslation(20, mainApplication.getContext().getSettings().getHeight() - 20, 0);
        guiNode.attachChild(scoreContainer.getNode());

        controlledPlayerContainers = new ControlledPlayerContainer[2];
        for (int teamIndex = 0; teamIndex < game.getTeams().length; teamIndex++) {
            ControlledPlayerContainer controlledPlayerContainer = new ControlledPlayerContainer(teamIndex == 0);
            int marginX = 20;
            int x ;
            if (teamIndex == 1) {
                x = (mainApplication.getContext().getSettings().getWidth() - marginX - ControlledPlayerContainer.WIDTH);
            } else {
                x = marginX;
            }
            controlledPlayerContainer.getNode().setLocalTranslation(x, 0, 0);
            guiNode.attachChild(controlledPlayerContainer.getNode());
            controlledPlayerContainers[teamIndex] = controlledPlayerContainer;
        }

        mainApplication.getRootNode().attachChild(rootNode);
        mainApplication.getGuiNode().attachChild(guiNode);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        GameAppState gameAppState = getAppState(GameAppState.class);
        Game game = gameAppState.getGame();

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

        PhysicsPrecomputationResult ballOnGroundResult = null;
        if ((game.getSituation() == null) && (game.getNextSituation() == null)) {
            ballOnGroundResult = game.getBall().precomputeTransformUntil(result -> result.getPosition().getY() < 0.2f);
        }
        if ((ballOnGroundResult != null) && (ballOnGroundResult.getPassedTime() > 0)) {
            // Prevent flickering
            if (ballOnGroundResult.getPosition().distanceSquared(ballGroundIndicator.getLocalTranslation()) > 1) {
                ballGroundIndicator.setLocalTranslation(ballOnGroundResult.getPosition());
            }
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

        ControllerAppState controllerAppState = getAppState(ControllerAppState.class);
        for (int teamIndex = 0; teamIndex < game.getTeams().length; teamIndex++) {
            Team team = game.getTeams()[teamIndex];
            Controller controller = null;
            for (Controller currentController : controllerAppState.getControllers().values()) {
                if (currentController.getTeam() == team) {
                    if ((controller == null) || currentController.getPlayerObject().isOwningBall()) {
                        controller = currentController;
                    }
                }
            }
            Float optimalShootStrength = null;
            if (game.getSituation() instanceof NearFreeKickSituation) {
                NearFreeKickSituation nearFreeKickSituation = (NearFreeKickSituation) game.getSituation();
                if (nearFreeKickSituation.getStartingPlayer().getTeam() == team) {
                    optimalShootStrength = nearFreeKickSituation.getOptimalShootStrength();
                }
            }
            controlledPlayerContainers[teamIndex].update(controller, optimalShootStrength, tpf);
        }

        TimeFormatter timeFormatter = gameAppState.getTimeFormatter();
        scoreContainer.setTimeAndGoals(timeFormatter.getTime(), timeFormatter.getOverTime(), game.getGoals());

        Spatial.CullHint generalCullHint = (gameAppState.isPaused() ? Spatial.CullHint.Always : Spatial.CullHint.Inherit);
        rootNode.setCullHint(generalCullHint);
        guiNode.setCullHint(generalCullHint);

        if (game.isGameOver()) {
            mainApplication.getStateManager().detach(this);
            mainApplication.getStateManager().getState(GameOverIngameMenuAppState.class).setEnabled(true);
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getRootNode().detachChild(rootNode);
        mainApplication.getGuiNode().detachChild(guiNode);
    }
}
