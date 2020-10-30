package com.carlkuesters.fifachampions.game;

import com.jme3.animation.LoopMode;

public class PlayerAnimation implements GameLoopListener {

    public PlayerAnimation(String name, float loopDuration) {
        this(name, loopDuration, LoopMode.Loop);
    }

    public PlayerAnimation(String name, float loopDuration, LoopMode loopMode) {
        this.name = name;
        this.loopDuration = loopDuration;
        this.remainingDuration = loopDuration;
        this.loopMode = loopMode;
    }
    private String name;
    private float loopDuration;
    private float remainingDuration;
    private LoopMode loopMode;

    @Override
    public void update(float tpf) {
        remainingDuration -= tpf;
    }

    public String getName() {
        return name;
    }

    public float getLoopDuration() {
        return loopDuration;
    }

    public LoopMode getLoopMode() {
        return loopMode;
    }

    public boolean isFinished() {
        return ((remainingDuration <= 0) && (loopMode != LoopMode.DontLoop));
    }
}
