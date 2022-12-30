package com.carlkuesters.fifachampions;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.FlyByCamera;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import lombok.Getter;

public class CameraAppState extends BaseDisplayAppState {

    @Getter
    private boolean freeCam = false;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        setDefaultFieldOfView();

        FlyByCamera flyByCamera = mainApplication.getFlyByCamera();
        flyByCamera.setMoveSpeed(100);
        flyByCamera.setDragToRotate(true);
        flyByCamera.setEnabled(freeCam);
    }

    public void setDefaultFieldOfView() {
        setFieldOfView(45);
    }

    public void setFieldOfView(float angle) {
        Camera camera = mainApplication.getCamera();
        camera.setFrustumPerspective(angle, ((float) camera.getWidth()) / camera.getHeight(), 0.01f, 300);
    }

    public void setLocationAndDirection(Vector3f position, Vector3f direction) {
        Camera camera = mainApplication.getCamera();
        camera.setLocation(position);
        camera.lookAtDirection(direction, Vector3f.UNIT_Y);
    }
}
