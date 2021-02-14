package com.carlkuesters.fifachampions.menu;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;

import java.util.LinkedList;

public class MenuGroup {

    public MenuGroup(Runnable back) {
        this.back = back;
        elements = new LinkedList<>();
    }
    private Runnable back;
    private LinkedList<MenuElement> elements;
    private MenuElement activeElement;
    private ColorRGBA defaultElementBackgroundColor;

    public void addElement(MenuElement element) {
        elements.add(element);
        if (activeElement == null) {
            TbtQuadBackgroundComponent background = (TbtQuadBackgroundComponent) element.getPanel().getBackground();
            defaultElementBackgroundColor = background.getColor();
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
                    primaryAxisDistance = FastMath.abs(distance.getY());
                    secondaryAxisDistance = FastMath.sign(direction.getX()) * distance.getX();
                } else {
                    primaryAxisDistance = FastMath.abs(distance.getX());
                    secondaryAxisDistance = FastMath.sign(direction.getY()) * distance.getY();
                }
                if (secondaryAxisDistance >= 0) {
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
            setBackground(this.activeElement, defaultElementBackgroundColor);
        }
        this.activeElement = activeElement;
        setBackground(activeElement, ColorRGBA.Red);
    }

    private void setBackground(MenuElement element, ColorRGBA color) {
        TbtQuadBackgroundComponent background = (TbtQuadBackgroundComponent) element.getPanel().getBackground();
        background.setColor(color);
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
