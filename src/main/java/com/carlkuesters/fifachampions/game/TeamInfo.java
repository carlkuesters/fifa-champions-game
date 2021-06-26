package com.carlkuesters.fifachampions.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TeamInfo {

    private String name;
    private String abbreviation;
    private String[] trikotNames;
    private Player[] fieldPlayers;
    private Player[] reservePlayers;
    private Formation defaultFormation;

    public int getAverageSkill_Defense() {
        return PlayerSkillUtil.getAverage(
            fieldPlayers[0].getGoalkeeperSkills().getAverageSkill(),
            fieldPlayers[1].getFieldPlayerSkills().getAverageSkill(),
            fieldPlayers[2].getFieldPlayerSkills().getAverageSkill(),
            fieldPlayers[3].getFieldPlayerSkills().getAverageSkill(),
            fieldPlayers[4].getFieldPlayerSkills().getAverageSkill()
        );
    }

    public int getAverageSkill_Middlefield() {
        return PlayerSkillUtil.getAverage(
            fieldPlayers[5].getFieldPlayerSkills().getAverageSkill(),
            fieldPlayers[6].getFieldPlayerSkills().getAverageSkill(),
            fieldPlayers[7].getFieldPlayerSkills().getAverageSkill(),
            fieldPlayers[8].getFieldPlayerSkills().getAverageSkill()
        );
    }

    public int getAverageSkill_Offense() {
        return PlayerSkillUtil.getAverage(
            fieldPlayers[9].getFieldPlayerSkills().getAverageSkill(),
            fieldPlayers[10].getFieldPlayerSkills().getAverageSkill()
        );
    }
}
