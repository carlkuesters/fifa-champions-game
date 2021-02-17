package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameAppState;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;

public class GameSettingsMenuAppState extends MenuAppState {

    @Override
    protected void initMenu() {
        addTitle("Einstellungen");

        ElementsMenuGroup menuGroup = new ElementsMenuGroup(() -> openMenu(ShirtMenuAppState.class));

        int containerWidth = 600;
        int containerX = ((totalWidth / 2) - (containerWidth / 2));
        int containerY = (totalHeight - 200);
        Container container = new Container();
        container.setLocalTranslation(containerX, containerY, 0);

        for (int i = 0; i < 6; i++) {
            Button button = new Button("Eintrag " + (i + 1));
            button.setFontSize(20);
            button.setTextHAlignment(HAlignment.Center);
            button.setPreferredSize(new Vector3f(containerWidth, 30, 0));
            container.addChild(button);
            menuGroup.addElement(new MenuElement(button, this::startGame));
        }

        guiNode.attachChild(container);

        addMenuGroup(menuGroup);
    }

    private void startGame() {
        close();
        mainApplication.getStateManager().attach(new GameAppState());
    }
}
