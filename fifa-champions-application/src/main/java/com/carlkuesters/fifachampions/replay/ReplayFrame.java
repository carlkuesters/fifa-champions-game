package com.carlkuesters.fifachampions.replay;

import com.carlkuesters.fifachampions.visuals.PlayerVisual;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
public class ReplayFrame {

    public ReplayFrame(HashMap<PlayerVisual, PlayerReplayState> playerStates, PhysicsReplayState ballState, String combinedTime, boolean audienceHyped) {
        this.playerStates = playerStates;
        this.ballState = ballState;
        this.combinedTime = combinedTime;
        this.audienceHyped = audienceHyped;
    }
    private HashMap<PlayerVisual, PlayerReplayState> playerStates;
    private PhysicsReplayState ballState;
    private String combinedTime;
    private boolean audienceHyped;
    @Setter
    private float replayTime;
}
