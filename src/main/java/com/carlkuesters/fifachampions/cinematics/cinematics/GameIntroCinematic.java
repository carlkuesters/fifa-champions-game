package com.carlkuesters.fifachampions.cinematics.cinematics;

import com.carlkuesters.fifachampions.cinematics.Cinematic;
import com.carlkuesters.fifachampions.cinematics.CinematicPart;
import com.carlkuesters.fifachampions.cinematics.actions.CameraPathAction;
import com.carlkuesters.fifachampions.cinematics.actions.PlayAudioAction;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class GameIntroCinematic extends Cinematic {

    @Override
    protected void initialize(SimpleApplication simpleApplication) {
        super.initialize(simpleApplication);

        AudioNode audioNode = new AudioNode(simpleApplication.getAssetManager(), "sounds/anthem.ogg", AudioData.DataType.Stream);
        audioNode.setPositional(false);
        audioNode.setLooping(false);
        audioNode.setVolume(0.5f);
        rootNode.attachChild(audioNode);

        // TODO: Replace with actual intro, currently reusing the main menu stuff for testing purposes
        parts = new CinematicPart[] {
            new CinematicPart(0, new PlayAudioAction(audioNode)),
            new CinematicPart(0, new CameraPathAction(new MotionEvent() {{
                setPath(new MotionPath() {{
                    addWayPoint(new Vector3f(-85, 25, -50));
                    addWayPoint(new Vector3f(40, 17, 20));
                }});
                setDirectionType(Direction.Path);
                setSpeed(0.5f);
            }})),
            new CinematicPart(16, new CameraPathAction(new MotionEvent() {{
                setPath(new MotionPath() {{
                    addWayPoint(new Vector3f(35, 6, -45));
                    addWayPoint(new Vector3f(-59, 6, -6));
                }});
                setDirectionType(Direction.Rotation);
                setRotation(new Quaternion().fromAngleAxis(0, Vector3f.UNIT_Y));
                setSpeed(0.5f);
            }})),
            new CinematicPart(36, new CameraPathAction(new MotionEvent() {{
                setPath(new MotionPath() {{
                    addWayPoint(new Vector3f(-50, 95, -20));
                    addWayPoint(new Vector3f(50, 95, 10));
                }});
                setDirectionType(Direction.Rotation);
                setRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X));
                setSpeed(0.5f);
            }}))
        };
    }
}
