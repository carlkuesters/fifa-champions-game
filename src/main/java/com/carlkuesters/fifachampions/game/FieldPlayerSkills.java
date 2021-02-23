package com.carlkuesters.fifachampions.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FieldPlayerSkills {

    private int acceleration;
    private int maximumSpeed;
    private int stamina;
    private int ballControl;
    private int shootingStrength;
    private int shootingAccuracy;
    private int footDuel;
    private int headerDuel;
    private int tricks;

    public int getAverageSkill() {
        return ((acceleration + maximumSpeed + stamina + ballControl + shootingStrength + shootingAccuracy + footDuel + headerDuel + tricks) / 9);
    }
}
