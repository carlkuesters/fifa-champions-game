package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.game.InitialTeamInfo;
import com.carlkuesters.fifachampions.game.content.Players;
import com.carlkuesters.fifachampions.visuals.PlayerSkin;
import com.carlkuesters.fifachampions.visuals.PlayerSkins;
import com.carlkuesters.fifachampions.visuals.PlayerVisual;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;

public class TrikotMenuAppState extends MenuAppState {

    private Label[] lblTrikotNames = new Label[2];
    private PlayerVisual[] playerVisuals = new PlayerVisual[2];
    private float playerVisualsAngle;

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
            mainApplication.getGameCreationInfo().getTeams()[teamIndex],
            () -> updateTrikot(teamIndex),
            () -> openMenu(GameSettingsMenuAppState.class)
        );
        addMenuGroup(menuGroup);

        PlayerSkin playerSkin = PlayerSkins.get((side == -1) ? Players.MARKUS : Players.STEFFEN);
        PlayerVisual playerVisual = new PlayerVisual(mainApplication.getAssetManager(), playerSkin);
        playerVisual.getModelNode().setLocalTranslation(side * 1.75f, 0, 0);
        playerVisual.playAnimation(PlayerVisual.IDLE_ANIMATION);
        playerVisuals[teamIndex] = playerVisual;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        rotatePlayerVisuals(tpf);
    }

    private void rotatePlayerVisuals(float tpf) {
        TempVars tempVars = TempVars.get();
        playerVisualsAngle += tpf;
        for (int i = 0; i < playerVisuals.length; i++) {
            playerVisuals[i].getModelNode().setLocalRotation(tempVars.quat1.fromAngleAxis(playerVisualsAngle + (i * FastMath.PI), Vector3f.UNIT_Y));
        }
        tempVars.release();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (PlayerVisual playerVisual : playerVisuals) {
            if (enabled) {
                mainApplication.getRootNode().attachChild(playerVisual.getWrapperNode());
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

    @Override
    protected void back() {
        openMenu(TeamsMenuAppState.class);
    }
}
