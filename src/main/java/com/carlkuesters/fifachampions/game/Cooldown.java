/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game;

/**
 *
 * @author Carl
 */
public abstract class Cooldown implements GameLoopListener {

    public Cooldown(float remainingTime) {
        this.remainingTime = remainingTime;
    }
    private float remainingTime;

    @Override
    public void update(float tpf) {
        remainingTime -= tpf;
    }
    
    public boolean isFinished() {
        return (remainingTime <= 0);
    }

    public abstract boolean equals(Cooldown cooldown);
}
