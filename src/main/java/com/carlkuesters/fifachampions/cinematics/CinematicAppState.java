package com.carlkuesters.fifachampions.cinematics;

import com.carlkuesters.fifachampions.game.BaseDisplayAppState;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.control.CameraControl;
import lombok.Getter;

public class CinematicAppState extends BaseDisplayAppState {

    private Cinematic currentCinematic;
    @Getter
    private CameraNode cameraNode;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        cameraNode = new CameraNode("cinematicCameraNode", mainApplication.getCamera());
        cameraNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        cameraNode.setEnabled(false);
        mainApplication.getRootNode().attachChild(cameraNode);
    }

    public void playCinematic(Cinematic cinematic){
        stopCinematic();
        cinematic.reset();
        currentCinematic = cinematic;
    }

    @Override
    public void update(float lastTimePerFrame){
        super.update(lastTimePerFrame);
        if (currentCinematic != null) {
            currentCinematic.update(lastTimePerFrame, mainApplication);
            if (currentCinematic.isFinished()) {
                if (currentCinematic.isLoop()) {
                    currentCinematic.reset();
                } else {
                    currentCinematic = null;
                }
            }
        }
    }

    public void stopCinematic() {
        if (currentCinematic != null) {
            currentCinematic.stop(mainApplication);
            currentCinematic = null;
        }
    }
}
