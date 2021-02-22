package com.carlkuesters.fifachampions.menu;

import com.jme3.input.Joystick;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.SpringGridLayout;

import java.util.HashMap;

public abstract class SideSelectionMenuAppState extends MenuAppState {

    private HashMap<Integer, MenuGroup> controllerMenuGroups = new HashMap<>();
    private HashMap<Integer, IconComponent> controllerIcons = new HashMap<>();

    @Override
    protected void initMenu() {
        addTitle("Seitenauswahl");

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

        for (Joystick joystick : mainApplication.getInputManager().getJoysticks()) {
            SideSelectionMenuGroup menuGroup = new SideSelectionMenuGroup(
                () -> getTeamSide(joystick.getJoyId()),
                teamSide -> {
                    setTeamSide(joystick.getJoyId(), teamSide);
                    updateControllerSide(joystick.getJoyId(), teamSide);
                },
                this::confirm
            );

            Container row = new Container();
            row.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
            row.setInsets(new Insets3f(0, rowPadding, 0, rowPadding));
            row.setBackground(null);

            Panel controllerLogo = new Panel();
            IconComponent controllerIcon = new IconComponent("textures/controller.png");
            controllerIcon.setVAlignment(VAlignment.Center);
            controllerIcon.setIconSize(new Vector2f(controllerLogoSize, controllerLogoSize));
            controllerIcons.put(joystick.getJoyId(), controllerIcon);
            controllerLogo.setBackground(controllerIcon);
            row.addChild(controllerLogo);

            containerInner.addChild(row);

            addMenuGroup(menuGroup);

            controllerMenuGroups.put(joystick.getJoyId(), menuGroup);
        }

        guiNode.attachChild(containerOuter);
    }

    @Override
    protected MenuGroup getMenuGroup(int joyId) {
        return controllerMenuGroups.get(joyId);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            for (Joystick joystick : mainApplication.getInputManager().getJoysticks()) {
                updateControllerSide(joystick.getJoyId(), getTeamSide(joystick.getJoyId()));
            }
            mainApplication.getCamera().setLocation(new Vector3f(0, 1, 5));
            mainApplication.getCamera().lookAtDirection(new Vector3f(0, 0.05f, -1), Vector3f.UNIT_Y);
        }
    }

    private void updateControllerSide(int joyId, int teamSide) {
        HAlignment hAlignment;
        if (teamSide == -1) {
            hAlignment = HAlignment.Left;
        } else if (teamSide == 1) {
            hAlignment = HAlignment.Right;
        } else {
            hAlignment = HAlignment.Center;
        }
        controllerIcons.get(joyId).setHAlignment(hAlignment);
    }

    protected abstract int getTeamSide(int joyId);

    protected abstract void setTeamSide(int joyId, int teamSide);

    protected abstract void confirm();
}
