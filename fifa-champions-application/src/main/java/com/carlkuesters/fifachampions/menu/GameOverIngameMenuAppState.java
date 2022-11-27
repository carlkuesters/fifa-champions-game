package com.carlkuesters.fifachampions.menu;

import com.jme3.math.Vector3f;

public class GameOverIngameMenuAppState extends IngameMenuAppState {

    @Override
    protected void initMenu() {
        super.initMenu();
        int buttonWidth = ((totalWidth / 2) - (marginBetween / 2) - marginX);
        int buttonHeight = ((containerHeight - marginBetween) / 2);

        int buttonX = ((totalWidth / 2) + (marginBetween / 2));
        int buttonY1 = yStart;
        int buttonY2 = yStart - buttonHeight - marginBetween;
        addButton(new Vector3f(buttonX, buttonY1, 0), buttonWidth, buttonHeight, "Revanche", this::endGame);
        addButton(new Vector3f(buttonX, buttonY2, 0), buttonWidth, buttonHeight, "Beenden", this::endGame);
    }
}
