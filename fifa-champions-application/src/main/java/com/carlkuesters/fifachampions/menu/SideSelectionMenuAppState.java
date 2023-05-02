package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.ControllerAppState;
import com.carlkuesters.fifachampions.game.Controller;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.SpringGridLayout;

import java.util.HashMap;

public abstract class SideSelectionMenuAppState extends MenuAppState {

    private HashMap<Controller, MenuGroup> controllerMenuGroups = new HashMap<>();
    private HashMap<Controller, SideSelectionControllerContainer> controllerContainers = new HashMap<>();

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

        ControllerAppState controllerAppState = getAppState(ControllerAppState.class);
        for (Controller controller : controllerAppState.getControllers().values()) {
            SideSelectionMenuGroup menuGroup = new SideSelectionMenuGroup(
                controller::getTeamSide,
                teamSide -> {
                    controller.setTeamSide(teamSide);
                    updateControllerContainer(controller);
                },
                () -> controllerAppState.getSettingsIndex(controller),
                controllerSettingsIndex -> {
                    controller.setSettings(controllerAppState.getSettings()[controllerSettingsIndex]);
                    updateControllerContainer(controller);
                },
                this::confirm
            );

            Container row = new Container();
            row.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
            row.setInsets(new Insets3f(0, rowPadding, 0, rowPadding));
            row.setBackground(null);

            Container controllerContainer = new Container();
            controllerContainer.setBackground(null);
            // Logo icon
            Panel controllerLogo = new Panel();
            IconComponent controllerIcon = new IconComponent("textures/controller.png");
            controllerIcon.setHAlignment(HAlignment.Center);
            controllerIcon.setVAlignment(VAlignment.Bottom);
            controllerIcon.setIconSize(new Vector2f(controllerLogoSize, controllerLogoSize));
            controllerLogo.setBackground(controllerIcon);
            controllerContainer.addChild(controllerLogo);
            // Settings text
            Label lblSettings = new Label("XXX");
            lblSettings.setFontSize(20);
            lblSettings.setTextHAlignment(HAlignment.Center);
            controllerContainer.addChild(lblSettings);

            row.addChild(controllerContainer);
            controllerContainers.put(controller, new SideSelectionControllerContainer(controllerIcon, lblSettings));
            containerInner.addChild(row);

            addMenuGroup(menuGroup);
            controllerMenuGroups.put(controller, menuGroup);
        }

        guiNode.attachChild(containerOuter);
    }

    @Override
    protected MenuGroup getMenuGroup(int joyId) {
        ControllerAppState controllerAppState = getAppState(ControllerAppState.class);
        return controllerMenuGroups.get(controllerAppState.getControllers().get(joyId));
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            for (Controller controller : getAppState(ControllerAppState.class).getControllers().values()) {
                updateControllerContainer(controller);
            }
            mainApplication.getCamera().setLocation(new Vector3f(0, 1, 5));
            mainApplication.getCamera().lookAtDirection(new Vector3f(0, 0.05f, -1), Vector3f.UNIT_Y);
        }
    }

    private void updateControllerContainer(Controller controller) {
        HAlignment hAlignment;
        if (controller.getTeamSide() == -1) {
            hAlignment = HAlignment.Right;
        } else if (controller.getTeamSide() == 1) {
            hAlignment = HAlignment.Left;
        } else {
            hAlignment = HAlignment.Center;
        }
        SideSelectionControllerContainer container = controllerContainers.get(controller);
        container.getControllerIcon().setHAlignment(hAlignment);
        container.getLblSettings().setTextHAlignment(hAlignment);
        container.getLblSettings().setText(getAppState(ControllerAppState.class).getSettingsName(controller));
    }

    protected abstract void confirm();

    @Override
    protected void showDetails() {
        super.showDetails();
        openMenu(ControllerSettingsMenuAppState.class);
    }
}
