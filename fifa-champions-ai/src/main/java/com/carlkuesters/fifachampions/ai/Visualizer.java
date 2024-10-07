package com.carlkuesters.fifachampions.ai;

import com.carlkuesters.fifachampions.ai.sources.metrica.DataSource_Metrica;
import com.carlkuesters.fifachampions.ai.sources.statsbomb.DataSource_Statsbomb;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Visualizer extends JFrame {

    public static void main(String[] args) throws Exception {
        new Visualizer().setVisible(true);
    }

    public Visualizer() throws Exception {
        setTitle("Visualizer");
        setSize(700, 400);
        setResizable(false);
        setLayout(new BorderLayout());
        add(visualizerPanel, BorderLayout.CENTER);
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent evt) {
                super.keyReleased(evt);
                switch (evt.getKeyCode()) {
                    case KeyEvent.VK_RIGHT -> setDataIndex(dataIndex + 1);
                    case KeyEvent.VK_LEFT -> setDataIndex(dataIndex - 1);
                }
            }
        });

        DataSource source = new DataSource_Statsbomb();
        source = new DataSource_Metrica();
        dataList = source.load();
        Collections.shuffle(dataList);
        setDataIndex(0);
    }
    private VisualizerPanel visualizerPanel = new VisualizerPanel();
    private List<float[][]> dataList;
    private int dataIndex;

    private void setDataIndex(int dataIndex) {
        this.dataIndex = dataIndex;
        float[][] data = dataList.get(dataIndex);
        float[] input = data[0];
        float[] output = data[1];
        ArrayList<VisualizerObject> visualizerObjects = new ArrayList<>();
        visualizerObjects.add(new VisualizerObject("Ball", input[0], input[1], 50));
        visualizerObjects.add(new VisualizerObject("Role", input[2], input[3], 50));
        visualizerObjects.add(new VisualizerObject("Player", output[0], output[1], 50));
        visualizerPanel.setObjects(visualizerObjects);
        repaint();
    }

    @AllArgsConstructor
    @Getter
    static class VisualizerObject {
        private String name;
        private float x;
        private float y;
        private int size;
    }

    static class VisualizerPanel extends JPanel {

        @Setter
        private List<VisualizerObject> objects = new ArrayList<>();
        private Color backgroundColor = new Color(100, 200, 50);
        private Color playerColor = new Color(200, 100, 50);
        private Color ballColor = new Color(100, 50, 200);

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(backgroundColor);
            g.fillRect(0, 0, getWidth(), getHeight());
            for (VisualizerObject object : objects) {
                int x = Math.round(((object.getX() + 1) / 2) * getWidth());
                int y = Math.round(((object.getY() + 1) / 2) * getHeight());
                int size = object.getSize();
                g.setColor(object.getName().startsWith("Player") ? playerColor : ballColor);
                g.fillOval(x - (size / 2), y - (size / 2), size, size);

                g.setColor(Color.BLACK);
                int nameWidth = g.getFontMetrics().stringWidth(object.getName());
                g.drawString(object.getName(), x - (nameWidth / 2), y + 5);
            }
        }
    }
}
