package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.Controller;

public class GoalkeeperPressureButtonBehaviour extends InstantButtonBehaviour {

    public GoalkeeperPressureButtonBehaviour(Controller controller) {
        super(controller);
    }

    @Override
    protected void onTrigger(boolean isPressed) {
        controller.getTeam().getGoalkeeper().setIsPressuring(isPressed);
    }
}
