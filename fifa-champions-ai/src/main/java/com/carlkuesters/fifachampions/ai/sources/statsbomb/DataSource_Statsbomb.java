package com.carlkuesters.fifachampions.ai.sources.statsbomb;

import com.carlkuesters.fifachampions.ai.DataSource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DataSource_Statsbomb extends DataSource {

    public DataSource_Statsbomb() {
        super(4, 2);
    }
    private static final String ROOT = "../../../statsbomb/open-data/data/";

    @Override
    public List<float[][]> load() {
        List<float[][]> allData = new ArrayList<>();

        HashMap<String, String[]> whitelistedRelations = new HashMap<>();
        whitelistedRelations.put("Ball Receipt*", new String[] { "Pass", "Pressure" });
        whitelistedRelations.put("Carry", new String[] { "Ball Recovery", "Block", "Pressure" });
        whitelistedRelations.put("Pass", new String[] {
            "Ball Receipt*", "Ball Recovery", "Block", "Carry", "Clearance", "Foul Committed",
            "Interception", "Pass", "Pressure"
        });

        HashMap<String, String[]> blacklistedRelations = new HashMap<>();
        blacklistedRelations.put("Carry", new String[] {
            "50/50", "Ball Receipt*", "Dispossessed", "Duel", "Dribble", "Dribbled Past", "Foul Won",
            "Foul Committed", "Goal Keeper", "Injury Stoppage", "Interception", "Miscontrol", "Pass", "Shot"
        });
        blacklistedRelations.put("Pass", new String[] {
            "50/50", "Duel", "Foul Won", "Goal Keeper"
        });

        HashSet<String> ignoredTypes = new HashSet<>();
        ignoredTypes.add("Starting XI");

        File matchesDirectory = new File(ROOT + "matches");
        for (File competitionDirectory : matchesDirectory.listFiles()) {
            for (File seasonDirectory : competitionDirectory.listFiles()) {
                JSONArray matches = new JSONArray(readFile(seasonDirectory.getPath()));
                for (int i = 0; i < matches.length(); i++) {
                    JSONObject match = matches.getJSONObject(i);
                    int matchId = match.getInt("match_id");
                    System.out.println("Loading match " + matchId + "...");
                    JSONArray events = new JSONArray(readFile(ROOT + "events/" + matchId + ".json"));

                    int homeTeamId = match.getJSONObject("home_team").getInt("home_team_id");
                    int awayTeamId = match.getJSONObject("away_team").getInt("away_team_id");
                    Integer invertedSideTeamId = null;

                    HashMap<String, JSONObject> eventsMap = new HashMap<>();
                    for (int r = 0; r < events.length(); r++) {
                        JSONObject event = events.getJSONObject(r);
                        eventsMap.put(event.getString("id"), event);

                        if (invertedSideTeamId == null) {
                            int period = event.getInt("period");
                            if ((period == 1) && event.has("position")) {
                                String playerRoleName = event.getJSONObject("position").getString("name");
                                if (playerRoleName.equals("Goalkeeper")) {
                                    int goalkeeperTeamId = event.getJSONObject("team").getInt("id");
                                    if ((goalkeeperTeamId == homeTeamId) && event.has("location")) {
                                        float homeGoalkeeperX = event.getJSONArray("location").getFloat(0);
                                        invertedSideTeamId = ((homeGoalkeeperX < 60) ? awayTeamId : homeTeamId);
                                    }
                                }
                            }
                        }
                    }
                    for (JSONObject event : eventsMap.values()) {
                        String playPattern = event.getJSONObject("play_pattern").getString("name");
                        if (playPattern.equals("Regular Play")) {
                            continue;
                        }
                        int period = event.getInt("period");
                        if (period == 5) {
                            continue;
                        }
                        String type = event.getJSONObject("type").getString("name");
                        String[] whitelistedRelatedTypes = whitelistedRelations.get(type);
                        if ((whitelistedRelatedTypes != null) && event.has("related_events")) {
                            JSONArray relatedEventIds = event.getJSONArray("related_events");
                            for (int r = 0; r < relatedEventIds.length(); r++) {
                                String relatedEventId = relatedEventIds.getString(r);
                                JSONObject relatedEvent = eventsMap.get(relatedEventId);
                                String relatedEventType = relatedEvent.getJSONObject("type").getString("name");
                                boolean wasWhitelisted = false;
                                for (String relatedType : whitelistedRelatedTypes) {
                                    if (relatedType.equals(relatedEventType)) {
                                        float[] ballLocation = getPosition(event, invertedSideTeamId);
                                        float[] playerLocation = getPosition(relatedEvent, invertedSideTeamId);
                                        StatsbombPlayerRole playerRole = StatsbombPlayerRole.values()[relatedEvent.getJSONObject("position").getInt("id") - 1];

                                        float[] inputs = new float[]{ ballLocation[0], ballLocation[1], playerRole.getX(), playerRole.getY() };
                                        float[] outputs = playerLocation;
                                        allData.add(new float[][]{ inputs, outputs });
                                        wasWhitelisted = true;
                                        break;
                                    }
                                }
                                if (!wasWhitelisted) {
                                    boolean isBlacklisted = false;
                                    String[] blacklistedRelatedTypes = blacklistedRelations.get(type);
                                    if (blacklistedRelatedTypes != null) {
                                        for (String blacklistedRelatedType : blacklistedRelatedTypes) {
                                            if (blacklistedRelatedType.equals(relatedEventType)) {
                                                isBlacklisted = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (!isBlacklisted) {
                                        System.out.println(matchId + "\t" + type + "(" + event.getString("id") + ")\t" + relatedEventType + " (" + relatedEventId + ")");
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            }
            break;
        }

        return allData;
    }

    private float[] getPosition(JSONObject event, int invertedSideTeamId) {
        JSONArray location = event.getJSONArray("location");
        float x = (location.getFloat(0) / 120);
        float y = (location.getFloat(1) / 60);
        JSONObject team = event.getJSONObject("team");
        if (team.getInt("id") == invertedSideTeamId) {
            // TODO: Confirm that this is really already done in the data
            // x = (1 - x);
            // y = (1 - y);
        }
        int period = event.getInt("period");
        if ((period == 2) || (period == 4)) {
            x = (1 - x);
            y = (1 - y);
        }
        x = Math.max(0, Math.min(x, 1));
        y = Math.max(0, Math.min(y, 1));
        return new float[]{ x, y };
    }
}
