package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.situations.PenaltySituation;

public class PenaltyShootButtonBehaviour extends ApproachShootButtonBehaviour<PenaltySituation> {

    @Override
    protected void shoot(PenaltySituation penaltySituation, PlayerObject playerObject, float strength) {
        // TODO: 1 in 3 chance
        playerObject.shoot(strength);
    }
}
