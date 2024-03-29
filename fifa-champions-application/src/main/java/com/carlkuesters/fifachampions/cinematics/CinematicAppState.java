package com.carlkuesters.fifachampions.cinematics;

import com.carlkuesters.fifachampions.BaseDisplayAppState;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.control.CameraControl;
import lombok.Getter;

public class CinematicAppState extends BaseDisplayAppState {

    @Getter
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
        cinematic.reset(mainApplication);
        currentCinematic = cinematic;
    }

    @Override
    public void update(float lastTimePerFrame){
        super.update(lastTimePerFrame);
        if (currentCinematic != null) {
            currentCinematic.update(lastTimePerFrame);
            if (currentCinematic.isFinished()) {
                if (currentCinematic.isLoop()) {
                    currentCinematic.reset(mainApplication);
                } else {
                    stopCinematic();
                }
            }
        }
    }

    public void stopCinematic() {
        if (currentCinematic != null) {
            currentCinematic.stop();
            currentCinematic = null;
        }
    }
}
