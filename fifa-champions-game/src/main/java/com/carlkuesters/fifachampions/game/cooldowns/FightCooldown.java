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
public class FightCooldown extends Cooldown{

    public FightCooldown(PlayerObject playerObject1, PlayerObject playerObject2) {
        super(2);
        this.playerObject1 = playerObject1;
        this.playerObject2 = playerObject2;
    }
    private PlayerObject playerObject1;
    private PlayerObject playerObject2;

    @Override
    public boolean equals(Cooldown cooldown) {
        if (cooldown instanceof FightCooldown) {
            FightCooldown fightCooldown = (FightCooldown) cooldown;
            return ((playerObject1 == fightCooldown.playerObject1) && (playerObject2 == fightCooldown.playerObject2))
                || ((playerObject1 == fightCooldown.playerObject2) && (playerObject2 == fightCooldown.playerObject1));
        }
        return false;
    }
}
