package com.carlkuesters.fifachampions.ai.sources.metrica;

import lombok.Getter;
import lombok.Setter;

@Getter
public class MetricaFrame {

    public MetricaFrame(int frame) {
        this.frame = frame;
    }
    private int frame;
    @Setter
    private String possessionTeam;
    @Setter
    private boolean isGameRunning;
}
