package com.carlkuesters.fifachampions.game;

import lombok.Getter;

@Getter
public class TeamStatistics {

    private int shotsTotal;
    private int shotsOnGOal;
    private float timeWithBall;
    private int fightsTotal;
    private int fightsWon;
    private int fouls;
    private int offsides;
    private int corners;
    private int passesTotal;
    private int passesSuccessful;

    public void addShot() {
        shotsTotal++;
    }

    public void addShotOnGoal() {
        shotsOnGOal++;
    }

    public void addTimeWithBall(float time) {
        timeWithBall += time;
    }

    public void addFight(boolean won) {
        fightsTotal++;
        if (won) {
            fightsWon++;
        }
    }

    public void addFoul() {
        fouls++;
    }

    public void addOffside() {
        offsides++;
    }

    public void addCorner() {
        corners++;
    }

    public void addPass(boolean successful) {
        passesTotal++;
        if (successful) {
            passesSuccessful++;
        }
    }

    public float getShotAccuracy() {
        return (((float) shotsTotal) / shotsOnGOal);
    }

    public float getPassAccuracy() {
        return (((float) passesSuccessful) / passesTotal);
    }
}
