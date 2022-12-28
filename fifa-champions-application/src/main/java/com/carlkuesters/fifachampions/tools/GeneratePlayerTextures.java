package com.carlkuesters.fifachampions.tools;

import com.carlkuesters.fifachampions.game.content.Teams;
import com.carlkuesters.fifachampions.visuals.PlayerSkin;
import com.carlkuesters.fifachampions.visuals.PlayerSkins;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GeneratePlayerTextures {

    private static final String DIRECTORY = "../assets/models/player/resources/";

    public static void main(String[] args) throws IOException {
        BufferedImage originalFace = ImageIO.read(new File(DIRECTORY + "face_original.png"));
        BufferedImage originalBody = ImageIO.read(new File(DIRECTORY + "body_original.png"));

        String[] trikotNames = Teams.FC_CHAMPIONS.getTrikotNames();
        BufferedImage[] trikots = new BufferedImage[trikotNames.length];
        for (int i = 0; i < trikots.length; i++) {
            trikots[i] = ImageIO.read(new File(DIRECTORY + "trikot_" + trikotNames[i] + ".png"));
        }

        int referenceFaceX = 827;
        int referenceFaceY = 1122;
        int referenceBodyX = 269;
        int referenceBodyY = 1896;
        Color referenceColorFace = new Color(originalFace.getRGB(referenceFaceX, referenceFaceY));
        Color referenceColorBody = new Color(originalBody.getRGB(referenceBodyX, referenceBodyY));
        System.out.println("Reference color face: " + referenceColorFace);
        System.out.println("Reference color body: " + referenceColorBody);

        for (PlayerSkin skin : PlayerSkins.getAllSkins()) {
            BufferedImage playerFace = ImageIO.read(new File( DIRECTORY + "face_" + skin.getFaceName() + ".png"));

            Color actualFace = new Color(playerFace.getRGB(referenceFaceX, referenceFaceY));
            int playerDifferenceRed = (actualFace.getRed() - referenceColorFace.getRed());
            int playerDifferenceGreen = (actualFace.getGreen() - referenceColorFace.getGreen());
            int playerDifferenceBlue = (actualFace.getBlue() - referenceColorFace.getBlue());

            BufferedImage playerBody = new BufferedImage(originalBody.getWidth(), originalBody.getHeight(), originalBody.getType());
            Graphics playerBodyGraphics = playerBody.getGraphics();
            playerBodyGraphics.setColor(Color.BLACK);
            playerBodyGraphics.fillRect(0, 0, playerBody.getWidth(), playerBody.getHeight());
            for (int x = 0; x < playerBody.getWidth(); x++) {
                for (int y = 0; y < playerBody.getHeight(); y++) {
                    Color originalColorBody = new Color(originalBody.getRGB(x, y), true);
                    if (originalColorBody.getAlpha() > 0) {
                        int playerBodyRed = checkPixelLimits(originalColorBody.getRed() + playerDifferenceRed);
                        int playerBodyGreen = checkPixelLimits(originalColorBody.getGreen() + playerDifferenceGreen);
                        int playerBodyBlue = checkPixelLimits(originalColorBody.getBlue() + playerDifferenceBlue);
                        Color playerBodyColor = new Color(playerBodyRed, playerBodyGreen, playerBodyBlue);
                        playerBodyGraphics.setColor(playerBodyColor);
                        playerBodyGraphics.fillRect(x, y, 1, 1);
                    }
                }
            }
            playerBodyGraphics.dispose();

            for (int i = 0; i < trikots.length; i++) {
                BufferedImage bodyAndTrikot = copyImage(playerBody);
                Graphics bodyAndTrikotGraphics = bodyAndTrikot.getGraphics();
                bodyAndTrikotGraphics.drawImage(trikots[i], 0, 0, null);
                String fileName = "body_" + skin.getFaceName() + "_" + trikotNames[i] + ".png";
                System.out.println("Writing " + fileName + "...");
                ImageIO.write(bodyAndTrikot, "png", new File(DIRECTORY + "generated/" + fileName));
            }
        }
    }

    private static int checkPixelLimits(int value) {
        return Math.max(0, Math.min(value, 255));
    }

    private static BufferedImage copyImage(BufferedImage source){
        BufferedImage target = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics graphics = target.getGraphics();
        graphics.drawImage(source, 0, 0, null);
        graphics.dispose();
        return target;
    }
}
