package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.game.InitialTeamInfo;
import com.carlkuesters.fifachampions.visuals.PlayerVisual;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;

public class TrikotMenuAppState extends MenuAppState {

    private Label[] lblTrikotNames = new Label[2];
    private PlayerVisual[] playerVisuals = new PlayerVisual[2];

    @Override
    protected void initMenu() {
        addTitle("Trikot");
        addSide(-1);
        addSide(1);
    }

    private void addSide(int side) {
        int teamIndex = ((side + 1) / 2);

        int containerMarginBetween = 550;
        int containerWidth = 300;
        int containerX = ((totalWidth / 2) + (side * (containerMarginBetween / 2)));
        if (side == -1) {
            containerX -= containerWidth;
        }
        int containerY = 169;
        Container container = new Container();
        container.setLocalTranslation(containerX, containerY, 0);

        Label lblTrikotName = new Label("XXX");
        lblTrikotName.setFontSize(20);
        lblTrikotName.setTextHAlignment(HAlignment.Center);
        lblTrikotName.setPreferredSize(new Vector3f(containerWidth, 30, 0));
        lblTrikotNames[teamIndex] = lblTrikotName;
        container.addChild(lblTrikotName);

        guiNode.attachChild(container);

        TrikotMenuGroup menuGroup = new TrikotMenuGroup(
            () -> openMenu(TeamsMenuAppState.class),
            mainApplication.getGameCreationInfo(),
            this::updateTrikot,
            joyId -> openMenu(GameSettingsMenuAppState.class)
        );
        addMenuGroup(menuGroup);

        PlayerVisual playerVisual = new PlayerVisual(mainApplication.getAssetManager());
        playerVisual.getModelNode().setLocalTranslation(side * 1.75f, 0, 0);
        playerVisual.playAnimation(PlayerVisual.IDLE_ANIMATION);
        playerVisuals[teamIndex] = playerVisual;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (PlayerVisual playerVisual : playerVisuals) {
            if (enabled) {
                mainApplication.getRootNode().attachChild(playerVisual.getWrapperNode());
                mainApplication.getCamera().setLocation(new Vector3f(0, 1, 5));
                mainApplication.getCamera().lookAtDirection(new Vector3f(0, 0.05f, -1), Vector3f.UNIT_Y);
                updateTrikot(0);
                updateTrikot(1);
            } else {
                mainApplication.getRootNode().detachChild(playerVisual.getWrapperNode());
            }
        }
    }

    private void updateTrikot(int teamIndex) {
        InitialTeamInfo initialTeamInfo = mainApplication.getGameCreationInfo().getTeams()[teamIndex];
        String trikotName = initialTeamInfo.getTeamInfo().getTrikotNames()[initialTeamInfo.getTrikotIndex()];
        lblTrikotNames[teamIndex].setText(trikotName);
        playerVisuals[teamIndex].setTrikot(trikotName);
    }
}
