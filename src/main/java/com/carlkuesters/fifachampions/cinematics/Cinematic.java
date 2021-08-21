package com.carlkuesters.fifachampions.cinematics;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import lombok.Getter;

public class Cinematic {

    public Cinematic() {
        // Used if the parts are too complex to be directly set in the super constructor
    }

    public Cinematic(boolean loop, CinematicPart[] parts) {
        this.loop = loop;
        this.parts = parts;
    }
    @Getter
    protected boolean loop;
    protected CinematicPart[] parts;
    @Getter
    private SimpleApplication simpleApplication;
    protected Node rootNode;
    protected Node guiNode;
    private boolean initialized;
    private boolean started;
    private float time;

    public void reset(SimpleApplication simpleApplication) {
        if (!initialized) {
            initialize(simpleApplication);
        }
        time = 0;
        for (CinematicPart part : parts) {
            part.reset(this);
        }
    }

    protected void initialize(SimpleApplication simpleApplication) {
        this.simpleApplication = simpleApplication;
        rootNode = new Node();
        guiNode = new Node();
        initialized = true;
    }

    public void update(float lastTimePerFrame) {
        if (!started) {
            start();
        }
        time += lastTimePerFrame;
        for (CinematicPart part : parts) {
            CinematicAction action = part.getAction();
            if (part.isTriggered()) {
                if (!action.isFinished()) {
                    action.update(lastTimePerFrame);
                } else if (!action.isCleanuped()) {
                    action.cleanup();
                }
            } else if (time >= part.getStartTime()) {
                part.trigger();
            }
        }
    }

    public void start() {
        simpleApplication.getRootNode().attachChild(rootNode);
        simpleApplication.getGuiNode().attachChild(guiNode);
        started = true;
    }

    public void stop() {
        simpleApplication.getRootNode().detachChild(rootNode);
        simpleApplication.getGuiNode().detachChild(guiNode);
        started = false;
        for (CinematicPart part : parts) {
            CinematicAction action = part.getAction();
            if (!action.isCleanuped()) {
                action.cleanup();
            }
        }
    }

    public boolean isFinished() {
        for (CinematicPart part : parts) {
            if ((!part.isTriggered()) || (!part.getAction().isFinished())) {
                return false;
            }
        }
        return true;
    }
}
