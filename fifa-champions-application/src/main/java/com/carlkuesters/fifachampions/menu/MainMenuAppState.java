package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.cinematics.CinematicAppState;
import com.carlkuesters.fifachampions.cinematics.cinematics.MainMenuCinematic;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.IconComponent;

public class MainMenuAppState extends MenuAppState {

    public MainMenuAppState() {
        mode = MenuMode.ROOT;
        autoEnabled = true;
        mainMenuCinematic = new MainMenuCinematic();
    }
    private int buttonWidth;
    private int buttonHeight;
    private ElementsMenuGroup menuGroup;
    private MainMenuCinematic mainMenuCinematic;

    @Override
    protected void initMenu() {
        int marginX = 100;
        int buttonsMarginBottom = 100;
        int buttonsMarginBetween = 20;
        buttonWidth = ((totalWidth - (2 * marginX) - buttonsMarginBetween) / 2);
        buttonHeight = (((totalHeight / 2) - buttonsMarginBottom - buttonsMarginBetween) / 2);

        int logoMarginY = 50;
        int logoWidth = (totalWidth - (2 * marginX));
        int logoHeight = ((totalHeight / 2) - (2 * logoMarginY));
        Panel logo = new Panel();
        IconComponent logoIcon = new IconComponent("textures/header.png");
        logoIcon.setIconSize(new Vector2f(logoWidth, logoHeight));
        logo.setBackground(logoIcon);
        logo.setLocalTranslation(new Vector3f(marginX, totalHeight - logoMarginY, 0));
        guiNode.attachChild(logo);

        menuGroup = new ElementsMenuGroup();
        addButton(new Vector3f(marginX, (totalHeight / 2f), 0), "Anstoß", () -> openMenu(InitialSideSelectionMenuAppState.class));
        addButton(new Vector3f(marginX + buttonWidth + buttonsMarginBetween, (totalHeight / 2f), 0), "Steuerung", () -> openMenu(ControllerSettingsMenuAppState.class));
        addButton(new Vector3f(marginX, (totalHeight / 2f) - buttonHeight - buttonsMarginBetween, 0), "Einstellungen", () -> openMenu(MainSettingsMenuAppState.class));
        addButton(new Vector3f(marginX + buttonWidth + buttonsMarginBetween, (totalHeight / 2f) - buttonHeight - buttonsMarginBetween, 0), "Beenden", () -> mainApplication.stop());
        addMenuGroup(menuGroup);
    }

    private void addButton(Vector3f position, String text, Runnable action) {
        Button button = new Button(text);
        button.setLocalTranslation(position);
        button.setPreferredSize(new Vector3f(buttonWidth, buttonHeight, 0));
        button.setTextHAlignment(HAlignment.Center);
        button.setTextVAlignment(VAlignment.Center);
        button.setFontSize(20);
        guiNode.attachChild(button);
        menuGroup.addElement(new MenuElement(button, action));
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        CinematicAppState cinematicAppState = getAppState(CinematicAppState.class);
        if (enabled && (cinematicAppState.getCurrentCinematic() != mainMenuCinematic)) {
            cinematicAppState.playCinematic(mainMenuCinematic);
        }
    }

    @Override
    protected void back() {
        // There is no back in main menu :)
    }
}
