package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.PlayerObject;

public class GoalKickFlankButtonBehaviour extends ApproachShootButtonBehaviour {

    @Override
    protected void shoot(PlayerObject playerObject, float strength) {
        // TODO: Consider camera rotation?
        playerObject.flank(strength);
    }
}
