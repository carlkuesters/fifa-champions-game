package com.carlkuesters.fifachampions.ai.sources.metrica;

import com.jme3.math.Vector2f;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class MetricaParser {

    @Setter
    private List<MetricaFrame> frames;
    private ArrayList<Integer> fieldPlayerColumns = new ArrayList<>();
    private ArrayList<Integer> reservePlayerColumns = new ArrayList<>();
    private int ballColumn = 0;
    private int activeFrameIndex;

    public void prepareTeam(String[] headers) {
        fieldPlayerColumns.clear();
        reservePlayerColumns.clear();
        ballColumn = -1;
        activeFrameIndex = 0;
        for (int i = 0; i < headers.length; i++) {
            String header = headers[i];
            if (header.startsWith("Player")) {
                if (fieldPlayerColumns.size() < 11) {
                    fieldPlayerColumns.add(i);
                } else {
                    reservePlayerColumns.add(i);
                }
            } else if (header.equals("Ball")) {
                ballColumn = i;
            }
        }
    }

    public ArrayList<float[][]> readLine(String[] values, MetricaTrackingFile file) {
        ArrayList<float[][]> data = new ArrayList<>();
        for (MetricaPlayerRole playerRole : MetricaPlayerRole.values()) {
            float[][] roleData = readLine(values, file, playerRole);
            if (roleData != null) {
                data.add(roleData);
            }
        }
        return data;
    }

    private float[][] readLine(String[] values, MetricaTrackingFile file, MetricaPlayerRole playerRole) {
        // Frame
        int frame = Integer.parseInt(values[1]);
        MetricaFrame activeFrame = null;
        for (int i = activeFrameIndex; i < frames.size(); i++) {
            MetricaFrame nextFrame = frames.get(i);
            if (frame < nextFrame.getFrame()) {
                break;
            }
            activeFrameIndex = i;
            activeFrame = nextFrame;
        }
        // Game hasn't started yet
        if (activeFrame == null) {
            return null;
        }
        if (!activeFrame.isGameRunning()) {
            return null;
        }
        // Not in possession
        if (!file.isAwayTeam() && activeFrame.getPossessionTeam().equals("Away")) {
            return null;
        }
        if (file.isAwayTeam() && activeFrame.getPossessionTeam().equals("Home")) {
            return null;
        }

        // Player
        int playerIndex = -1;
        for (int i = 0; i < file.getRoleColumns().length; i++) {
            if (file.getRoleColumns()[i] == playerRole) {
                playerIndex = i;
            }
        }

        int playerColumn = fieldPlayerColumns.get(playerIndex);
        float[] playerPosition = getPosition(values, playerColumn, file);
        float x = playerPosition[0];
        float y = playerPosition[1];
        if (Float.isNaN(x) && Float.isNaN(y)) {
            for (int r = 0; r < reservePlayerColumns.size(); r++) {
                int reservePlayerColumn = reservePlayerColumns.get(r);
                float[] reservePlayerPosition = getPosition(values, reservePlayerColumn, file);
                float reservePlayerX = reservePlayerPosition[0];
                float reservePlayerY = reservePlayerPosition[1];
                if (!Float.isNaN(reservePlayerX) && !Float.isNaN(reservePlayerY)) {
                    System.out.println("Exchange " + playerColumn + " for " + reservePlayerColumn);
                    fieldPlayerColumns.set(playerIndex, reservePlayerColumn);
                    x = reservePlayerX;
                    y = reservePlayerY;
                    reservePlayerColumns.remove(r);
                    break;
                }
            }
        }

        // Ball
        float[] ballPosition = getPosition(values, ballColumn, file);
        float ballX = ballPosition[0];
        float ballY = ballPosition[1];
        if (Float.isNaN(ballX) || Float.isNaN(ballY)) {
            return null;
        }

        float playerRoleX = ((playerRole.getX() - 0.5f) * 2);
        float playerRoleY = ((playerRole.getY() - 0.5f) * 2);

        float distanceToRoleX = Math.abs(x - playerRoleX);
        float distanceToRoleY = Math.abs(y - playerRoleY);
        float maximumDistanceToRole = 1.5f;
        if ((distanceToRoleX > maximumDistanceToRole) || (distanceToRoleY > maximumDistanceToRole)) {
            return null;
        }

        float distanceToBall = new Vector2f(x, y).distance(new Vector2f(ballX, ballY));
        if (distanceToBall < 0.04f) {
            return null;
        }

        float[] inputs = new float[] { ballX, ballY, playerRoleX, playerRoleY };
        float[] outputs = new float[] { x, y };
        return new float[][] { inputs, outputs };
    }

    private float[] getPosition(String[] values, int index, MetricaTrackingFile file) {
        float x = Float.parseFloat(values[index]);
        float y = Float.parseFloat(values[index + 1]);
        x = ((x - 0.5f) * 2);
        y = ((y - 0.5f) * 2);
        if (file.isAwayTeam()) {
            x *= -1;
            y *= -1;
        }
        boolean isSecondHalf = values[0].equals("2");
        if (isSecondHalf) {
            x *= -1;
            y *= -1;
        }
        x = Math.max(-1, Math.min(x, 1));
        y = Math.max(-1, Math.min(y, 1));
        return new float[]{ x, y };
    }
}
