package com.carlkuesters.fifachampions.game.buttons.behaviours;

public class GoalkeeperPressureButtonBehaviour extends InstantButtonBehaviour {

    @Override
    protected void onTrigger(boolean isPressed) {
        controller.getTeam().getGoalkeeper().setIsPressuring(isPressed);
    }
}
