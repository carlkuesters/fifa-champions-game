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
public abstract class ControllerButton {
    
    protected Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public abstract ControllerButtonBehaviour getBehaviour();
}
