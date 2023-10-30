package com.carlkuesters.fifachampions.menu;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedList;

public class ElementsMenuGroup extends MenuGroup {

    public ElementsMenuGroup() {
        elements = new LinkedList<>();
        defaultElementBackgroundColors = new HashMap<>();
    }
    private static final ColorRGBA COLOR_ACTIVE = new ColorRGBA(1, 1, 1, 0.75f);
    private LinkedList<MenuElement> elements;
    @Getter
    private MenuElement activeElement;
    private HashMap<MenuElement, ColorRGBA> defaultElementBackgroundColors;
    private TbtQuadBackgroundComponent defaultBackground;

    public void addElement(MenuElement element) {
        elements.add(element);

        TbtQuadBackgroundComponent background = (TbtQuadBackgroundComponent) element.getPanel().getBackground();
        ColorRGBA backgroundColor = ((background != null) ? background.getColor() : null);
        defaultElementBackgroundColors.put(element, backgroundColor);
        if ((defaultBackground == null) && (background != null)) {
            defaultBackground = background;
        }

        if (activeElement == null) {
            setActiveElement(element);
        }
    }

    @Override
    public void primaryNavigateLeft() {
        super.primaryNavigateLeft();
        primaryNavigate(new Vector3f(-1, 0, 0));
    }

    @Override
    public void primaryNavigateRight() {
        super.primaryNavigateRight();
        primaryNavigate(new Vector3f(1, 0, 0));
    }

    @Override
    public void primaryNavigateUp() {
        super.primaryNavigateUp();
        primaryNavigate(new Vector3f(0, 1, 0));
    }

    @Override
    public void primaryNavigateDown() {
        super.primaryNavigateDown();
        primaryNavigate(new Vector3f(0, -1, 0));
    }

    private void primaryNavigate(Vector3f direction) {
        float newActiveElementCenterDistanceSquared = Float.MAX_VALUE;
        MenuElement newActiveElement = null;
        Vector3f activeElementCenter = activeElement.getPanel().getWorldTranslation().add(activeElement.getPanel().getSize().divide(2));
        for (MenuElement element : elements) {
            if (element != activeElement) {
                Vector3f elementCenter = element.getPanel().getWorldTranslation().add(element.getPanel().getSize().divide(2));
                Vector3f elementCenterDistance = elementCenter.subtract(activeElementCenter);
                if (direction.angleBetween(elementCenterDistance.normalize()) < FastMath.QUARTER_PI) {
                    float centerDistanceSquared = elementCenterDistance.lengthSquared();
                    if (centerDistanceSquared < newActiveElementCenterDistanceSquared) {
                        newActiveElementCenterDistanceSquared = centerDistanceSquared;
                        newActiveElement = element;
                    }
                }
            }
        }
        if (newActiveElement != null) {
            setActiveElement(newActiveElement);
        }
    }

    public void setActiveElement(MenuElement newActiveElement) {
        MenuElement oldActiveElement = this.activeElement;
        activeElement = newActiveElement;
        if (oldActiveElement != null) {
            updateBackgroundColor(oldActiveElement);
        }
        updateBackgroundColor(activeElement);
    }

    protected void updateBackgroundColor(MenuElement element) {
        setBackground(element, getBackgroundColor(element));
    }

    protected ColorRGBA getBackgroundColor(MenuElement element) {
        if (element == activeElement) {
            return COLOR_ACTIVE;
        }
        return defaultElementBackgroundColors.get(element);
    }

    protected void setBackground(MenuElement element, ColorRGBA color) {
        Panel panel = element.getPanel();
        if (color != null) {
            TbtQuadBackgroundComponent background = defaultBackground.clone();
            background.setColor(color);
            panel.setBackground(background);
        } else {
            panel.setBackground(null);
        }
    }

    @Override
    public void confirm() {
        if (activeElement != null) {
            activeElement.getAction().run();
        } else {
            super.confirm();
        }
    }
}
