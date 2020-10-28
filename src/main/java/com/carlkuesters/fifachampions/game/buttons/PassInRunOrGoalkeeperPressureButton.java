package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.buttons.behaviours.GoalkeeperPressureButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.PassInRunButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ThrowInInRunButtonBehaviour;
import com.carlkuesters.fifachampions.game.situations.CornerKickSituation;
import com.carlkuesters.fifachampions.game.situations.FarFreeKickSituation;
import com.carlkuesters.fifachampions.game.situations.KickOffSituation;
import com.carlkuesters.fifachampions.game.situations.ThrowInSituation;

public class PassInRunOrGoalkeeperPressureButton extends DefaultBallActionButton {

    public PassInRunOrGoalkeeperPressureButton() {
        PassInRunButtonBehaviour passInRunButtonBehaviour = new PassInRunButtonBehaviour();
        GoalkeeperPressureButtonBehaviour goalkeeperPressureButtonBehaviour = new GoalkeeperPressureButtonBehaviour();
        setNoSituationBehaviours(
            goalkeeperPressureButtonBehaviour,
            passInRunButtonBehaviour,
            goalkeeperPressureButtonBehaviour,
            goalkeeperPressureButtonBehaviour
        );
        setBehaviourWithOwnedBall(KickOffSituation.class, passInRunButtonBehaviour);
        setBehaviourWithOwnedBall(FarFreeKickSituation.class, passInRunButtonBehaviour);
        setBehaviourWithOwnedBall(CornerKickSituation.class, passInRunButtonBehaviour);
        setBehaviourWithOwnedBall(ThrowInSituation.class, new ThrowInInRunButtonBehaviour());
    }
}
