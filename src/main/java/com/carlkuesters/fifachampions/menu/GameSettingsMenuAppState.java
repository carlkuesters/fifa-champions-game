package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameAppState;
import com.carlkuesters.fifachampions.GameCreationInfo;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;

import java.util.function.Consumer;
import java.util.function.Function;

public class GameSettingsMenuAppState extends MenuAppState {

    private int containerWidth = 600;
    private LabelCarousel<Float> carouselHalftimeDuration;

    @Override
    protected void initMenu() {
        addTitle("Einstellungen");

        ElementsMenuGroup menuGroup = new ElementsMenuGroup(() -> openMenu(TrikotMenuAppState.class));

        int containerX = ((totalWidth / 2) - (containerWidth / 2));
        int containerY = (totalHeight - 200);
        Container container = new Container();
        container.setLocalTranslation(containerX, containerY, 0);

        addButton(container, menuGroup, "Spiel starten", this::startGame);
        addButton(container, menuGroup, "Aufstellung", () -> openMenu(InitialFormationMenuAppState.class));
        carouselHalftimeDuration = addCarouselButton(
            container,
            menuGroup,
            new Float[] { 120f, 180f, 240f, 300f, 360f, 420f, 480f },
            halfTimeDuration -> "Halbzeitlänge: " + (int) (halfTimeDuration / 60) + " Minuten",
            halfTimeDuration -> mainApplication.getGameCreationInfo().setHalftimeDuration(halfTimeDuration)
        );

        guiNode.attachChild(container);

        addMenuGroup(menuGroup);
    }

    private void addButton(Container container, ElementsMenuGroup menuGroup, String text, Runnable action) {
        Button button = addButton(container, text);
        menuGroup.addElement(new MenuElement(button, action));
    }

    private <T> LabelCarousel<T> addCarouselButton(Container container, ElementsMenuGroup menuGroup, T[] values, Function<T, String> getText, Consumer<T> setCurrentValue) {
        Button button = addButton(container, "");
        LabelCarousel<T> labelCarousel = new LabelCarousel<>(button, values, getText);
        menuGroup.addElement(new MenuElement(button, () -> {
            CarouselUtil.changeValue(labelCarousel, -1, 1);
            setCurrentValue.accept(labelCarousel.getValue());
        }));
        return labelCarousel;
    }

    private Button addButton(Container container, String text) {
        Button button = new Button(text);
        button.setFontSize(20);
        button.setTextHAlignment(HAlignment.Center);
        button.setPreferredSize(new Vector3f(containerWidth, 30, 0));
        container.addChild(button);
        return button;
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
