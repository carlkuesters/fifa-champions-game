package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.buttons.behaviours.FlankButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.GoalKickFlankButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.StraddleButtonBehaviour;
import com.carlkuesters.fifachampions.game.situations.CornerKickSituation;
import com.carlkuesters.fifachampions.game.situations.FarFreeKickSituation;
import com.carlkuesters.fifachampions.game.situations.GoalKickSituation;

public class FlankOrStraddleButton extends DefaultBallActionButton {

    public FlankOrStraddleButton() {
        FlankButtonBehaviour flankButtonBehaviour = new FlankButtonBehaviour();
        StraddleButtonBehaviour straddleButtonBehaviour = new StraddleButtonBehaviour();
        setNoSituationBehaviours(
            straddleButtonBehaviour,
            flankButtonBehaviour,
            straddleButtonBehaviour,
            straddleButtonBehaviour
        );
        setBehaviourWithOwnedBall(CornerKickSituation.class, flankButtonBehaviour);
        setBehaviourWithOwnedBall(GoalKickSituation.class, new GoalKickFlankButtonBehaviour());
        setBehaviourWithOwnedBall(FarFreeKickSituation.class, flankButtonBehaviour);
    }
}
