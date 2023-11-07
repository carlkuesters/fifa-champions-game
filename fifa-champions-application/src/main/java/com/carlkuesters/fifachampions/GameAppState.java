package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.cinematics.Cinematic;
import com.carlkuesters.fifachampions.cinematics.CinematicAppState;
import com.carlkuesters.fifachampions.cinematics.cinematics.*;
import com.carlkuesters.fifachampions.game.*;
import com.carlkuesters.fifachampions.game.cinematics.CinematicInfo;
import com.carlkuesters.fifachampions.game.cinematics.cinematics.*;
import com.carlkuesters.fifachampions.joystick.GameJoystickSubListener;
import com.carlkuesters.fifachampions.menu.PauseIngameMenuAppState;
import com.carlkuesters.fifachampions.visuals.BallVisual;
import com.carlkuesters.fifachampions.visuals.TimeFormatter;
import com.carlkuesters.fifachampions.visuals.PlayerSkins;
import com.carlkuesters.fifachampions.visuals.PlayerVisual;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;

public class GameAppState extends BaseDisplayAppState {

    @Getter
    private Game game;
    @Getter
    private TimeFormatter timeFormatter;
    private Node rootNode;
    private HashMap<PlayerObject, PlayerVisual> playerVisuals = new HashMap<>();
    @Getter
    private BallVisual ballVisual;
    @Setter
    @Getter
    private boolean synchronizeVisuals;
    private boolean isFirstFrame = true;
    @Setter
    @Getter
    private boolean paused;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        rootNode = new Node();
        setDisplayVisuals(false);

        ControllerAppState controllerAppState = getAppState(ControllerAppState.class);
        game = new Game(controllerAppState.getControllers().values(), mainApplication.getGameCreationInfo(), this::createCinematic);
        timeFormatter = new TimeFormatter(game);

        controllerAppState.getJoystickListener().setGameSubListener(new GameJoystickSubListener(controllerAppState.getControllers(), () -> {
            if (game.getActiveCinematic() == null) {
                paused = true;
                stateManager.getState(PauseIngameMenuAppState.class).setEnabled(true);
            }
        }));

        for (Team team : game.getTeams()) {
            for (PlayerObject playerObject : team.getPlayers()) {
                createPlayerVisual(playerObject);
            }
            for (PlayerObject playerObject : team.getReservePlayers()) {
                createPlayerVisual(playerObject);
            }
        }

        ballVisual = new BallVisual(mainApplication.getAssetManager());
        rootNode.attachChild(ballVisual.getBallNode());

        mainApplication.getRootNode().attachChild(rootNode);
    }

    private void createPlayerVisual(PlayerObject playerObject) {
        PlayerVisual playerVisual = new PlayerVisual(mainApplication.getAssetManager(), PlayerSkins.get(playerObject.getPlayer()));
        String trikotName = (playerObject.isGoalkeeper() ? "thinstripes" : playerObject.getTeam().getTrikotName());
        playerVisual.setTrikot(trikotName);
        playerVisuals.put(playerObject, playerVisual);

        rootNode.attachChild(playerVisual.getWrapperNode());
        setDisplayPlayerVisual(playerVisual, false);
    }

    private Cinematic createCinematic(CinematicInfo<?> cinematicInfo) {
        if (cinematicInfo instanceof OffsideCinematicInfo) {
            OffsideCinematicInfo offsideCinematicInfo = (OffsideCinematicInfo) cinematicInfo;
            return new OffsideCinematic(offsideCinematicInfo.getData());
        } else if (cinematicInfo instanceof GoalCinematicInfo) {
            return new GoalCinematic();
        } else if (cinematicInfo instanceof GameIntroCinematicInfo) {
            return new GameIntroCinematic();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (isGameRunning()) {
            // Skip logic on initial very long frame that loads the models
            if (isFirstFrame) {
                isFirstFrame = false;
                return;
            }
            game.update(tpf);
        }

        if (synchronizeVisuals) {
            for (Team team : game.getTeams()) {
                for (PlayerObject playerObject : team.getPlayers()) {
                    updatePlayerVisual(playerObject);
                    setDisplayPlayerVisual(getPlayerVisual(playerObject), true);
                }
                for (PlayerObject playerObject : team.getReservePlayers()) {
                    setDisplayPlayerVisual(getPlayerVisual(playerObject), false);
                }
            }
            updateTransform(game.getBall(), ballVisual.getBallModel());
            setAudienceHyped(game.isAudienceHyped());
        }

        CinematicAppState cinematicAppState = getAppState(CinematicAppState.class);
        CameraAppState cameraAppState = getAppState(CameraAppState.class);
        Cinematic activeCinematic = (Cinematic) game.getActiveCinematic();
        if (activeCinematic != null) {
            cameraAppState.setDefaultFieldOfView();
            if (activeCinematic != cinematicAppState.getCurrentCinematic()) {
                cinematicAppState.playCinematic(activeCinematic);
            }
        } else {
            cinematicAppState.stopCinematic();

            if (!cameraAppState.isFreeCam()) {
                CameraPerspective cameraPerspective = game.getCameraPerspective();
                if (cameraPerspective != null) {
                    cameraAppState.setDefaultFieldOfView();
                    cameraAppState.setLocationAndDirection(cameraPerspective.getPosition(), cameraPerspective.getDirection());
                } else {
                    // Use the ball visual instead of the ball game object to support camera during replays
                    Vector3f ballPosition = ballVisual.getBallModel().getLocalTranslation();
                    float x = 1.1f * ballPosition.getX();
                    x = Math.max(-44, Math.min(x, 44));
                    float y = 30;
                    float z = (1 * (ballPosition.getZ() - 10)) + 60;
                    z = Math.max(30, Math.min(z, 63));
                    cameraAppState.setFieldOfView(25);
                    cameraAppState.setLocationAndDirection(new Vector3f(x, y, z), new Vector3f(0, -0.6f, -0.9f));
                }
            }
        }
    }

    private void updatePlayerVisual(PlayerObject playerObject) {
        PlayerVisual playerVisual = getPlayerVisual(playerObject);
        updateTransform(playerObject, playerVisual.getModelNode());
        playerVisual.setAnimation(playerObject.getAnimation());
    }

    public Collection<PlayerVisual> getPlayerVisuals() {
        return playerVisuals.values();
    }

    public PlayerVisual getPlayerVisual(PlayerObject playerObject) {
        return playerVisuals.get(playerObject);
    }

    private void updateTransform(PhysicsObject physicsObject, Spatial spatial) {
        spatial.setLocalTranslation(physicsObject.getPosition());
        spatial.setLocalRotation(physicsObject.getRotation());
    }

    public void setDisplayVisuals(boolean displayed) {
        rootNode.setCullHint(displayed ? Spatial.CullHint.Inherit : Spatial.CullHint.Always);
    }

    public void setDisplayPlayerVisual(PlayerVisual playerVisual, boolean displayed) {
        playerVisual.getWrapperNode().setCullHint(displayed ? Spatial.CullHint.Inherit : Spatial.CullHint.Always);
    }

    public boolean shouldRecordReplayFrames() {
        return isGameRunning() && game.isReplayRecording();
    }

    private boolean isGameRunning() {
        return (!game.isGameOver() && !paused);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getRootNode().detachChild(rootNode);
        getAppState(CameraAppState.class).setDefaultFieldOfView();
        getAppState(ControllerAppState.class).getJoystickListener().setGameSubListener(null);
        setAudienceHyped(false);
    }

    public void setAudienceHyped(boolean hyped) {
        getAppState(StadiumAppState.class).setAudienceHyped(hyped);
    }
}
