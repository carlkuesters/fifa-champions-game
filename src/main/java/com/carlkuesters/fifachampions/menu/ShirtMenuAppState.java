package com.carlkuesters.fifachampions.menu;

import com.jme3.math.Vector3f;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;

public class ShirtMenuAppState extends MenuAppState {

    @Override
    protected void initMenu() {
        int titleMarginTop = 70;
        int titleWidth = 200;
        Label lblTitle = new Label("Trikot");
        lblTitle.setFontSize(32);
        lblTitle.setLocalTranslation(new Vector3f((totalWidth / 2f) - (titleWidth / 2f), totalHeight - titleMarginTop, 0));
        lblTitle.setPreferredSize(new Vector3f(titleWidth, 0, 0));
        lblTitle.setTextHAlignment(HAlignment.Center);
        guiNode.attachChild(lblTitle);

        addSide(-1);
        addSide(1);
    }

    private void addSide(int side) {
        int containerMarginBetween = 550;
        int containerWidth = 300;
        int containerX = ((totalWidth / 2) + (side * (containerMarginBetween / 2)));
        if (side == -1) {
            containerX -= containerWidth;
        }
        int containerY = 169;
        Container container = new Container();
        container.setLocalTranslation(containerX, containerY, 0);

        Label lblDefense = new Label("XXX");
        lblDefense.setFontSize(20);
        lblDefense.setTextHAlignment(HAlignment.Center);
        lblDefense.setPreferredSize(new Vector3f(containerWidth, 30, 0));
        container.addChild(lblDefense);

        guiNode.attachChild(container);

        MenuGroup menuGroup = new MenuGroup(this::backToTeamsMenu);
        menuGroup.addElement(new MenuElement(container, this::openGameSettingsMenu));
        addMenuGroup(menuGroup);
    }

    private void backToTeamsMenu() {
        close();
        mainApplication.getStateManager().attach(new TeamsMenuAppState());
    }

    private void openGameSettingsMenu() {
        close();
        mainApplication.getStateManager().attach(new GameSettingsMenuAppState());
    }
}
