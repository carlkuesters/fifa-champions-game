package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameAppState;
import com.jme3.math.Vector3f;

public class PauseIngameMenuAppState extends IngameMenuAppState {

    @Override
    protected void initMenu() {
        super.initMenu();
        int buttonWidth = (((totalWidth / 2) - (marginBetween / 2) - marginBetween - marginX) / 2);
        int buttonHeight = ((containerHeight - (2 * marginBetween)) / 3);

        int buttonX1 = ((totalWidth / 2) + (marginBetween / 2));
        int buttonX2 = (buttonX1 + buttonWidth + marginBetween);
        int buttonY1 = yStart;
        int buttonY2 = yStart - buttonHeight - marginBetween;
        int buttonY3 = yStart - (2 * (buttonHeight + marginBetween));
        addButton(new Vector3f(buttonX1, buttonY1, 0), buttonWidth, buttonHeight, "Fortsetzen", this::back);
        addButton(new Vector3f(buttonX2, buttonY1, 0), buttonWidth, buttonHeight, "Wiederholung", () -> openMenu(ReplayMenuAppState.class));
        addButton(new Vector3f(buttonX1, buttonY2, 0), buttonWidth, buttonHeight, "Aufstellung", () -> openMenu(PauseFormationMenuAppState.class));
        addButton(new Vector3f(buttonX2, buttonY2, 0), buttonWidth, buttonHeight, "Seitenwahl", () -> openMenu(PauseSideSelectionMenuAppState.class));
        addButton(new Vector3f(buttonX1, buttonY3, 0), buttonWidth, buttonHeight, "Einstellungen", () -> openMenu(MainSettingsMenuAppState.class));
        addButton(new Vector3f(buttonX2, buttonY3, 0), buttonWidth, buttonHeight, "Beenden", this::endGame);
    }

    @Override
    protected void back() {
        super.back();
        getAppState(GameAppState.class).setPaused(false);
    }
}
