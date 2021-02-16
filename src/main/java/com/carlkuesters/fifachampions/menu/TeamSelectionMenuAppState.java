package com.carlkuesters.fifachampions.menu;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.SpringGridLayout;

public class TeamSelectionMenuAppState extends MenuAppState {

    private IconComponent[] controllerIcons;

    @Override
    protected void initMenu() {
        addTitle("Seitenauswahl");

        MenuGroup menuGroup = new MenuGroup(() -> openMenu(PauseIngameMenuAppState.class));

        int containerWidth = 600;
        int containerHeight = 400;
        int containerX = ((totalWidth / 2) - (containerWidth / 2));
        int containerY = ((totalHeight / 2) + (containerHeight / 2));
        int rowPadding = 20;
        int controllerLogoSize = 70;

        Container containerOuter = new Container();
        containerOuter.setLocalTranslation(containerX, containerY, 0);
        containerOuter.setPreferredSize(new Vector3f(containerWidth, containerHeight, 0));

        Container containerInner = new Container();
        containerInner.setInsets(new Insets3f((rowPadding / 2f), 0, (rowPadding / 2f), 0));
        containerInner.setBackground(null);
        containerOuter.addChild(containerInner);

        controllerIcons = new IconComponent[mainApplication.getInputManager().getJoysticks().length];
        for (int i = 0; i < controllerIcons.length; i++) {
            Container row = new Container();
            row.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
            row.setInsets(new Insets3f(0, rowPadding, 0, rowPadding));
            row.setBackground(null);

            Panel controllerLogo = new Panel();
            IconComponent controllerIcon = new IconComponent("textures/logo.png");
            if (Math.random() < 0.33) {
                controllerIcon.setHAlignment(HAlignment.Left);
            } else if (Math.random() < 0.5) {
                controllerIcon.setHAlignment(HAlignment.Center);
            } else {
                controllerIcon.setHAlignment(HAlignment.Right);
            }
            controllerIcon.setVAlignment(VAlignment.Center);
            controllerIcon.setIconSize(new Vector2f(controllerLogoSize, controllerLogoSize));
            controllerIcons[i] = controllerIcon;
            controllerLogo.setBackground(controllerIcon);
            row.addChild(controllerLogo);

            containerInner.addChild(row);
        }

        guiNode.attachChild(containerOuter);

        addMenuGroup(menuGroup);
    }

    public void updateControllerSide(int controllerIndex, int side) {
        HAlignment hAlignment;
        if (side == 1) {
            hAlignment = HAlignment.Left;
        } else if (side == -1) {
            hAlignment = HAlignment.Right;
        } else {
            hAlignment = HAlignment.Center;
        }
        controllerIcons[controllerIndex].setHAlignment(hAlignment);
    }
}
