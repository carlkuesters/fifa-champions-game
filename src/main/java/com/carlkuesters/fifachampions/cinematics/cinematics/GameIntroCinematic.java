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

        addPart(new CinematicPart(0, new PlayAudioAction(audioNode)));
        CinematicPart cameraPart1 = addPart(new CinematicPart(0, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(39, 11, 29));
                addWayPoint(new Vector3f(25, 22, 29));
            }});
            setDirectionType(Direction.LookAt);
            setLookAt(new Vector3f(0, 10, 0), Vector3f.UNIT_Y);
            setSpeed(1);
        }})));
        CinematicPart cameraPart2 = addPart(new CinematicPart(cameraPart1, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(48, 22, -43));
                addWayPoint(new Vector3f(-35, 19, -54));
                addWayPoint(new Vector3f(-50, 13, -14));
            }});
            setDirectionType(Direction.Rotation);
            setRotation(new Quaternion(0.030663978f, -0.723667f, 0.032254815f, 0.68871284f));
            setSpeed(1);
        }})));
        CinematicPart cameraPart3 = addPart(new CinematicPart(cameraPart2, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(0, 3, 36));
                addWayPoint(new Vector3f(0, 6, -22));
            }});
            setDirectionType(Direction.Rotation);
            setRotation(new Quaternion(0.027416293f, -0.008860216f, 2.664796E-4f, 0.99958473f));
            setSpeed(1);
        }})));
        addPart(new CinematicPart(cameraPart3, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(-81, 58, 44));
                addWayPoint(new Vector3f(-42, 35, -55));
                addWayPoint(new Vector3f(41, 7, -28));
                addWayPoint(new Vector3f(14, 4, 26));
                addWayPoint(new Vector3f(0, 20, 32.5f));
            }});
            setDirectionType(Direction.LookAt);
            setLookAt(new Vector3f(0, -6, 0), Vector3f.UNIT_Y);
            setSpeed(1);
        }})));
    }
}
