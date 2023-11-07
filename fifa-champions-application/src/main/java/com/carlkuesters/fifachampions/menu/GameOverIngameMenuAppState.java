package com.carlkuesters.fifachampions.menu;

import com.jme3.math.Vector3f;

public class GameOverIngameMenuAppState extends IngameMenuAppState {

    @Override
    protected void initMenu() {
        super.initMenu();
        int buttonWidth = ((totalWidth / 2) - (marginBetween / 2) - marginX);
        int buttonHeight = ((containerHeight - (2 * marginBetween)) / 3);

        int buttonX = ((totalWidth / 2) + (marginBetween / 2));
        int buttonY1 = yStart;
        int buttonY2 = yStart - (buttonHeight + marginBetween);
        int buttonY3 = yStart - (2 * (buttonHeight + marginBetween));
        addButton(new Vector3f(buttonX, buttonY1, 0), buttonWidth, buttonHeight, "Revanche", this::endGame);
        addButton(new Vector3f(buttonX, buttonY2, 0), buttonWidth, buttonHeight, "Wiederholung", () -> openMenu(ReplayMenuAppState.class));
        addButton(new Vector3f(buttonX, buttonY3, 0), buttonWidth, buttonHeight, "Beenden", this::endGame);
    }

    @Override
    protected void back() {
        // There is no back in game over menu :)
    }
}
