package com.carlkuesters.fifachampions.replay;

import com.carlkuesters.fifachampions.visuals.PlayerVisual;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

@AllArgsConstructor
@Getter
public class ReplayFrame {
    private float replayTime;
    private HashMap<PlayerVisual, PlayerReplayState> playerStates;
    private PhysicsReplayState ballState;
    private String combinedTime;
    private boolean audienceHyped;
}
