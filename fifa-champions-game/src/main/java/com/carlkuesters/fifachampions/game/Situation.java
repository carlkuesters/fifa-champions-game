package com.carlkuesters.fifachampions.game;

public abstract class Situation implements GameLoopListener {

    protected Game game;

    public abstract void start();

    public void customizePlayerSelection() {

    }

    @Override
    public void update(float tpf) {

    }

    public void setGame(Game game) {
        this.game = game;
    }
}
