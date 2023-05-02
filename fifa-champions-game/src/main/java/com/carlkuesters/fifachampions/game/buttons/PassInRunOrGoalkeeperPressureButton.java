package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.buttons.behaviours.GoalkeeperPressureButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.PassInRunButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ThrowInInRunButtonBehaviour;
import com.carlkuesters.fifachampions.game.situations.CornerKickSituation;
import com.carlkuesters.fifachampions.game.situations.FarFreeKickSituation;
import com.carlkuesters.fifachampions.game.situations.KickOffSituation;
import com.carlkuesters.fifachampions.game.situations.ThrowInSituation;

public class PassInRunOrGoalkeeperPressureButton extends DefaultBallActionButton {

    public PassInRunOrGoalkeeperPressureButton(Controller controller) {
        super(controller);
        PassInRunButtonBehaviour passInRunButtonBehaviour = new PassInRunButtonBehaviour(controller);
        GoalkeeperPressureButtonBehaviour goalkeeperPressureButtonBehaviour = new GoalkeeperPressureButtonBehaviour(controller);
        setNoSituationBehaviours(
            goalkeeperPressureButtonBehaviour,
            passInRunButtonBehaviour,
            goalkeeperPressureButtonBehaviour,
            goalkeeperPressureButtonBehaviour
        );
        setBehaviourWithOwnedBall(KickOffSituation.class, passInRunButtonBehaviour);
        setBehaviourWithOwnedBall(FarFreeKickSituation.class, passInRunButtonBehaviour);
        setBehaviourWithOwnedBall(CornerKickSituation.class, passInRunButtonBehaviour);
        setBehaviourWithOwnedBall(ThrowInSituation.class, new ThrowInInRunButtonBehaviour(controller));
    }
}
