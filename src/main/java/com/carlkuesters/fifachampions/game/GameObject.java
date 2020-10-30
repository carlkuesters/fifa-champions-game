package com.carlkuesters.fifachampions.game;

public class GameObject implements GameLoopListener {

    protected Game game;

    @Override
    public void update(float tpf) {
        
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
