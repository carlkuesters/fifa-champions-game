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
