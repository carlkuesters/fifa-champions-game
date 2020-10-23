/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game;

import com.jme3.animation.LoopMode;

/**
 *
 * @author Carl
 */
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
        return (remainingDuration <= 0);
    }
}
