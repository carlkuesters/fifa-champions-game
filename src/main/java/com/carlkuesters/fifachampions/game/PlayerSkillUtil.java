package com.carlkuesters.fifachampions.game;

public class PlayerSkillUtil {

    public static float getValue(int skill, float minimum, float maximum) {
        return (minimum + ((skill / 100f) * maximum));
    }
}
