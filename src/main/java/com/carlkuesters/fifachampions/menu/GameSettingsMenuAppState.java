package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameAppState;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;

public class GameSettingsMenuAppState extends MenuAppState {

    private int containerWidth = 600;

    @Override
    protected void initMenu() {
        addTitle("Einstellungen");

        ElementsMenuGroup menuGroup = new ElementsMenuGroup(() -> openMenu(TrikotMenuAppState.class));

        int containerX = ((totalWidth / 2) - (containerWidth / 2));
        int containerY = (totalHeight - 200);
        Container container = new Container();
        container.setLocalTranslation(containerX, containerY, 0);

        addButton(container, menuGroup, "Spiel starten", this::startGame);
        addButton(container, menuGroup, "Aufstellung", () -> openMenu(InitialFormationMenuAppState.class));
        addButton(container, menuGroup, "Eintrag #3", this::startGame);
        addButton(container, menuGroup, "Eintrag #4", this::startGame);
        addButton(container, menuGroup, "Eintrag #5", this::startGame);
        addButton(container, menuGroup, "Eintrag #6", this::startGame);

        guiNode.attachChild(container);

        addMenuGroup(menuGroup);
    }

    private void addButton(Container container, ElementsMenuGroup menuGroup, String text, Runnable action) {
        Button button = new Button(text);
        button.setFontSize(20);
        button.setTextHAlignment(HAlignment.Center);
        button.setPreferredSize(new Vector3f(containerWidth, 30, 0));
        container.addChild(button);
        menuGroup.addElement(new MenuElement(button, action));
    }

    private void startGame() {
        close();
        mainApplication.getStateManager().attach(new GameAppState());
    }
}
