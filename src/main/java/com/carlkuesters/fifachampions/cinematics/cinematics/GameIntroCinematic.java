package com.carlkuesters.fifachampions.cinematics.cinematics;

import com.carlkuesters.fifachampions.cinematics.Cinematic;
import com.carlkuesters.fifachampions.cinematics.CinematicPart;
import com.carlkuesters.fifachampions.cinematics.actions.*;
import com.carlkuesters.fifachampions.game.content.Players;
import com.carlkuesters.fifachampions.visuals.PlayerSkins;
import com.carlkuesters.fifachampions.visuals.PlayerVisual;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Quaternion;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;

public class GameIntroCinematic extends Cinematic {

    @Override
    protected void initialize(SimpleApplication simpleApplication) {
        super.initialize(simpleApplication);

        AudioNode audioNode = new AudioNode(simpleApplication.getAssetManager(), "sounds/anthem_short.ogg", AudioData.DataType.Stream);
        audioNode.setPositional(false);
        audioNode.setVolume(0.5f);
        rootNode.attachChild(audioNode);

        PlayerVisual[][] playerVisuals = new PlayerVisual[2][11];
        for (int i = 0; i < playerVisuals.length; i++) {
            for (int r = 0; r < playerVisuals[i].length; r++) {
                PlayerVisual playerVisual = new PlayerVisual(simpleApplication.getAssetManager(), PlayerSkins.get(Players.STEFFEN));
                playerVisual.setTrikot((i == 0) ? "red" : "yellow");
                playerVisuals[i][r] = playerVisual;
            }
        }

        addPart(new CinematicPart(0, new PlayAudioAction(audioNode)));
        // Stadion 1
        CinematicPart cameraPart1 = addPart(new CinematicPart(0, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(39, 11, 29));
                addWayPoint(new Vector3f(25, 22, 29));
            }});
            setDirectionType(Direction.LookAt);
            setLookAt(new Vector3f(0, 10, 0), Vector3f.UNIT_Y);
            setInitialDuration(8.4f);
        }})));
        // Stadion 2
        CinematicPart cameraPart2 = addPart(new CinematicPart(cameraPart1, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(48, 22, -43));
                addWayPoint(new Vector3f(-35, 19, -54));
                addWayPoint(new Vector3f(-50, 13, -14));
            }});
            setDirectionType(Direction.Rotation);
            setRotation(new Quaternion(0.030663978f, -0.723667f, 0.032254815f, 0.68871284f));
            setInitialDuration(10.2f);
        }})));
        // Tunnel
        addPart(new CinematicPart(cameraPart2, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(0, 3, 36));
                addWayPoint(new Vector3f(0, 6, -22));
            }});
            setDirectionType(Direction.Rotation);
            setRotation(new Quaternion(0.027416293f, -0.008860216f, 2.664796E-4f, 0.99958473f));
            setInitialDuration(11.5f);
        }})));
        for (int i = 0; i < playerVisuals.length; i++) {
            for (int r = 0; r < playerVisuals[i].length; r++) {
                PlayerVisual playerVisual = playerVisuals[i][r];
                float side = ((i - 0.5f) * 2);
                float startX = ((side * 1) + (float) ((Math.random() - 0.5f) * 0.25f));
                int _r = r;
                addPart(new CinematicPart(cameraPart2, new AttachSpatialAction(simpleApplication.getRootNode(), playerVisual.getWrapperNode())));
                addPart(new CinematicPart(cameraPart2, new PlayerMoveAction(playerVisual, PlayerVisual.RUN_ANIMATION_SLOW, new MotionEvent() {{
                    setPath(new MotionPath() {{
                        setPathSplineType(Spline.SplineType.Linear);
                        addWayPoint(new Vector3f(startX, 0, 54 + (_r * 2)));
                        addWayPoint(new Vector3f(startX, 0, 1));
                        addWayPoint(new Vector3f(side * (1 + _r), 0, 1));
                        addWayPoint(new Vector3f(side * (1 + _r), 0, 0));
                    }});
                    setDirectionType(Direction.Path);
                    setInitialDuration(9.5f + (_r / 2f));
                }})));
            }
        }
        // Stadion QuickCut 1
        CinematicPart cameraPart4 = addPart(new CinematicPart(8.4f + 10.2f + 11.5f + 5.2f, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(39, 11, 29));
                addWayPoint(new Vector3f(25, 22, 29));
            }});
            setDirectionType(Direction.LookAt);
            setLookAt(new Vector3f(0, 10, 0), Vector3f.UNIT_Y);
            setInitialDuration(1.8f);
        }})));
        // Stadion QuickCut 2
        CinematicPart cameraPart5 = addPart(new CinematicPart(cameraPart4, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(39, 11, 29));
                addWayPoint(new Vector3f(25, 22, 29));
            }});
            setDirectionType(Direction.LookAt);
            setLookAt(new Vector3f(0, 10, 0), Vector3f.UNIT_Y);
            setInitialDuration(2.2f);
        }})));
        // Stadion QuickCut 3
        CinematicPart cameraPart6 = addPart(new CinematicPart(cameraPart5, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(39, 11, 29));
                addWayPoint(new Vector3f(25, 22, 29));
            }});
            setDirectionType(Direction.LookAt);
            setLookAt(new Vector3f(0, 10, 0), Vector3f.UNIT_Y);
            setInitialDuration(4);
        }})));
        // Player 1
        CinematicPart cameraPart7 = addPart(new CinematicPart(cameraPart6, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(0, 3, 36));
                addWayPoint(new Vector3f(0, 6, -22));
            }});
            setDirectionType(Direction.Rotation);
            setRotation(new Quaternion(0.027416293f, -0.008860216f, 2.664796E-4f, 0.99958473f));
            setInitialDuration(4.25f);
        }})));
        // Player 2
        CinematicPart cameraPart8 = addPart(new CinematicPart(cameraPart7, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(0, 3, 36));
                addWayPoint(new Vector3f(0, 6, -22));
            }});
            setDirectionType(Direction.Rotation);
            setRotation(new Quaternion(0.027416293f, -0.008860216f, 2.664796E-4f, 0.99958473f));
            setInitialDuration(4.1f);
        }})));
        // Stadion 3
        CinematicPart cameraPart9 = addPart(new CinematicPart(cameraPart8, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(0, 3, 36));
                addWayPoint(new Vector3f(0, 6, -22));
            }});
            setDirectionType(Direction.Rotation);
            setRotation(new Quaternion(0.027416293f, -0.008860216f, 2.664796E-4f, 0.99958473f));
            setInitialDuration(4.2f);
        }})));
        // Stadion End
        addPart(new CinematicPart(cameraPart9, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(-81, 58, 44));
                addWayPoint(new Vector3f(-42, 35, -55));
                addWayPoint(new Vector3f(41, 7, -28));
                addWayPoint(new Vector3f(14, 4, 26));
                addWayPoint(new Vector3f(0, 20, 32.5f));
            }});
            setDirectionType(Direction.LookAt);
            setLookAt(new Vector3f(0, -6, 0), Vector3f.UNIT_Y);
            setInitialDuration(8);
        }})));
    }
}
