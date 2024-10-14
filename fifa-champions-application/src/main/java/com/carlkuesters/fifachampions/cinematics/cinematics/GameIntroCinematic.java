package com.carlkuesters.fifachampions.cinematics.cinematics;

import com.carlkuesters.fifachampions.*;
import com.carlkuesters.fifachampions.cinematics.Cinematic;
import com.carlkuesters.fifachampions.cinematics.CinematicPart;
import com.carlkuesters.fifachampions.cinematics.actions.*;
import com.carlkuesters.fifachampions.game.*;
import com.carlkuesters.fifachampions.visuals.BallVisual;
import com.carlkuesters.fifachampions.visuals.PlayerVisual;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.*;
import com.jme3.post.filters.FadeFilter;

public class GameIntroCinematic extends Cinematic {

    private BallVisual[] ballVisuals = new BallVisual[3];

    @Override
    protected void initialize(SimpleApplication simpleApplication) {
        super.initialize(simpleApplication);
        GameAppState gameAppState = simpleApplication.getStateManager().getState(GameAppState.class);
        Game game = gameAppState.getGame();

        // FadeIn
        PostFilterAppState postFilterAppState = simpleApplication.getStateManager().getState(PostFilterAppState.class);
        float fadeDuration = 2;
        FadeFilter fadeFilter = new FadeFilter(fadeDuration);
        postFilterAppState.addFilter(fadeFilter);
        addPart(new CinematicPart(0, new SimpleAction(() -> {
            fadeFilter.setValue(0);
            fadeFilter.fadeIn();
        })));
        addPart(new CinematicPart(fadeDuration, new SimpleAction(() -> postFilterAppState.removeFilter(fadeFilter))));

        // Audio
        AudioNode audioNode = new AudioNode(simpleApplication.getAssetManager(), "sounds/anthem_short.ogg", AudioData.DataType.Stream);
        audioNode.setPositional(false);
        audioNode.setVolume(0.5f);
        rootNode.attachChild(audioNode);
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
        addPart(new CinematicPart(cameraPart2, new SimpleAction(() -> gameAppState.setDisplayVisuals(true))));
        for (int i = 0; i < game.getTeams().length; i++) {
            float side = ((i - 0.5f) * 2);
            for (int r = 0; r < game.getTeams()[i].getPlayers().length; r++) {
                int _r = r;
                PlayerObject playerObject = game.getTeams()[i].getPlayers()[r];
                PlayerVisual playerVisual = gameAppState.getPlayerVisual(playerObject);
                addPart(new CinematicPart(cameraPart2, new SimpleAction(() -> gameAppState.setDisplayPlayerVisual(playerVisual, true))));
                float startX = ((side * 1) + (float) ((Math.random() - 0.5f) * 0.25f));
                addPart(new CinematicPart(cameraPart2, new PlayerAnimationAction(playerVisual, PlayerAnimations.createRunSlow(), true)));
                CinematicPart walkPart = addPart(new CinematicPart(cameraPart2, new MoveAction(playerVisual.getModelNode(), new MotionEvent() {{
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
                addPart(new CinematicPart(walkPart, new PlayerAnimationAction(playerVisual, PlayerAnimations.createIdle(), true)));
            }
        }
        // Stadion QuickCut 1
        CinematicPart cameraPart4 = addPart(new CinematicPart(8.4f + 10.2f + 11.5f + 5.2f, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(20, 1.5f, 2));
                addWayPoint(new Vector3f(-5, 2.5f, 0));
            }});
            setDirectionType(Direction.Path);
            setInitialDuration(1.8f);
        }})));
        // Stadion QuickCut 2
        CinematicPart cameraPart5 = addPart(new CinematicPart(cameraPart4, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(-20, 20, -15));
                addWayPoint(new Vector3f(20, 10, -15));
            }});
            setDirectionType(Direction.LookAt);
            setLookAt(new Vector3f(0, 2, 0), Vector3f.UNIT_Y);
            setInitialDuration(2.2f);
        }})));
        // Stadion QuickCut 3
        CinematicPart cameraPart6 = addPart(new CinematicPart(cameraPart5, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(-58.5f, 2.5f, -8));
                addWayPoint(new Vector3f(-58.5f, 2.5f, 6));
            }});
            setDirectionType(Direction.Rotation);
            setRotation(new Quaternion(-0.0027860003f, 0.7806335f, 0.0034786568f, 0.6249731f));
            setInitialDuration(4);
        }})));
        // Player 1
        addPart(new CinematicPart(cameraPart6, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(-44, 2, -10));
                addWayPoint(new Vector3f(-44, 2, -5));
            }});
            setDirectionType(Direction.Rotation);
            setRotation(new Quaternion(0.0053341277f, -0.41391551f, 0.0024251242f, 0.91029644f));
            setInitialDuration(4.25f);
        }})));
        float[] zOffsetStart = new float[] { -0.5f, -0.9f, -0.6f };
        float[] zOffsetEnd = new float[] { -0.5f, -0.6f, 1.5f };
        for (int i = 0; i < 3; i++) {
            final int _i = i;
            PlayerObject playerObject = gameAppState.getGame().getTeams()[0].getPlayers()[1 + i];
            PlayerVisual playerVisual = gameAppState.getPlayerVisual(playerObject);
            addPart(new CinematicPart(cameraPart6, new PlayerAnimationAction(playerVisual, new PlayerAnimation("run_backwards", 0.6f, true), true)));
            addPart(new CinematicPart(cameraPart6, new MoveAction(playerVisual.getModelNode(), new MotionEvent() {{
                setPath(new MotionPath() {{
                    addWayPoint(new Vector3f(-49 - (_i + 0.5f), 0, -9 + zOffsetStart[_i]));
                    addWayPoint(new Vector3f(-49 - (_i + 0.5f), 0, 4 + zOffsetEnd[_i]));
                }});
                setDirectionType(Direction.PathAndRotation);
                setRotation(new Quaternion().fromAngleAxis((float) Math.PI, Vector3f.UNIT_Y));
                setInitialDuration(4.25f);
            }})));
        }
        PlayerObject playerObjectSprint = gameAppState.getGame().getTeams()[0].getPlayers()[4];
        PlayerVisual playerVisualSprint = gameAppState.getPlayerVisual(playerObjectSprint);
        addPart(new CinematicPart(cameraPart6, new PlayerAnimationAction(playerVisualSprint, PlayerAnimations.createRunFast(), true)));
        addPart(new CinematicPart(cameraPart6, new MoveAction(playerVisualSprint.getModelNode(), new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(-47, 0, 12));
                addWayPoint(new Vector3f(-47, 0, -13));
            }});
            setDirectionType(Direction.Path);
            setInitialDuration(4.25f);
        }})));
        // Player 2
        float start8 = 8.4f + 10.2f + 11.5f + 5.2f + 1.8f + 2.2f + 4 + 4.25f;
        CinematicPart cameraPart8 = addPart(new CinematicPart(start8, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(0, 2.5f, -15));
                addWayPoint(new Vector3f(0, 2, -15));
            }});
            setDirectionType(Direction.Rotation);
            setRotation(new Quaternion(-0.00631611f, 0.96426797f, -0.0230733f, -0.26384667f));
            setInitialDuration(4.1f);
        }})));
        float[] zOffset = new float[] { -1, -0.7f, -1 };
        float[] timeOffsets = new float[] { 0.2f, 0.4f, 0 };
        for (int i = 0; i < ballVisuals.length; i++) {
            Vector3f playerPosition = new Vector3f(-2 - (i * 2), 0, -20 + zOffset[i]);
            Vector3f ballPositionNear = playerPosition.add(-0.3f, 0, 0);
            Vector3f ballPositionFar = ballPositionNear.add(0, 0, 10);

            BallVisual ballVisual = new BallVisual(simpleApplication.getAssetManager());
            addPart(new CinematicPart(start8, new AttachSpatialAction(simpleApplication.getRootNode(), ballVisual.getBallNode())));
            CinematicPart ballMovePart = addPart(new CinematicPart(start8 + 1 + timeOffsets[i], new MoveAction(ballVisual.getBallModel(), new MotionEvent() {{
                setPath(new MotionPath() {{
                    setPathSplineType(Spline.SplineType.Linear);
                    addWayPoint(ballPositionFar);
                    addWayPoint(ballPositionNear);
                    addWayPoint(ballPositionFar);
                }});
                setDirectionType(Direction.Rotation);
                setRotation(new Quaternion());
                setInitialDuration(2);
            }})));
            addPart(new CinematicPart(ballMovePart, new DetachSpatialAction(ballVisual.getBallNode())));
            ballVisuals[i] = ballVisual;

            PlayerObject playerObject = gameAppState.getGame().getTeams()[0].getPlayers()[5 + i];
            PlayerVisual playerVisual = gameAppState.getPlayerVisual(playerObject);
            addPart(new CinematicPart(start8, new PlayerAnimationAction(playerVisual, PlayerAnimations.createIdle(), true)));
            addPart(new CinematicPart(start8, new MoveAction(playerVisual.getModelNode(), new MotionEvent() {{
                setPath(new MotionPath() {{
                    addWayPoint(playerPosition);
                    addWayPoint(playerPosition);
                }});
                setDirectionType(Direction.Rotation);
                setRotation(new Quaternion());
                setInitialDuration(4.1f);
            }})));
            addPart(new CinematicPart(start8 + 1 + timeOffsets[i], new PlayerAnimationAction(playerVisual, new PlayerAnimation("standing_pass", 2.5f), false)));
            addPart(new CinematicPart(start8 + 1 + timeOffsets[i] + 2.5f, new PlayerAnimationAction(playerVisual, PlayerAnimations.createIdle(), true)));
        }
        // Stadion 3
        CinematicPart cameraPart9 = addPart(new CinematicPart(cameraPart8, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(-55, 8, 36));
                addWayPoint(new Vector3f(49, 8, 40));
            }});
            setDirectionType(Direction.LookAt);
            setLookAt(new Vector3f(0, 11, -40), Vector3f.UNIT_Y);
            setInitialDuration(4.2f);
        }})));
        float radius = 2;
        float runToPositionsTime = 54;
        for (int i = 0; i < game.getTeams().length; i++) {
            float side = ((i - 0.5f) * 2);
            Vector3f center = new Vector3f((side * 10), 0, 0);
            Team team = game.getTeams()[i];
            for (int r = 0; r < team.getPlayers().length; r++) {
                float angle = ((((float) r) / team.getPlayers().length) * FastMath.TWO_PI);
                float x = center.getX() + (FastMath.sin(angle) * radius);
                float z = center.getZ() + (FastMath.cos(angle) * radius);
                Vector3f position = new Vector3f(x, 0, z);
                PlayerObject playerObject = team.getPlayers()[r];
                PlayerVisual playerVisual = gameAppState.getPlayerVisual(playerObject);
                addPart(new CinematicPart(cameraPart8, new PlayerAnimationAction(playerVisual, PlayerAnimations.createIdle(), true)));
                addPart(new CinematicPart(cameraPart8, new SimpleAction(() -> {
                    playerVisual.getModelNode().setLocalTranslation(position);
                    playerVisual.getModelNode().setLocalRotation(new Quaternion().fromAngleAxis(angle + FastMath.PI, Vector3f.UNIT_Y));
                })));
                addPart(new CinematicPart(runToPositionsTime, new SimpleAction(() -> {
                    playerObject.setPosition(position);
                    playerObject.lookAt_XZ(center);
                })));
            }
        }
        addPart(new CinematicPart(runToPositionsTime, new SimpleAction(() -> gameAppState.setSynchronizeVisuals(true))));
        // Stadion End
        addPart(new CinematicPart(cameraPart9, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(-81, 58, 44));
                addWayPoint(new Vector3f(-42, 35, -55));
                addWayPoint(new Vector3f(41, 7, -28));
                addWayPoint(new Vector3f(14, 4, 26));
                addWayPoint(GameAppState.DEFAULT_CAMERA_LOCATION);
            }});
            setDirectionType(Direction.LookAt);

            Plane groundPlane = new Plane(Vector3f.UNIT_Y, 0);
            Ray defaultGameCameraRay = new Ray(GameAppState.DEFAULT_CAMERA_LOCATION, GameAppState.DEFAULT_CAMERA_DIRECTION);
            Vector3f defaultGameCameraLookAtOnGround = new Vector3f();
            defaultGameCameraRay.intersectsWherePlane(groundPlane, defaultGameCameraLookAtOnGround);
            setLookAt(defaultGameCameraLookAtOnGround, Vector3f.UNIT_Y);

            setInitialDuration(8);
        }})));
        CameraAppState cameraAppState = simpleApplication.getStateManager().getState(CameraAppState.class);
        addPart(new CinematicPart(cameraPart9, new SimpleAction(() -> {
            cameraAppState.setFieldOfView(GameAppState.DEFAULT_CAMERA_FIELD_OF_VIEW);
        })));
    }

    @Override
    public void stop() {
        super.stop();
        AppStateManager stateManager = getSimpleApplication().getStateManager();

        for (BallVisual ballVisual : ballVisuals) {
            getSimpleApplication().getRootNode().detachChild(ballVisual.getBallNode());
        }

        stateManager.attach(new ReplayAppState());
        stateManager.attach(new IngameAppState());
    }
}
