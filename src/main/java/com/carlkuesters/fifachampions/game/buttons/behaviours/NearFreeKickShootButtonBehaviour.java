package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.situations.NearFreeKickSituation;

public class NearFreeKickShootButtonBehaviour extends ApproachShootButtonBehaviour<NearFreeKickSituation> {

    @Override
    protected void shoot(NearFreeKickSituation nearFreeKickSituation, PlayerObject playerObject, float strength) {
        // TODO: near free kick curve logic
        playerObject.shoot(strength);
    }
}
