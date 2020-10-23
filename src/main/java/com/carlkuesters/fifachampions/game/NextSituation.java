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
public class NextSituation implements GameLoopListener {

    public NextSituation(Situation situation, float remainingTime, boolean isBallUnowned) {
        this.situation = situation;
        this.remainingTime = remainingTime;
        this.isBallUnowned = isBallUnowned;
    }
    private Situation situation;
    private float remainingTime;
    private boolean isBallUnowned;

    @Override
    public void update(float tpf) {
        remainingTime -= tpf;
    }

    public boolean isBallUnowned() {
        return isBallUnowned;
    }

    public boolean hasStarted() {
        return (remainingTime <= 0);
    }

    public Situation getSituation() {
        return situation;
    }
}
