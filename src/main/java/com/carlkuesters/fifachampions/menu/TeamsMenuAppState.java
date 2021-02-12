package com.carlkuesters.fifachampions.menu;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;

public class TeamsMenuAppState extends MenuAppState {

    @Override
    protected void initMenu() {
        int titleMarginTop = 70;
        int titleWidth = 200;
        Label lblTitle = new Label("Anstoß");
        lblTitle.setFontSize(32);
        lblTitle.setLocalTranslation(new Vector3f((totalWidth / 2f) - (titleWidth / 2f), totalHeight - titleMarginTop, 0));
        lblTitle.setPreferredSize(new Vector3f(titleWidth, 0, 0));
        lblTitle.setTextHAlignment(HAlignment.Center);
        guiNode.attachChild(lblTitle);

        addSide(-1);
        addSide(1);
    }

    private void addSide(int side) {
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

        Label lblTeamName = new Label("FC-Champions");
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

        MenuGroup menuGroup = new MenuGroup(this::backToMainMenu);
        menuGroup.addElement(new MenuElement(container, this::openShirtsMenu));
        addMenuGroup(menuGroup);
    }

    private void backToMainMenu() {
        close();
        mainApplication.getStateManager().attach(new MainMenuAppState());
    }

    private void openShirtsMenu() {
        close();
        mainApplication.getStateManager().attach(new ShirtMenuAppState());
    }
}
