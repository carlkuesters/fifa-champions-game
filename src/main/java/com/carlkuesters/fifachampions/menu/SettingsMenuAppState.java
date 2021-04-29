package com.carlkuesters.fifachampions.menu;

import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class SettingsMenuAppState extends MenuAppState {

    private int containerWidth = 600;
    private Container container;
    private ElementsMenuGroup menuGroup;

    @Override
    protected void initMenu() {
        addTitle("Einstellungen");

        int containerX = ((totalWidth / 2) - (containerWidth / 2));
        int containerY = (totalHeight - 200);
        container = new Container();
        container.setLocalTranslation(containerX, containerY, 0);
        guiNode.attachChild(container);

        menuGroup = new ElementsMenuGroup();
        addMenuGroup(menuGroup);
    }

    protected void addButton(String text, Runnable action) {
        Button button = addButton(text);
        menuGroup.addElement(new MenuElement(button, action));
    }

    protected LabelCarousel<Boolean> addCarouselButton(String title, Consumer<Boolean> setCurrentValue) {
        return addCarouselButton(
            new Boolean[] { true, false },
            value -> title + ": " + (value ? "An" : "Aus"),
            setCurrentValue
        );
    }

    protected <T> LabelCarousel<T> addCarouselButton(T[] values, Function<T, String> getText, Consumer<T> setCurrentValue) {
        Button button = addButton("");
        LabelCarousel<T> labelCarousel = new LabelCarousel<>(button, values, getText);
        menuGroup.addElement(new MenuElement(button, () -> {
            CarouselUtil.changeValue(labelCarousel, 1);
            setCurrentValue.accept(labelCarousel.getValue());
        }));
        return labelCarousel;
    }

    private Button addButton(String text) {
        Button button = new Button(text);
        button.setFontSize(20);
        button.setTextHAlignment(HAlignment.Center);
        button.setPreferredSize(new Vector3f(containerWidth, 30, 0));
        container.addChild(button);
        return button;
    }
}
