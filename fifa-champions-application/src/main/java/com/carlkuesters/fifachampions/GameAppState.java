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
        if (!paused) {
            // Skip logic on initial very long frame that loads the models
            if (isFirstFrame) {
                isFirstFrame = false;
                return;
            }
            game.update(tpf);

            Cinematic activeCinematic = (Cinematic) game.getActiveCinematic();
            CinematicAppState cinematicAppState = getAppState(CinematicAppState.class);
            if (activeCinematic != cinematicAppState.getCurrentCinematic()) {
                if (activeCinematic != null) {
                    cinematicAppState.playCinematic(activeCinematic);
                } else {
                    cinematicAppState.stopCinematic();
                }
            }
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
    }

    private void updatePlayerVisual(PlayerObject playerObject) {
        PlayerVisual playerVisual = getPlayerVisual(playerObject);
        updateTransform(playerObject, playerVisual.getModelNode());
        // Animation
        PlayerAnimation playerAnimation = playerObject.getAnimation();
        if (isNullOrDefaultRunAnimation(playerAnimation)) {
            playerAnimation = getPlayerDefaultRunAnimation(playerObject.getVelocity().length(), playerObject.isTurning());
        }
        playerVisual.playAnimation(playerAnimation);
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

    private boolean isNullOrDefaultRunAnimation(PlayerAnimation playerAnimation) {
        return ((playerAnimation == null)
             || (playerAnimation == PlayerVisual.RUN_ANIMATION_FAST)
             || (playerAnimation == PlayerVisual.RUN_ANIMATION_MEDIUM)
             || (playerAnimation == PlayerVisual.RUN_ANIMATION_SLOW)
             || (playerAnimation == PlayerVisual.IDLE_ANIMATION));
    }

    private PlayerAnimation getPlayerDefaultRunAnimation(float velocity, boolean isTurning) {
        if (velocity > 8) {
            return PlayerVisual.RUN_ANIMATION_FAST;
        } else if ((velocity > 3) || isTurning) {
            return PlayerVisual.RUN_ANIMATION_MEDIUM;
        } else if (velocity > MathUtil.EPSILON) {
            return PlayerVisual.RUN_ANIMATION_SLOW;
        }
        return PlayerVisual.IDLE_ANIMATION;
    }

    public void setDisplayVisuals(boolean displayed) {
        rootNode.setCullHint(displayed ? Spatial.CullHint.Inherit : Spatial.CullHint.Always);
    }

    public void setDisplayPlayerVisual(PlayerVisual playerVisual, boolean displayed) {
        playerVisual.getWrapperNode().setCullHint(displayed ? Spatial.CullHint.Inherit : Spatial.CullHint.Always);
    }

    public boolean shouldRecordReplayFrames() {
        return !paused && game.isReplayRecording();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getRootNode().detachChild(rootNode);
        getAppState(ControllerAppState.class).getJoystickListener().setGameSubListener(null);
        setAudienceHyped(false);
    }

    public void setAudienceHyped(boolean hyped) {
        getAppState(StadiumAppState.class).setAudienceHyped(hyped);
    }
}
