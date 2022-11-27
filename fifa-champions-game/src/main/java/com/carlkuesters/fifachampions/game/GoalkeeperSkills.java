package com.carlkuesters.fifachampions.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GoalkeeperSkills {

    private int agility;
    private int jumpStrength;
    private int reflexes;
    private int ballCling;

    public int getAverageSkill() {
        return PlayerSkillUtil.getAverage(agility, jumpStrength, reflexes, ballCling);
    }
}
