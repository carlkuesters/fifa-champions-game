package com.carlkuesters.fifachampions.ai.sources.statsbomb;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatsbombPlayerRole {
    GK(0, 0.5f), // 1
    RB(0.15f, 0.8f), // 2
    RCB(0.15f, 0.6f), // 3
    CB(0.15f, 0.5f), // 4
    LCB(0.15f, 0.4f), // 5
    LB(0.15f, 0.2f), // 6
    RWB(0.325f, 0.8f), // 7
    LWB(0.325f, 0.2f), // 8
    RDM(0.325f, 0.6f), // 9
    CDM(0.325f, 0.5f), // 10
    LDM(0.325f, 0.4f), // 11
    RM(0.5f, 0.8f), // 12
    RCM(0.5f, 0.6f), // 13
    CM(0.5f, 0.5f), // 14
    LCM(0.5f, 0.4f), // 15
    LM(0.5f, 0.2f), // 16
    RW(0.65f, 0.8f), // 17
    RAM(0.65f, 0.6f), // 18
    CAM(0.65f, 0.5f), // 19
    LAM(0.65f, 0.4f), // 20
    LW(0.65f, 0.2f), // 21
    RCF(0.85f, 0.6f), // 22
    ST(0.85f, 0.5f), // 23
    LCF(0.85f, 0.4f), // 24
    SS(0.75f, 0.5f); // 25

    private float x;
    private float y;
}
