package com.carlkuesters.fifachampions.game;

import lombok.Getter;

@Getter
public class PlayerAnimation implements GameLoopListener {

    public PlayerAnimation(String name, float duration) {
        this(name, duration, false);
    }

    public PlayerAnimation(String name, float duration, boolean loop) {
        this.name = name;
        this.duration = duration;
        this.loop = loop;
    }
    private String name;
    private float duration;
    private boolean loop;
    private float time;

    @Override
    public void update(float tpf) {
        time += tpf;
        if (loop) {
            while (time >= duration) {
                time -= duration;
            }
        }
    }

    public boolean isFinished() {
        return !loop && (time >= duration);
    }

    public PlayerAnimation cloneWithRandomOffset() {
        PlayerAnimation clone = new PlayerAnimation(name, duration, loop);
        clone.time = (float) (Math.random() * duration);
        return clone;
    }
}
