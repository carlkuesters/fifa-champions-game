package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.PlayerAnimation;

public class PassDirectButtonBehaviour extends ChargedBallButtonBehaviour {

    public PassDirectButtonBehaviour(Controller controller) {
        super(controller);
    }

    @Override
    public void onPressed(boolean isPressed) {
        super.onPressed(isPressed);
        if (isPressed && controller.getPlayerObject().isOwningBall()) {
            controller.getPlayerObject().setAnimation(new PlayerAnimation("short_pass_start", maxChargedDuration));
        }
    }

    @Override
    protected void onTrigger(float strength) {
        controller.getPlayerObject().turnIntoControllerTargetDirection();
        controller.getPlayerObject().passDirect(strength);
    }
}
