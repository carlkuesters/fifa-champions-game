package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.buttons.behaviours.PassDirectButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.PressureButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ThrowInDirectButtonBehaviour;
import com.carlkuesters.fifachampions.game.situations.CornerKickSituation;
import com.carlkuesters.fifachampions.game.situations.FarFreeKickSituation;
import com.carlkuesters.fifachampions.game.situations.KickOffSituation;
import com.carlkuesters.fifachampions.game.situations.ThrowInSituation;

public class PassDirectOrPressureButton extends DefaultBallActionButton {

    public PassDirectOrPressureButton() {
        PassDirectButtonBehaviour passDirectButtonBehaviour = new PassDirectButtonBehaviour();
        setNoSituationBehaviours(
            passDirectButtonBehaviour,
            passDirectButtonBehaviour,
            passDirectButtonBehaviour,
            new PressureButtonBehaviour()
        );
        setBehaviourWithOwnedBall(KickOffSituation.class, passDirectButtonBehaviour);
        setBehaviourWithOwnedBall(FarFreeKickSituation.class, passDirectButtonBehaviour);
        setBehaviourWithOwnedBall(CornerKickSituation.class, passDirectButtonBehaviour);
        setBehaviourWithOwnedBall(ThrowInSituation.class, new ThrowInDirectButtonBehaviour());
    }
}
