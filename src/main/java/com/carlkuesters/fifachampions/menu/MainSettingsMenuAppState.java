package com.carlkuesters.fifachampions.menu;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;

public class MainSettingsMenuAppState extends SettingsMenuAppState {

    private LabelCarousel<Boolean> carouselFullscreen;
    private LabelCarousel<int[]> carouselResolution;
    private LabelCarousel<Boolean> carouselVSync;

    @Override
    protected void initMenu() {
        super.initMenu();

        JmeContext context = mainApplication.getContext();
        AppSettings settings = context.getSettings();

        carouselFullscreen = addCarouselButton("Vollbild", fullscreen -> {
            settings.setFullscreen(fullscreen);
            context.restart();
        });
        carouselResolution = addCarouselButton(
            new int[][] {
                new int[] { 1280, 720 },
                new int[] { 1600, 900 },
                new int[] { 1920, 1080 }
            },
            resolution -> "Auflösung: " + resolution[0] +" x " + resolution[1],
            resolution -> {
                settings.setWidth(resolution[0]);
                settings.setHeight(resolution[1]);
                context.restart();
            }
        );
        carouselVSync = addCarouselButton("VSync", vsync -> {
            settings.setVSync(vsync);
            context.restart();
        });
        addButton("Zurück", this::back);
    }

    @Override
    protected void back() {
        openMenu(MainMenuAppState.class);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            AppSettings settings = mainApplication.getContext().getSettings();
            carouselFullscreen.setValue(settings.isFullscreen());
            for (int[] resolution : carouselResolution.getValues()) {
                if ((settings.getWidth() == resolution[0]) && (settings.getHeight() == resolution[1])) {
                    carouselResolution.setValue(resolution);
                    break;
                }
            }
            carouselVSync.setValue(settings.isVSync());
        }
    }
}
