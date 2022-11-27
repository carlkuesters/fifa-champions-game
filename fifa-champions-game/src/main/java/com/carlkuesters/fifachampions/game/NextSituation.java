package com.carlkuesters.fifachampions.game;

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
