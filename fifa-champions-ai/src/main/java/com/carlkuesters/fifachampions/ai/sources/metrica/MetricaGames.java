package com.carlkuesters.fifachampions.ai.sources.metrica;

public class MetricaGames {

    private static final String ROOT = "../../../metrica-sports/sample-data/data/";

    public static final MetricaGame[] GAMES = new MetricaGame[] {
        new MetricaGame(ROOT + "Sample_Game_1/Sample_Game_1_RawEventsData.csv", new MetricaTrackingFile[]{
            new MetricaTrackingFile(ROOT + "Sample_Game_1/Sample_Game_1_RawTrackingData_Home_Team.csv", false, new MetricaPlayerRole[]{
                MetricaPlayerRole.GK,
                MetricaPlayerRole.RD,
                MetricaPlayerRole.RCD,
                MetricaPlayerRole.LCD,
                MetricaPlayerRole.LD,
                MetricaPlayerRole.RM,
                MetricaPlayerRole.RCM,
                MetricaPlayerRole.LCM,
                MetricaPlayerRole.LM,
                MetricaPlayerRole.LS,
                MetricaPlayerRole.RS,
            }),
            new MetricaTrackingFile(ROOT + "Sample_Game_1/Sample_Game_1_RawTrackingData_Away_Team.csv", true, new MetricaPlayerRole[]{
                MetricaPlayerRole.GK,
                MetricaPlayerRole.RD,
                MetricaPlayerRole.RCD,
                MetricaPlayerRole.LD,
                MetricaPlayerRole.LS,
                MetricaPlayerRole.RCM,
                MetricaPlayerRole.LCD,
                MetricaPlayerRole.LCM,
                MetricaPlayerRole.LM,
                MetricaPlayerRole.RM,
                MetricaPlayerRole.RS,
            }),
        }),
        new MetricaGame(ROOT + "Sample_Game_2/Sample_Game_2_RawEventsData.csv", new MetricaTrackingFile[]{
            new MetricaTrackingFile(ROOT + "Sample_Game_2/Sample_Game_2_RawTrackingData_Home_Team.csv", true, new MetricaPlayerRole[]{
                MetricaPlayerRole.GK,
                MetricaPlayerRole.RD,
                MetricaPlayerRole.RCD,
                MetricaPlayerRole.LCD,
                MetricaPlayerRole.LD,
                MetricaPlayerRole.RM,
                MetricaPlayerRole.RCM,
                MetricaPlayerRole.LCM,
                MetricaPlayerRole.LM,
                MetricaPlayerRole.RS,
                MetricaPlayerRole.LS,
            }),
            new MetricaTrackingFile(ROOT + "Sample_Game_2/Sample_Game_2_RawTrackingData_Away_Team.csv", false, new MetricaPlayerRole[]{
                MetricaPlayerRole.GK,
                MetricaPlayerRole.RD,
                MetricaPlayerRole.RCD,
                MetricaPlayerRole.LCD,
                MetricaPlayerRole.LD,
                MetricaPlayerRole.RM,
                MetricaPlayerRole.RCM,
                MetricaPlayerRole.LCM,
                MetricaPlayerRole.LM,
                MetricaPlayerRole.RS,
                MetricaPlayerRole.LS,
            })
        })
    };
}
