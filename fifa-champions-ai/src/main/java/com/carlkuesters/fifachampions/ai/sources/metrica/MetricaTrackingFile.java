package com.carlkuesters.fifachampions.ai.sources.metrica;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MetricaTrackingFile {
    private String path;
    private boolean awayTeam;
    private MetricaPlayerRole[] roleColumns;
}
