package com.carlkuesters.fifachampions.ai.sources.metrica;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MetricaPlayerRole {
    GK(0, 0.5f),
    LD(0.15f, 0.2f),
    LCD(0.15f, 0.4f),
    RCD(0.15f, 0.6f),
    RD(0.15f, 0.8f),
    LM(0.5f, 0.2f),
    LCM(0.5f, 0.4f),
    RCM(0.5f, 0.6f),
    RM(0.5f, 0.8f),
    LS(0.85f, 0.25f),
    RS(0.85f, 0.75f);

    private float x;
    private float y;
}
