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
    protected void onNotBallOwnerPressed() {
        super.onNotBallOwnerPressed();
        if (controller.isTrickShooting()) {
            int bicycleStartFrames = 66 - 46;
            int bicycleStartToEndFrames = 135 - 46;
            // The animation is precisely so long, that the kick happens at max charge duration
            float freezeTime = (maxChargedDuration * (((float) bicycleStartToEndFrames) / bicycleStartFrames));
            controller.getPlayerObject().setAnimation(new PlayerAnimation("bicycle_kick", freezeTime));
            controller.getPlayerObject().freeze(freezeTime);
            controller.getPlayerObject().turnIntoControllerTargetDirection();
            controller.getPlayerObject().setTrickShooting(true);
        }
    }

    @Override
    protected void onTrigger(float strength) {
        PlayerObject playerObject = controller.getPlayerObject();
        playerObject.turnIntoControllerTargetDirection();
        if (playerObject.isTrickShooting()) {
            float freezeTime = 2;
            playerObject.setAnimation(new PlayerAnimation("bicycle_kick_end", freezeTime));
            playerObject.freeze(freezeTime);
            playerObject.shoot(strength);
            playerObject.setTrickShooting(false);
        } else if (playerObject.getGame().getBall().getPosition().getY() > 1) {
            playerObject.header(strength);
            playerObject.setAnimation(new PlayerAnimation("header_end", 0.5f));
        } else {
            playerObject.shoot(strength);
            playerObject.setAnimation(new PlayerAnimation("run_kick_end", 1));
        }
    }
}
