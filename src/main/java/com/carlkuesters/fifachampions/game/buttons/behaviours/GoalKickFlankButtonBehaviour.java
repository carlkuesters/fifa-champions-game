package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.situations.GoalKickSituation;

public class GoalKickFlankButtonBehaviour extends ApproachShootButtonBehaviour<GoalKickSituation> {

    @Override
    protected void shoot(GoalKickSituation goalKickSituation, PlayerObject playerObject, float strength) {
        playerObject.setDirection(goalKickSituation.getTargetDirection());
        playerObject.flank(strength);
    }
}
