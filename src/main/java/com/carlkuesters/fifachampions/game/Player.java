package com.carlkuesters.fifachampions.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Player {
    private String name;
    private String position;
    private FieldPlayerSkills fieldPlayerSkills;
    private GoalkeeperSkills goalkeeperSkills;
}
