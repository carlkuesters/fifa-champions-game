/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game.cooldowns;

import com.carlkuesters.fifachampions.game.Cooldown;
import com.carlkuesters.fifachampions.game.PlayerObject;

/**
 *
 * @author Carl
 */
public class UnownedBallPickupCooldown extends Cooldown{

    public UnownedBallPickupCooldown(PlayerObject playerObject) {
        super(1);
        this.playerObject = playerObject;
    }
    private PlayerObject playerObject;

    @Override
    public boolean equals(Cooldown cooldown) {
        if (cooldown instanceof UnownedBallPickupCooldown) {
            UnownedBallPickupCooldown ballOwnerCooldown = (UnownedBallPickupCooldown) cooldown;
            return (playerObject == ballOwnerCooldown.playerObject);
        }
        return false;
    }
}
