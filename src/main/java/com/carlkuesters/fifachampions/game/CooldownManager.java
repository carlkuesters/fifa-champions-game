package com.carlkuesters.fifachampions.game;

import java.util.ArrayList;

public class CooldownManager implements GameLoopListener{
    
    private ArrayList<Cooldown> cooldowns = new ArrayList<>();

    public boolean isNotOnCooldown(Cooldown cooldown) {
        return cooldowns.stream().noneMatch(activeCooldown -> activeCooldown.equals(cooldown));
    }

    public void putOnCooldown(Cooldown cooldown) {
        for (int i = 0; i < cooldowns.size(); i++) {
            Cooldown activeCooldown = cooldowns.get(i);
            if (activeCooldown.equals(cooldown)) {
                cooldowns.set(i, cooldown);
                return;
            }
        }
        cooldowns.add(cooldown);
    }

    @Override
    public void update(float tpf) {
        for (int i = 0; i < cooldowns.size(); i++) {
            Cooldown cooldown = cooldowns.get(i);
            cooldown.update(tpf);
            if (cooldown.isFinished()) {
                cooldowns.remove(i);
                i--;
            }
        }
    }
}
