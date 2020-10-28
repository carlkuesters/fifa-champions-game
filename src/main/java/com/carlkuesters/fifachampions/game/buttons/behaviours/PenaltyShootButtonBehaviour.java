package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.PlayerObject;

public class PenaltyShootButtonBehaviour extends ApproachShootButtonBehaviour {

    @Override
    protected void shoot(PlayerObject playerObject, float strength) {
        // TODO: 1 in 3 chance
        playerObject.shoot(strength);
    }
}
