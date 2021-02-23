package com.carlkuesters.fifachampions.game;

import java.util.function.Function;

public class PlayerSkillUtil {

    public static float getValue(float worstValue, float bestValue, int skill) {
        return (worstValue + ((skill / 100f) * (bestValue - worstValue)));
    }

    public static boolean isWinning(Player player1, Player player2, Function<Player, Integer> getSkill) {
        int skill1 = getSkill.apply(player1);
        int skill2 = getSkill.apply(player2);
        return (Math.random() < (((float) skill1) / (skill1 + skill2)));
    }
}
