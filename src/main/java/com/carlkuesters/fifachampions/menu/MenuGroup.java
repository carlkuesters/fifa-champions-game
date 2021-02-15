package com.carlkuesters.fifachampions.menu;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;

import java.util.HashMap;
import java.util.LinkedList;

public class MenuGroup {

    public MenuGroup(Runnable back) {
        this.back = back;
        elements = new LinkedList<>();
        defaultElementBackgroundColors = new HashMap();
    }
    private Runnable back;
    private LinkedList<MenuElement> elements;
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

    public void navigateLeft() {
        navigate(new Vector3f(-1, 0, 0));
    }

    public void navigateRight() {
        navigate(new Vector3f(1, 0, 0));
    }

    public void navigateUp() {
        navigate(new Vector3f(0, 1, 0));
    }

    public void navigateDown() {
        navigate(new Vector3f(0, -1, 0));
    }

    private void navigate(Vector3f direction) {
        float minimumPrimaryAxisDistance = Float.MAX_VALUE;
        float minimumSecondaryAxisDistance = Float.MAX_VALUE;
        MenuElement newActiveElement = null;
        for (MenuElement element : elements) {
            if (element != activeElement) {
                Vector3f activeElementCorner = activeElement.getPanel().getWorldTranslation().clone();
                Vector3f elementCorner = element.getPanel().getWorldTranslation().clone();
                if (direction.getX() > 0) {
                    activeElementCorner.addLocal(activeElement.getPanel().getSize().getX(), 0, 0);
                } else if (direction.getX() < 0) {
                    elementCorner.addLocal(element.getPanel().getSize().getX(), 0, 0);
                }
                if (direction.getY() > 0) {
                    activeElementCorner.addLocal(0, activeElement.getPanel().getSize().getY(), 0);
                } else if (direction.getY() < 0) {
                    elementCorner.addLocal(0, element.getPanel().getSize().getY(), 0);
                }
                Vector3f distance = elementCorner.subtract(activeElementCorner);
                float primaryAxisDistance;
                float secondaryAxisDistance;
                if (direction.getX() != 0) {
                    primaryAxisDistance = FastMath.sign(direction.getX()) * distance.getX();
                    secondaryAxisDistance = FastMath.abs(distance.getY());
                } else {
                    primaryAxisDistance = FastMath.sign(direction.getY()) * distance.getY();
                    secondaryAxisDistance = FastMath.abs(distance.getX());
                }
                if (primaryAxisDistance >= 0) {
                    if ((primaryAxisDistance < minimumPrimaryAxisDistance)
                    || ((primaryAxisDistance == minimumPrimaryAxisDistance) && (secondaryAxisDistance < minimumSecondaryAxisDistance))) {
                        minimumPrimaryAxisDistance = primaryAxisDistance;
                        minimumSecondaryAxisDistance = secondaryAxisDistance;
                        newActiveElement = element;
                    }
                }
            }
        }
        if (newActiveElement != null) {
            setActiveElement(newActiveElement);
        }
    }

    public void setActiveElement(MenuElement activeElement) {
        if (this.activeElement != null) {
            setBackground(this.activeElement, defaultElementBackgroundColors.get(this.activeElement));
        }
        this.activeElement = activeElement;
        setBackground(activeElement, ColorRGBA.Red);
    }

    private void setBackground(MenuElement element, ColorRGBA color) {
        Panel panel = element.getPanel();
        if (color != null) {
            TbtQuadBackgroundComponent background = defaultBackground.clone();
            background.setColor(color);
            panel.setBackground(background);
        } else {
            panel.setBackground(null);
        }
    }

    public void confirm() {
        if (activeElement != null) {
            activeElement.getAction().run();
        } else {
            back();
        }
    }

    public void back() {
        back.run();
    }
}
