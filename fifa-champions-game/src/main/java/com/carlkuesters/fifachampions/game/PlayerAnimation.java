package com.carlkuesters.fifachampions.game;

import lombok.Getter;

public class PlayerAnimation implements GameLoopListener {

    public PlayerAnimation(String name, float loopDuration) {
        this(name, loopDuration, false);
    }

    public PlayerAnimation(String name, float loopDuration, boolean loop) {
        this.name = name;
        this.loopDuration = loopDuration;
        this.remainingDuration = loopDuration;
        this.loop = loop;
    }
    @Getter
    private String name;
    @Getter
    private float loopDuration;
    private float remainingDuration;
    @Getter
    private boolean loop;

    @Override
    public void update(float tpf) {
        remainingDuration -= tpf;
    }

    public boolean isFinished() {
        return !loop && (remainingDuration <= 0);
    }
}
