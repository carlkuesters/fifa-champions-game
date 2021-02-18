package com.carlkuesters.fifachampions.menu;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;

import java.util.HashMap;
import java.util.LinkedList;

public class ElementsMenuGroup extends MenuGroup {

    public ElementsMenuGroup(Runnable back) {
        super(back);
        elements = new LinkedList<>();
        defaultElementBackgroundColors = new HashMap<>();
    }
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

    @Override
    public void navigateLeft(int joyId) {
        super.navigateLeft(joyId);
        navigate(new Vector3f(-1, 0, 0));
    }

    @Override
    public void navigateRight(int joyId) {
        super.navigateRight(joyId);
        navigate(new Vector3f(1, 0, 0));
    }

    @Override
    public void navigateUp(int joyId) {
        super.navigateUp(joyId);
        navigate(new Vector3f(0, 1, 0));
    }

    @Override
    public void navigateDown(int joyId) {
        super.navigateDown(joyId);
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

    @Override
    public void confirm(int joyId) {
        if (activeElement != null) {
            activeElement.getAction().run();
        } else {
            super.confirm(joyId);
        }
    }
}
