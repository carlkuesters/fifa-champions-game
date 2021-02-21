package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.Main;
import com.carlkuesters.fifachampions.game.Formation;
import com.jme3.math.ColorRGBA;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FormationMenuGroup extends ElementsMenuGroup implements Carousel {

    public FormationMenuGroup(Runnable back, Supplier<Formation> getFormation, Consumer<Formation> setFormation, BiConsumer<MenuElement, MenuElement> swapPlayers) {
        super(back);
        this.getFormation = getFormation;
        this.setFormation = setFormation;
        this.swapPlayers = swapPlayers;
        markedForSwitchElements = new HashMap<>();
    }
    private Supplier<Formation> getFormation;
    private Consumer<Formation> setFormation;
    private MenuElement selectedElement;
    private BiConsumer<MenuElement, MenuElement> swapPlayers;
    private HashMap<MenuElement, Boolean> markedForSwitchElements;

    @Override
    public void secondaryNavigateLeft(int joyId) {
        super.secondaryNavigateLeft(joyId);
        CarouselUtil.changeValue(this, joyId, -1);
    }

    @Override
    public void secondaryNavigateRight(int joyId) {
        super.secondaryNavigateRight(joyId);
        CarouselUtil.changeValue(this, joyId, 1);
    }

    @Override
    public int getCarouselValue(int joyId) {
        Formation formation = getFormation.get();
        for (int i = 0; i < Main.FORMATIONS.length; i++) {
            if (formation == Main.FORMATIONS[i]) {
                return i;
            }
        }
        return -999;
    }

    @Override
    public void setCarouselValue(int joyId, int value) {
        setFormation.accept(Main.FORMATIONS[value]);
    }

    @Override
    public int getCarouselMaximumValue(int joyId) {
        return (Main.FORMATIONS.length - 1);
    }

    @Override
    public void confirm(int joyId) {
        MenuElement activeElement = getActiveElement();
        if (selectedElement == null) {
            setSelectedElement(activeElement);
        } else {
            if (selectedElement != activeElement) {
                swapPlayers.accept(selectedElement, activeElement);
            }
            setSelectedElement(null);
        }
    }

    private void setSelectedElement(MenuElement newSelectedElement) {
        MenuElement oldSelectedElement = this.selectedElement;
        selectedElement = newSelectedElement;
        if (oldSelectedElement != null) {
            updateBackgroundColor(oldSelectedElement);
        }
        if (selectedElement != null) {
            updateBackgroundColor(selectedElement);
        }
    }

    @Override
    protected ColorRGBA getBackgroundColor(MenuElement element) {
        if (element == selectedElement) {
            return ColorRGBA.Blue;
        } else if (isMarkedForSwitch(element)) {
            return ColorRGBA.Green;
        }
        return super.getBackgroundColor(element);
    }

    public void setMarkedForSwitch(MenuElement menuElement, boolean markedForSwitch) {
        markedForSwitchElements.put(menuElement, markedForSwitch);
        updateBackgroundColor(menuElement);
    }

    private boolean isMarkedForSwitch(MenuElement menuElement) {
        Boolean isMarkedForSwitch = markedForSwitchElements.get(menuElement);
        return ((isMarkedForSwitch != null) && isMarkedForSwitch);
    }
}
