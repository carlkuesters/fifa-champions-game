package com.carlkuesters.fifachampions.cinematics.cinematics;

import com.carlkuesters.fifachampions.JMonkeyUtil;
import com.carlkuesters.fifachampions.cinematics.Cinematic;
import com.carlkuesters.fifachampions.cinematics.CinematicPart;
import com.carlkuesters.fifachampions.cinematics.actions.AlphaFadeAction;
import com.carlkuesters.fifachampions.cinematics.actions.AttachSpatialAction;
import com.carlkuesters.fifachampions.cinematics.actions.CameraPathAction;
import com.carlkuesters.fifachampions.cinematics.actions.DetachSpatialAction;
import com.carlkuesters.fifachampions.game.Game;
import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;

public class OffsideCinematic extends Cinematic {

    public OffsideCinematic(float x) {
        this.x = x;
    }
    private float x;

    @Override
    protected void initialize(SimpleApplication simpleApplication) {
        super.initialize(simpleApplication);
        Spatial line = new Geometry("offsideLine", new Quad(1, 1));
        float lineWidth = 0.05f;
        line.setLocalTranslation(x - (lineWidth / 2), 0.35f, Game.FIELD_HALF_HEIGHT);
        line.setLocalRotation(new Quaternion());
        line.rotate(JMonkeyUtil.getQuaternion_X(-90));
        line.setLocalScale(lineWidth, (2 * Game.FIELD_HALF_HEIGHT), 1);
        Material lineMaterial = new Material(simpleApplication.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        lineMaterial.setColor("Color", new ColorRGBA(1, 1, 0, 0));
        lineMaterial.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        lineMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        line.setMaterial(lineMaterial);
        line.setQueueBucket(RenderQueue.Bucket.Transparent);

        parts = new CinematicPart[] {
            new CinematicPart(0, new CameraPathAction(new MotionEvent() {{
                setPath(new MotionPath() {{
                    addWayPoint(new Vector3f(x - 4, 6, 43));
                    addWayPoint(new Vector3f(x + 4, 6, 43));
                }});
                setDirectionType(Direction.Rotation);
                setRotation(new Quaternion().fromAngleAxis(0, Vector3f.UNIT_Y));
                setRotation(new Quaternion(-5.996786E-8f, 0.9966429f, -0.08187138f, -4.7628244E-8f));
                setSpeed(1.5f);
            }})),
            new CinematicPart(1.5f, new AttachSpatialAction(rootNode, line)),
            new CinematicPart(1.5f, new AlphaFadeAction(lineMaterial, 0, 1, 1)),
            new CinematicPart(5, new AlphaFadeAction(lineMaterial, 1, 0, 1)),
            new CinematicPart(6, new DetachSpatialAction(line)),
        };
    }
}
