package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.PlayerAnimation;

public class FlankButtonBehaviour extends ChargedBallButtonBehaviour {

    public FlankButtonBehaviour(Controller controller) {
        super(controller);
    }

    @Override
    public void onPressed(boolean isPressed) {
        super.onPressed(isPressed);
        if (isPressed) {
            controller.getPlayerObject().setAnimation(new PlayerAnimation("run_kick_start", maxChargedDuration));
        }
    }

    @Override
    protected void onTrigger(float strength) {
        controller.getPlayerObject().setAnimation(new PlayerAnimation("run_kick_end", 1));
        controller.getPlayerObject().turnIntoControllerTargetDirection();
        controller.getPlayerObject().flank(strength);
    }
}
