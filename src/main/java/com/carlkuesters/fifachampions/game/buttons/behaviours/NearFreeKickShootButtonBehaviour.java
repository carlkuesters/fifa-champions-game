package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.Ball;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.situations.NearFreeKickSituation;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class NearFreeKickShootButtonBehaviour extends ApproachShootButtonBehaviour<NearFreeKickSituation> {

    @Override
    protected void shoot(NearFreeKickSituation nearFreeKickSituation, PlayerObject playerObject, float strength) {
        float optimalStrength = nearFreeKickSituation.getOptimalShootStrength();
        // TODO: Player dependent
        float maximumShootVelocity = 29;
        Ball ball = playerObject.getGame().getBall();
        float optimalBallVelocityX = nearFreeKickSituation.getTargetInGoalPosition()
                .subtract(ball.getPosition())
                .normalizeLocal()
                .multLocal(optimalStrength * maximumShootVelocity)
                .getX();
        Vector3f optimalBallVelocity = ball.getInitialVelocity_ByTargetVelocityX(nearFreeKickSituation.getTargetInGoalPosition(), optimalBallVelocityX);
        float actualVelocityFactor = FastMath.pow(2, strength - optimalStrength);
        Vector3f actualBallVelocity = optimalBallVelocity.normalize().multLocal(actualVelocityFactor * maximumShootVelocity);
        playerObject.shoot(actualBallVelocity);
    }
}
