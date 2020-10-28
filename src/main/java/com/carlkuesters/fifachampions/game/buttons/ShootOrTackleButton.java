package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.buttons.behaviours.NearFreeKickShootButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.PenaltyShootButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ShootButtonBehaviour;
import com.carlkuesters.fifachampions.game.situations.*;

public class ShootOrTackleButton extends DefaultBallActionButton {

    public ShootOrTackleButton() {
        ShootButtonBehaviour shootButtonBehaviour = new ShootButtonBehaviour();
        setNoSituationBehaviours(
            shootButtonBehaviour,
            shootButtonBehaviour,
            shootButtonBehaviour,
            null
        );
        setBehaviourWithOwnedBall(KickOffSituation.class, shootButtonBehaviour);
        setBehaviourWithOwnedBall(FarFreeKickSituation.class, shootButtonBehaviour);
        setBehaviourWithOwnedBall(NearFreeKickSituation.class, new NearFreeKickShootButtonBehaviour());
        setBehaviourWithOwnedBall(PenaltySituation.class, new PenaltyShootButtonBehaviour());
    }
}
