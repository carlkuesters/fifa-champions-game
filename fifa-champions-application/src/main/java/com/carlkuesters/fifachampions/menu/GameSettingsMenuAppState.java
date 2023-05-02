package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameAppState;
import com.carlkuesters.fifachampions.game.GameCreationInfo;

public class GameSettingsMenuAppState extends SettingsMenuAppState {

    public GameSettingsMenuAppState() {
        super("Einstellungen");
    }
    private LabelCarousel<Float> carouselHalftimeDuration;

    @Override
    protected void initMenu() {
        super.initMenu();
        addButton("Spiel starten", this::startGame);
        addButton("Aufstellung", () -> openMenu(InitialFormationMenuAppState.class));
        carouselHalftimeDuration = addCarouselButton(
            new Float[] { 120f, 180f, 240f, 300f, 360f, 420f, 480f },
            halfTimeDuration -> "Halbzeitlänge: " + (int) (halfTimeDuration / 60) + " Minuten",
            halfTimeDuration -> mainApplication.getGameCreationInfo().setHalftimeDuration(halfTimeDuration)
        );
    }

    private void startGame() {
        close();
        mainApplication.getStateManager().attach(new GameAppState());
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            GameCreationInfo gameCreationInfo = mainApplication.getGameCreationInfo();
            carouselHalftimeDuration.setValue(gameCreationInfo.getHalftimeDuration());
        }
    }
}
