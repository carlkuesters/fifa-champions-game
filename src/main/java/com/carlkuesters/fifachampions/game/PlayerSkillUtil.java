package com.carlkuesters.fifachampions.game;

public class PlayerSkillUtil {

    public static float getValue(float worstValue, float bestValue, int skill) {
        return (worstValue + ((skill / 100f) * (bestValue - worstValue)));
    }
}
