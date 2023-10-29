package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.PlayerAnimation;
import com.carlkuesters.fifachampions.game.PlayerObject;

public class ShootButtonBehaviour extends ChargedBallButtonBehaviour {

    public ShootButtonBehaviour(Controller controller) {
        super(controller);
    }

    @Override
    protected void onBallOwnerPressed() {
        super.onBallOwnerPressed();
        controller.getPlayerObject().setAnimation(new PlayerAnimation("run_kick_start", maxChargedDuration));
    }

    @Override
    protected void onTrigger(float strength) {
        PlayerObject playerObject = controller.getPlayerObject();
        playerObject.turnIntoControllerTargetDirection();
        if (playerObject.getGame().getBall().getPosition().getY() > 1) {
            playerObject.header(strength);
            playerObject.setAnimation(new PlayerAnimation("header_end", 0.5f));
        } else {
            playerObject.shoot(strength);
            playerObject.setAnimation(new PlayerAnimation("run_kick_end", 1));
        }
    }
}
