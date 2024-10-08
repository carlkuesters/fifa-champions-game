package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.*;
import com.carlkuesters.fifachampions.game.situations.BallSituation;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public abstract class ApproachShootButtonBehaviour<T extends BallSituation> extends ChargedBallButtonBehaviour {

    public ApproachShootButtonBehaviour(Controller controller) {
        super(controller);
    }
    public static float APPROACH_SPEED = 2.767f;
    public static float APPROACH_DURATION = 1.2f;

    @Override
    protected void onTrigger(float strength) {
        PlayerObject playerObject = controller.getPlayerObject();
        T ballSituation = (T) playerObject.getGame().getSituation();
        playerObject.setCanMove(true);
        playerObject.setForcedSpeed(APPROACH_SPEED);
        // Adjust the target location according to the animation, so the foot exactly hits the ball visually
        // TODO: Left foots have negated angle
        Vector3f footOffset = new Quaternion().fromAngleAxis((true ? 1 : -1) * FastMath.HALF_PI, Vector3f.UNIT_Y).mult(playerObject.getDirection()).multLocal(0.4f);
        playerObject.setTargetLocation(MathUtil.convertTo2D_XZ(controller.getGame().getBall().getPosition().add(footOffset)));
        playerObject.setAnimation(new PlayerAnimation("stand_kick", 2.41f));
        playerObject.getGame().enqueue(new EnqueuedAction(() -> {
            playerObject.setForcedSpeed(null);
            playerObject.setTargetLocation(null);
            shoot(ballSituation, playerObject, strength);
        }, APPROACH_DURATION));
    }

    protected abstract void shoot(T ballSituation, PlayerObject playerObject, float strength);
}
