package com.carlkuesters.fifachampions.menu;

import com.jme3.input.Joystick;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.SpringGridLayout;

import java.util.HashMap;

public abstract class TeamSelectionMenuAppState extends MenuAppState {

    private HashMap<Integer, IconComponent> controllerIcons = new HashMap<>();

    @Override
    protected void initMenu() {
        addTitle("Seitenauswahl");

        TeamSelectionMenuGroup menuGroup = new TeamSelectionMenuGroup(this::back, this::getTeamSide, (joyId, teamSide) -> {
            setTeamSide(joyId, teamSide);
            updateControllerSide(joyId, teamSide);
        }, this::confirm);

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
        }

        guiNode.attachChild(containerOuter);

        addMenuGroup(menuGroup);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            for (Joystick joystick : mainApplication.getInputManager().getJoysticks()) {
                updateControllerSide(joystick.getJoyId(), getTeamSide(joystick.getJoyId()));
            }
        }
    }

    private void updateControllerSide(int joyId, int teamSide) {
        HAlignment hAlignment;
        if (teamSide == 1) {
            hAlignment = HAlignment.Left;
        } else if (teamSide == -1) {
            hAlignment = HAlignment.Right;
        } else {
            hAlignment = HAlignment.Center;
        }
        controllerIcons.get(joyId).setHAlignment(hAlignment);
    }

    protected abstract void back();

    protected abstract int getTeamSide(int joyId);

    protected abstract void setTeamSide(int joyId, int teamSide);

    protected abstract void confirm();
}
