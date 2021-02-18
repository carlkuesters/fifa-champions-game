package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.game.InitialTeamInfo;
import com.carlkuesters.fifachampions.game.TeamInfo;
import com.jme3.math.Vector2f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;

public class TeamsMenuAppState extends MenuAppState {

    private Label[] lblTeamNames = new Label[2];

    @Override
    protected void initMenu() {
        addTitle("Anstoß");
        addSide(-1);
        addSide(1);
    }

    private void addSide(int side) {
        int teamIndex = ((side + 1) / 2);
        InitialTeamInfo initialTeamInfo = mainApplication.getGameCreationInfo().getTeams()[teamIndex];
        TeamInfo teamInfo = initialTeamInfo.getTeamInfo();

        int containerMarginOutside = 150;
        int containerMarginBetween = 400;
        int containerWidth = ((totalWidth - (2 * containerMarginOutside) - containerMarginBetween) / 2);
        int containerX = ((totalWidth / 2) + (side * (containerMarginBetween / 2)));
        if (side == -1) {
            containerX -= containerWidth;
        }
        int containerY = (totalHeight - 200);
        Container container = new Container();
        container.setLocalTranslation(containerX, containerY, 0);

        Label lblTeamName = new Label(teamInfo.getName());
        lblTeamName.setFontSize(20);
        container.addChild(lblTeamName);
        lblTeamNames[teamIndex] = lblTeamName;

        Panel teamImage = new Panel();
        IconComponent teamIconComponent = new IconComponent("textures/logo.png");
        teamIconComponent.setIconSize(new Vector2f(containerWidth, containerWidth));
        teamImage.setBackground(teamIconComponent);
        container.addChild(teamImage);

        Label lblDefense = new Label("Defense: XX");
        lblDefense.setFontSize(20);
        lblDefense.setTextHAlignment(HAlignment.Center);
        container.addChild(lblDefense);

        Label lblOffense = new Label("Offense: XX");
        lblOffense.setFontSize(20);
        lblOffense.setTextHAlignment(HAlignment.Center);
        container.addChild(lblOffense);

        Label lblMiddlefield = new Label("Midlefield: XX");
        lblMiddlefield.setFontSize(20);
        lblMiddlefield.setTextHAlignment(HAlignment.Center);
        container.addChild(lblMiddlefield);

        guiNode.attachChild(container);

        TeamsMenuGroup menuGroup = new TeamsMenuGroup(
            () -> openMenu(InitialSideSelectionMenuAppState.class),
            mainApplication.getGameCreationInfo(),
            this::updateTeam,
            joyId -> openMenu(TrikotMenuAppState.class)
        );
        addMenuGroup(menuGroup);
    }

    private void updateTeam(int teamIndex) {
        InitialTeamInfo initialTeamInfo = mainApplication.getGameCreationInfo().getTeams()[teamIndex];
        String teamName = initialTeamInfo.getTeamInfo().getName();
        lblTeamNames[teamIndex].setText(teamName);
    }
}
