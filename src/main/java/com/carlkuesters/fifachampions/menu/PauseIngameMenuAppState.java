package com.carlkuesters.fifachampions.menu;

import com.jme3.math.Vector3f;

public class PauseIngameMenuAppState extends IngameMenuAppState {

    @Override
    protected void initMenu() {
        super.initMenu();
        int buttonWidth = (((totalWidth / 2) - (marginBetween / 2) - marginBetween - marginX) / 2);
        int buttonHeight = ((containerHeight - marginBetween) / 2);

        int buttonX1 = ((totalWidth / 2) + (marginBetween / 2));
        int buttonX2 = (buttonX1 + buttonWidth + marginBetween);
        int buttonY1 = yStart;
        int buttonY2 = yStart - buttonHeight - marginBetween;
        addButton(new Vector3f(buttonX1, buttonY1, 0), buttonWidth, buttonHeight, "Fortsetzen", this::close);
        addButton(new Vector3f(buttonX2, buttonY1, 0), buttonWidth, buttonHeight, "Seitenwahl", () -> openMenu(TeamSelectionMenuAppState.class));
        addButton(new Vector3f(buttonX1, buttonY2, 0), buttonWidth, buttonHeight, "Aufstellung", () -> openMenu(FormationMenuAppState.class));
        addButton(new Vector3f(buttonX2, buttonY2, 0), buttonWidth, buttonHeight, "Beenden", this::endGame);
    }
}
