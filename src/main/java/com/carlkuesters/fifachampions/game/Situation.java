package com.carlkuesters.fifachampions.game;

public abstract class Situation {

    protected Game game;

    public abstract void start();

    public void setGame(Game game) {
        this.game = game;
    }
}
