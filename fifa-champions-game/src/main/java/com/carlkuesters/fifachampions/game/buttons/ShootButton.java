package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.buttons.behaviours.NearFreeKickShootButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.PenaltyShootButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ShootButtonBehaviour;
import com.carlkuesters.fifachampions.game.situations.*;

public class ShootButton extends DefaultBallActionButton {

    public ShootButton(Controller controller) {
        super(controller);
        ShootButtonBehaviour shootButtonBehaviour = new ShootButtonBehaviour(controller);
        setNoSituationBehaviours(
            shootButtonBehaviour,
            shootButtonBehaviour,
            shootButtonBehaviour,
            null
        );
        setBehaviourWithOwnedBall(KickOffSituation.class, shootButtonBehaviour);
        setBehaviourWithOwnedBall(FarFreeKickSituation.class, shootButtonBehaviour);
        setBehaviourWithOwnedBall(NearFreeKickSituation.class, new NearFreeKickShootButtonBehaviour(controller));
        setBehaviourWithOwnedBall(PenaltySituation.class, new PenaltyShootButtonBehaviour(controller));
    }
}
