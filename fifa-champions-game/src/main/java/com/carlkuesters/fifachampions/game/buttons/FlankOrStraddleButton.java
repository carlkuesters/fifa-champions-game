package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.buttons.behaviours.FlankButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.GoalKickFlankButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.StraddleButtonBehaviour;
import com.carlkuesters.fifachampions.game.situations.CornerKickSituation;
import com.carlkuesters.fifachampions.game.situations.FarFreeKickSituation;
import com.carlkuesters.fifachampions.game.situations.GoalKickSituation;

public class FlankOrStraddleButton extends DefaultBallActionButton {

    public FlankOrStraddleButton(Controller controller) {
        super(controller);
        FlankButtonBehaviour flankButtonBehaviour = new FlankButtonBehaviour(controller);
        StraddleButtonBehaviour straddleButtonBehaviour = new StraddleButtonBehaviour(controller);
        setNoSituationBehaviours(
            straddleButtonBehaviour,
            flankButtonBehaviour,
            straddleButtonBehaviour,
            straddleButtonBehaviour
        );
        setBehaviourWithOwnedBall(CornerKickSituation.class, flankButtonBehaviour);
        setBehaviourWithOwnedBall(GoalKickSituation.class, new GoalKickFlankButtonBehaviour(controller));
        setBehaviourWithOwnedBall(FarFreeKickSituation.class, flankButtonBehaviour);
    }
}
