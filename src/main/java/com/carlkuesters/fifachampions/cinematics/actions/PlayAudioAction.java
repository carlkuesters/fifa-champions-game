package com.carlkuesters.fifachampions.cinematics.actions;

import com.carlkuesters.fifachampions.cinematics.CinematicAction;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;

public class PlayAudioAction extends CinematicAction {

    public PlayAudioAction(AudioNode audioNode) {
        this.audioNode = audioNode;
    }
    private AudioNode audioNode;

    @Override
    public void trigger() {
        super.trigger();
        audioNode.play();
    }

    @Override
    protected boolean isFinished() {
        return (audioNode.getStatus() == AudioSource.Status.Stopped);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        audioNode.stop();
    }
}
