package com.carlkuesters.fifachampions.ai.sources.metrica;

import au.com.bytecode.opencsv.CSVReader;
import com.carlkuesters.fifachampions.ai.DataSource;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DataSource_Metrica extends DataSource {

    public DataSource_Metrica() {
        super(4, 2);
    }

    @Override
    public List<float[][]> load() throws IOException {
        List<float[][]> allData = new ArrayList<>();

        HashSet<String> possessionEventTypes = new HashSet<>();
        possessionEventTypes.add("PASS");
        possessionEventTypes.add("RECOVERY");
        possessionEventTypes.add("SHOT");

        MetricaParser parser = new MetricaParser();
        MetricaGame[] games = new MetricaGame[] {
            MetricaGames.GAMES[0],
            MetricaGames.GAMES[1],
        };
        for (MetricaGame game : games) {
            System.out.println("Loading tracking file " + game.getEventsPath() + "...");
            ArrayList<MetricaFrame> frames = new ArrayList<>();
            String[] values;
            try (CSVReader csvReader = new CSVReader(new FileReader(game.getEventsPath()))) {
                // Skip unused lines
                csvReader.readNext();
                String possessionTeam = null;
                while ((values = csvReader.readNext()) != null) {
                    String team = values[0];
                    String type = values[1];
                    String subType = values[2];
                    int startFrame = Integer.parseInt(values[4]);
                    boolean isGameRunning = (!type.equals("BALL OUT") && !subType.equals("END HALF"));
                    if (possessionEventTypes.contains(type)) {
                        possessionTeam = team;
                    }
                    MetricaFrame existingFrame = null;
                    if (frames.size() > 0) {
                        MetricaFrame lastFrame = frames.get(frames.size() - 1);
                        if (lastFrame.getFrame() == startFrame) {
                            existingFrame = lastFrame;
                        }
                    }
                    MetricaFrame frame;
                    if (existingFrame == null) {
                        frame = new MetricaFrame(startFrame);
                        frames.add(frame);
                    } else {
                        frame = existingFrame;
                    }
                    frame.setGameRunning(isGameRunning);
                    frame.setPossessionTeam(possessionTeam);
                }
            }
            for (MetricaFrame frame : frames) {
                if (frame.getPossessionTeam() == null) {
                    throw new RuntimeException("No possession team at frame " + frame.getFrame());
                }
            }
            MetricaFrame endFrame = new MetricaFrame(frames.get(frames.size() - 1).getFrame() - 1);
            endFrame.setGameRunning(false);
            frames.add(endFrame);
            parser.setFrames(frames);

            for (MetricaTrackingFile file : game.getTeamTrackingFiles()) {
                System.out.println("Loading tracking file " + file.getPath() + "...");
                try (CSVReader csvReader = new CSVReader(new FileReader(file.getPath()))) {
                    // Skip unused lines
                    csvReader.readNext();
                    csvReader.readNext();
                    String[] headers = csvReader.readNext();
                    parser.prepareTeam(headers);
                    while ((values = csvReader.readNext()) != null) {
                        ArrayList<float[][]> newData = parser.readLine(values, file);
                        for (float[][] data : newData) {
                            for (float[] dataValues : data) {
                                for (float value : dataValues) {
                                    if (Float.isNaN(value)) {
                                        throw new RuntimeException("NaN detected!");
                                    }
                                }
                            }
                        }
                        allData.addAll(newData);
                    }
                }
                System.out.println("Loaded file.");
            }
        }

        return allData;
    }
}
