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
        IconComponent logoIconComponent = new IconComponent("textures/logo.png");
        logoIconComponent.setIconSize(new Vector2f(logoWidth, logoHeight));
        logo.setBackground(logoIconComponent);
        logo.setLocalTranslation(new Vector3f(marginX, totalHeight - logoMarginY, 0));
        guiNode.attachChild(logo);

        menuGroup = new ElementsMenuGroup();
        addButton(new Vector3f(marginX, (totalHeight / 2f), 0), "Anstoß", () -> openMenu(InitialSideSelectionMenuAppState.class));
        addButton(new Vector3f(marginX + buttonWidth + buttonsMarginBetween, (totalHeight / 2f), 0), "Einstellungen", () -> System.out.println("Einstellungen"));
        addButton(new Vector3f(marginX, (totalHeight / 2f) - buttonHeight - buttonsMarginBetween, 0), "Test", () -> System.out.println("Test"));
        addButton(new Vector3f(marginX + buttonWidth + buttonsMarginBetween, (totalHeight / 2f) - buttonHeight - buttonsMarginBetween, 0), "Beenden", this::closeApplication);
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
        // TODO: Why do I have to cast here?
        CinematicAppState cinematicAppState = (CinematicAppState) getAppState(CinematicAppState.class);
        if (enabled) {
            cinematicAppState.playCinematic(mainMenuCinematic);
        } else {
            cinematicAppState.stopCinematic();
        }
    }

    private void closeApplication() {
        System.exit(0);
    }

    @Override
    protected void back() {
        // THere is no back in main menu :)
    }
}
