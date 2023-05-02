package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.buttons.behaviours.PassDirectButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.PressureButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ThrowInDirectButtonBehaviour;
import com.carlkuesters.fifachampions.game.situations.CornerKickSituation;
import com.carlkuesters.fifachampions.game.situations.FarFreeKickSituation;
import com.carlkuesters.fifachampions.game.situations.KickOffSituation;
import com.carlkuesters.fifachampions.game.situations.ThrowInSituation;

public class PassDirectOrPressureButton extends DefaultBallActionButton {

    public PassDirectOrPressureButton(Controller controller) {
        super(controller);
        PassDirectButtonBehaviour passDirectButtonBehaviour = new PassDirectButtonBehaviour(controller);
        setNoSituationBehaviours(
            passDirectButtonBehaviour,
            passDirectButtonBehaviour,
            passDirectButtonBehaviour,
            new PressureButtonBehaviour(controller)
        );
        setBehaviourWithOwnedBall(KickOffSituation.class, passDirectButtonBehaviour);
        setBehaviourWithOwnedBall(FarFreeKickSituation.class, passDirectButtonBehaviour);
        setBehaviourWithOwnedBall(CornerKickSituation.class, passDirectButtonBehaviour);
        setBehaviourWithOwnedBall(ThrowInSituation.class, new ThrowInDirectButtonBehaviour(controller));
    }
}
