package com.carlkuesters.fifachampions.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameCreationInfo {
    private InitialTeamInfo[] teams;
    private float halftimeDuration = 240;
}
