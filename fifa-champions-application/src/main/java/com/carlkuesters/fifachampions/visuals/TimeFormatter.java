package com.carlkuesters.fifachampions.visuals;

import com.carlkuesters.fifachampions.game.Game;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TimeFormatter {

    private static final int SECONDS_PER_HALF_TIME = (45 * 60);

    private Game game;

    public String getCombinedTime() {
        String time = getTime();
        String overTime = getOverTime();
        return (time + ((overTime != null) ? " (+" + overTime + ")" : ""));
    }

    public String getTime() {
        return formatTime(game.getPassedTime());
    }

    public String getOverTime() {
        return ((game.getHalfTimePassedOverTime() > 0) ? formatTime(game.getHalfTimePassedOverTime()) : null);
    }

    private String formatTime(float time) {
        int seconds = (int) ((time / game.getHalfTimeDuration()) * SECONDS_PER_HALF_TIME);
        int minutes = (seconds / 60);
        seconds -= (minutes * 60);
        return formatMinutesOrSeconds(minutes) + ":" + formatMinutesOrSeconds(seconds);
    }

    private String formatMinutesOrSeconds(int value) {
        return ((value < 10) ? "0" : "") + value;
    }
}
