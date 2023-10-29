package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.replay.PhysicsReplayState;
import com.carlkuesters.fifachampions.replay.PlayerReplayState;
import com.carlkuesters.fifachampions.replay.Replay;
import com.carlkuesters.fifachampions.replay.ReplayFrame;
import com.carlkuesters.fifachampions.visuals.PlayerVisual;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class ReplayAppState extends BaseDisplayAppState {

    private Replay replay = new Replay();
    @Getter
    @Setter
    private boolean playing;
    private float currentReplayTime;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        // Save the initial kickoff frame, as the player can open the replay menu before actually starting the game (and therefore recording frames)
        saveFrame(0);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (getAppState(GameAppState.class).shouldRecordReplayFrames()) {
            saveFrame(tpf);
        } else if (playing) {
            moveTime(tpf);
        }
    }

    public void jumpToEnd() {
        setCurrentReplayTime(replay.getDuration());
    }

    public void moveTime(float deltaTime) {
        setCurrentReplayTime(Math.max(0, Math.min(currentReplayTime + deltaTime, replay.getDuration())));
    }

    private void setCurrentReplayTime(float currentReplayTime) {
        this.currentReplayTime = currentReplayTime;
        if (currentReplayTime >= replay.getDuration()) {
            playing = false;
        }
        loadFrame(getCurrentFrame());
    }

    public ReplayFrame getCurrentFrame() {
        return replay.getFrame(currentReplayTime);
    }

    public float getCurrentReplayProgress() {
        if (replay.getDuration() == 0) {
            return 0;
        }
        return (currentReplayTime / replay.getDuration());
    }

    private void saveFrame(float tpf) {
        GameAppState gameAppState = getAppState(GameAppState.class);
        Game game = gameAppState.getGame();

        HashMap<PlayerVisual, PlayerReplayState> playerStates = new HashMap<>();
        for (PlayerVisual playerVisual : gameAppState.getPlayerVisuals()) {
            boolean displayed = playerVisual.getWrapperNode().getCullHint() != Spatial.CullHint.Always;
            Vector3f position = playerVisual.getModelNode().getLocalTranslation().clone();
            Quaternion rotation = playerVisual.getModelNode().getLocalRotation().clone();

            AnimChannel animChannel = playerVisual.getPlayerModel().getControl(AnimControl.class).getChannel(0);
            String animationName = animChannel.getAnimationName();
            float animationTime = animChannel.getTime();

            playerStates.put(playerVisual, new PlayerReplayState(position, rotation, displayed, animationName, animationTime));
        }

        Spatial ballModel = gameAppState.getBallVisual().getBallModel();
        PhysicsReplayState ballState = new PhysicsReplayState(ballModel.getLocalTranslation().clone(), ballModel.getLocalRotation().clone());

        String combinedTime = gameAppState.getTimeFormatter().getCombinedTime();
        boolean audienceHyped = game.isAudienceHyped();

        replay.addFrame(new ReplayFrame(playerStates, ballState, combinedTime, audienceHyped), tpf);
    }

    private void loadFrame(ReplayFrame frame) {
        GameAppState gameAppState = getAppState(GameAppState.class);

        PhysicsReplayState ballState = frame.getBallState();
        Spatial ballModel = gameAppState.getBallVisual().getBallModel();
        ballModel.setLocalTranslation(ballState.getPosition());
        ballModel.setLocalRotation(ballState.getRotation());

        frame.getPlayerStates().forEach((playerVisual, playerState) -> {
            gameAppState.setDisplayPlayerVisual(playerVisual, playerState.isDisplayed());

            Node modelNode = playerVisual.getModelNode();
            modelNode.setLocalTranslation(playerState.getPosition());
            modelNode.setLocalRotation(playerState.getRotation());

            AnimChannel animChannel = playerVisual.getPlayerModel().getControl(AnimControl.class).getChannel(0);
            if (playerState.getAnimationName() != null) {
                animChannel.setAnim(playerState.getAnimationName(), 0);
                animChannel.setTime(playerState.getAnimationTime());
                animChannel.setSpeed(0);
            } else {
                animChannel.reset(true);
            }
        });

        gameAppState.setAudienceHyped(frame.isAudienceHyped());
    }
}
