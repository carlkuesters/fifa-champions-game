package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameAppState;
import com.carlkuesters.fifachampions.ReplayAppState;
import com.carlkuesters.fifachampions.replay.ReplayFrame;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.QuadBackgroundComponent;

public class ReplayMenuAppState extends MenuAppState {

    private int containerWidth;
    private int containerX;
    private int containerY;
    private int markerWidth = 10;
    private Panel marker;
    private int lblTimeWidth = 200;
    private int lblTimeHeight = 35;
    private Label lblTime;

    @Override
    protected void initMenu() {
        int containerMargin = 100;
        containerWidth = (totalWidth - (2 * containerMargin));
        int containerHeight = 50;
        containerX = containerMargin;
        containerY = (containerMargin + containerHeight);
        Container container = new Container();
        container.setLocalTranslation(containerX, containerY, 0);
        container.setPreferredSize(new Vector3f(containerWidth, containerHeight, 0));
        guiNode.attachChild(container);

        marker = new Panel();
        marker.setPreferredSize(new Vector3f(markerWidth, containerHeight, 0));
        QuadBackgroundComponent background = (QuadBackgroundComponent) marker.getBackground();
        background.setColor(ColorRGBA.White);
        guiNode.attachChild(marker);

        lblTime = new Label("");
        lblTime.setTextHAlignment(HAlignment.Center);
        lblTime.setPreferredSize(new Vector3f(lblTimeWidth, lblTimeHeight, 0));
        lblTime.setFontSize(20);
        lblTime.setColor(ColorRGBA.White);
        guiNode.attachChild(lblTime);

        ReplayMenuGroup menuGroup = new ReplayMenuGroup(this::moveTime, this::togglePlaying);
        addMenuGroup(menuGroup);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        ReplayAppState replayAppState = getAppState(ReplayAppState.class);
        ReplayFrame currentFrame = replayAppState.getCurrentFrame();
        float replayProgress = replayAppState.getCurrentReplayProgress();
        float markerX = containerX + (replayProgress * (containerWidth - markerWidth));
        marker.setLocalTranslation(markerX, containerY, 1);
        lblTime.setText(currentFrame.getCombinedTime());
        lblTime.setLocalTranslation(markerX + (markerWidth / 2f) - (lblTimeWidth / 2f), containerY + lblTimeHeight, 1);
    }

    private void moveTime(float deltaTime) {
        ReplayAppState replayAppState = getAppState(ReplayAppState.class);
        replayAppState.moveTime(deltaTime);
    }

    private void togglePlaying() {
        ReplayAppState replayAppState = getAppState(ReplayAppState.class);
        getAppState(ReplayAppState.class).setPlaying(!replayAppState.isPlaying());
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        GameAppState gameAppState = getAppState(GameAppState.class);
        if (gameAppState != null) {
            gameAppState.setSynchronizeVisuals(!enabled);

            ReplayAppState replayAppState = getAppState(ReplayAppState.class);
            if (enabled) {
                replayAppState.jumpToEnd();
            } else {
                replayAppState.setPlaying(false);
            }
        }
    }
}
