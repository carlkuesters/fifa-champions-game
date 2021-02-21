package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.game.InitialTeamInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class GameCreationInfo {
    private HashMap<Integer, Integer> controllerTeamSides;
    private InitialTeamInfo[] teams;
    private float halftimeDuration = 240;
}
