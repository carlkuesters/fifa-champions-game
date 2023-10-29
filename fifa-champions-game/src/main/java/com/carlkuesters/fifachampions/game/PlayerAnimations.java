package com.carlkuesters.fifachampions.game;

public class PlayerAnimations {

    public static PlayerAnimation createRunFast() {
        return new PlayerAnimation("run_fast", 0.7f, true);
    }

    public static PlayerAnimation createRunMedium() {
        return new PlayerAnimation("run_medium", 1.17f, true);
    }

    public static PlayerAnimation createRunSlow() {
        return new PlayerAnimation("run_slow", 1.59f, true);
    }

    public static PlayerAnimation createIdle() {
        return new PlayerAnimation("idle", 4, true);
    }
}
