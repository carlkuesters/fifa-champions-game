package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.PlayerObject;

public class NearFreeKickShootButtonBehaviour extends ApproachShootButtonBehaviour {

    @Override
    protected void shoot(PlayerObject playerObject, float strength) {
        // TODO: near free kick curve logic
        playerObject.shoot(strength);
    }
}
