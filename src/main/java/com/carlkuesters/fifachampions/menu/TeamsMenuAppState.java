package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.game.TeamInfo;
import com.jme3.math.Vector2f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;

public class TeamsMenuAppState extends MenuAppState {

    private int iconMarginX = 50;
    private int iconSize;
    private Label[] lblsTeamName = new Label[2];
    private Panel[] pansTeamIcon = new Panel[2];
    private Label[] lblsOffense = new Label[2];
    private Label[] lblsMiddlefield = new Label[2];
    private Label[] lblsDefense = new Label[2];

    @Override
    protected void initMenu() {
        addTitle("Anstoß");
        addSide(-1);
        addSide(1);
    }

    private void addSide(int side) {
        int teamIndex = ((side + 1) / 2);

        int containerMarginOutside = 150;
        int containerMarginBetween = 400;
        int containerWidth = ((totalWidth - (2 * containerMarginOutside) - containerMarginBetween) / 2);
        iconSize = (containerWidth - (2 * iconMarginX));
        int containerX = ((totalWidth / 2) + (side * (containerMarginBetween / 2)));
        if (side == -1) {
            containerX -= containerWidth;
        }
        int containerY = (totalHeight - 200);
        Container container = new Container();
        container.setLocalTranslation(containerX, containerY, 0);

        Label lblTeamName = new Label("");
        lblTeamName.setFontSize(20);
        container.addChild(lblTeamName);
        lblsTeamName[teamIndex] = lblTeamName;

        Panel teamImage = new Panel();
        teamImage.setInsets(new Insets3f(20, iconMarginX, 0, iconMarginX));
        container.addChild(teamImage);
        pansTeamIcon[teamIndex] = teamImage;

        Label lblOffense = new Label("");
        lblOffense.setFontSize(20);
        lblOffense.setTextHAlignment(HAlignment.Center);
        container.addChild(lblOffense);
        lblsOffense[teamIndex] = lblOffense;

        Label lblMiddlefield = new Label("");
        lblMiddlefield.setFontSize(20);
        lblMiddlefield.setTextHAlignment(HAlignment.Center);
        container.addChild(lblMiddlefield);
        lblsMiddlefield[teamIndex] = lblMiddlefield;

        Label lblDefense = new Label("");
        lblDefense.setFontSize(20);
        lblDefense.setTextHAlignment(HAlignment.Center);
        lblDefense.setInsets(new Insets3f(0, 0, 20, 0));
        container.addChild(lblDefense);
        lblsDefense[teamIndex] = lblDefense;

        guiNode.attachChild(container);

        TeamsMenuGroup menuGroup = new TeamsMenuGroup(
            mainApplication.getGameCreationInfo().getTeams()[teamIndex],
            () -> updateTeam(teamIndex),
            () -> openMenu(TrikotMenuAppState.class)
        );
        addMenuGroup(menuGroup);

        updateTeam(teamIndex);
    }

    private void updateTeam(int teamIndex) {
        TeamInfo teamInfo = mainApplication.getGameCreationInfo().getTeams()[teamIndex].getTeamInfo();
        lblsTeamName[teamIndex].setText(teamInfo.getName());
        IconComponent teamIconComponent = new IconComponent("textures/teams/" + teamInfo.getName() + ".png");
        teamIconComponent.setIconSize(new Vector2f(iconSize, iconSize));
        pansTeamIcon[teamIndex].setBackground(teamIconComponent);
        lblsOffense[teamIndex].setText("Offense: " + teamInfo.getAverageSkill_Offense());
        lblsMiddlefield[teamIndex].setText("Midlefield: " + teamInfo.getAverageSkill_Middlefield());
        lblsDefense[teamIndex].setText("Defense: " + teamInfo.getAverageSkill_Defense());
    }

    @Override
    protected void back() {
        openMenu(InitialSideSelectionMenuAppState.class);
    }
}
