package com.carlkuesters.fifachampions.cinematics.cinematics;

import com.carlkuesters.fifachampions.GameAppState;
import com.carlkuesters.fifachampions.JMonkeyUtil;
import com.carlkuesters.fifachampions.ReplayAppState;
import com.carlkuesters.fifachampions.cinematics.Cinematic;
import com.carlkuesters.fifachampions.cinematics.CinematicPart;
import com.carlkuesters.fifachampions.cinematics.actions.*;
import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.game.cinematics.cinematics.OffsideCinematicInfoData;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OffsideCinematic extends Cinematic {

    private static final float DURATION_START = 3;
    private static final float DURATION_FREEZE = 2;
    private static final float DURATION_END = 2;
    private static final float DURATION_LINE_FADE = 0.75f;
    private static final float DURATION_TOTAL = DURATION_START + DURATION_FREEZE + DURATION_END;

    public OffsideCinematic(OffsideCinematicInfoData data) {
        this.data = data;
    }
    private OffsideCinematicInfoData data;
    private Geometry line;

    @Override
    protected void initialize(SimpleApplication simpleApplication) {
        super.initialize(simpleApplication);

        GameAppState gameAppState = simpleApplication.getStateManager().getState(GameAppState.class);
        ReplayAppState replayAppState = simpleApplication.getStateManager().getState(ReplayAppState.class);

        line = new Geometry("offsideLine", new Quad(1, 1));
        float lineWidth = 0.05f;
        line.setLocalTranslation(data.getX() - (lineWidth / 2), 0.35f, Game.FIELD_HALF_HEIGHT);
        line.setLocalRotation(new Quaternion());
        line.rotate(JMonkeyUtil.getQuaternion_X(-90));
        line.setLocalScale(lineWidth, (2 * Game.FIELD_HALF_HEIGHT), 1);
        Material lineMaterial = new Material(simpleApplication.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        lineMaterial.setColor("Color", new ColorRGBA(1, 1, 0, 0));
        lineMaterial.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        lineMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        line.setMaterial(lineMaterial);
        line.setQueueBucket(RenderQueue.Bucket.Transparent);

        addPart(new CinematicPart(0, new SimpleAction(() -> {
            gameAppState.setSynchronizeVisuals(false);
            replayAppState.setCurrentReplayTime(data.getReplayTime() - DURATION_START);
            replayAppState.setPlaying(true);
        })));
        addPart(new CinematicPart(0, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(data.getX() - (data.getDirection() * DURATION_START), 6, 43));
                addWayPoint(new Vector3f(data.getX() + (data.getDirection() * DURATION_END), 6, 43));
            }});
            setDirectionType(Direction.Rotation);
            setRotation(new Quaternion(-5.996786E-8f, 0.9966429f, -0.08187138f, -4.7628244E-8f));
            setInitialDuration(DURATION_TOTAL);
        }})));
        addPart(new CinematicPart(DURATION_START, new AttachSpatialAction(rootNode, line)));
        addPart(new CinematicPart(DURATION_START, new AlphaFadeAction(lineMaterial, 0, 1, DURATION_LINE_FADE)));
        addPart(new CinematicPart(DURATION_START, new SimpleAction(() -> replayAppState.setPlaying(false))));
        addPart(new CinematicPart(DURATION_START + DURATION_FREEZE, new SimpleAction(() -> replayAppState.setPlaying(true))));
        addPart(new CinematicPart(DURATION_START + DURATION_FREEZE, new AlphaFadeAction(lineMaterial, 1, 0, DURATION_LINE_FADE)));
        addPart(new CinematicPart(DURATION_START + DURATION_FREEZE + DURATION_LINE_FADE, new DetachSpatialAction(line)));
    }

    @Override
    public void stop() {
        super.stop();
        AppStateManager stateManager = getSimpleApplication().getStateManager();

        rootNode.detachChild(line);

        stateManager.getState(GameAppState.class).setSynchronizeVisuals(true);
        stateManager.getState(ReplayAppState.class).setPlaying(false);
    }
}
