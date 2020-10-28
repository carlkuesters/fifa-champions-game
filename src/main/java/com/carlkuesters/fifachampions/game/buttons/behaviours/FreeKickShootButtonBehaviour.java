package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.EnqueuedAction;
import com.carlkuesters.fifachampions.game.PlayerAnimation;
import com.carlkuesters.fifachampions.game.PlayerObject;

public class FreeKickShootButtonBehaviour extends ChargedBallButtonBehaviour {

    @Override
    protected void onTrigger(float strength) {
        PlayerObject playerObject = controller.getPlayerObject();
        playerObject.setAnimation(new PlayerAnimation("stand_kick", 3.333f));
        playerObject.getGame().enqueue(new EnqueuedAction(() -> {
            playerObject.shoot(strength);
        }, 1.66f));
    }
}
