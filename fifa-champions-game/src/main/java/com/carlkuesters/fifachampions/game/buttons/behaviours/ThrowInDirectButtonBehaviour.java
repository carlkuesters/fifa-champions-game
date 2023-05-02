package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.PlayerAnimation;

public class ThrowInDirectButtonBehaviour extends ChargedBallButtonBehaviour {

    public ThrowInDirectButtonBehaviour(Controller controller) {
        super(controller);
    }

    @Override
    public void onPressed(boolean isPressed) {
        super.onPressed(isPressed);
        if (isPressed) {
            controller.getPlayerObject().setAnimation(new PlayerAnimation("throw_in_start", maxChargedDuration));
        }
    }

    @Override
    protected void onTrigger(float strength) {
        float freezeTime = 0.5f;
        controller.getPlayerObject().setAnimation(new PlayerAnimation("throw_in_end", freezeTime));
        controller.getPlayerObject().freeze(freezeTime);
        controller.getPlayerObject().turnIntoControllerTargetDirection();
        controller.getPlayerObject().throwInDirect(strength);
    }
}
