package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.game.TeamInfo;
import com.jme3.math.Vector2f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;

public class TeamsMenuAppState extends MenuAppState {

    @Override
    protected void initMenu() {
        addTitle("Anstoß");
        addSide(-1);
        addSide(1);
    }

    private void addSide(int side) {
        int teamIndex = ((side + 1) / 2);
        TeamInfo teamInfo = mainApplication.getGameCreationInfo().getTeams()[teamIndex];

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

        ElementsMenuGroup menuGroup = new ElementsMenuGroup(() -> openMenu(InitialTeamSelectionMenuAppState.class));
        menuGroup.addElement(new MenuElement(container, () -> openMenu(ShirtMenuAppState.class)));
        addMenuGroup(menuGroup);
    }
}
