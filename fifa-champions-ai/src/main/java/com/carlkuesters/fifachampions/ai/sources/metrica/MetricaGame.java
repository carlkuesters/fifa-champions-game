package com.carlkuesters.fifachampions.ai.sources.metrica;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MetricaGame {
    private String eventsPath;
    private MetricaTrackingFile[] teamTrackingFiles;
}
