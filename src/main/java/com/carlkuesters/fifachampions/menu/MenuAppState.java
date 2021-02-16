package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.game.BaseDisplayAppState;
import com.carlkuesters.fifachampions.joystick.MenuJoystickSubListener;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;

import java.util.LinkedList;

public abstract class MenuAppState extends BaseDisplayAppState {

    public MenuAppState() {
        guiNode = new Node();
        menuGroups = new LinkedList<>();
        menuJoystickSubListener = new MenuJoystickSubListener(this::getMenuGroup);
    }
    protected Node guiNode;
    private LinkedList<MenuGroup> menuGroups;
    private MenuJoystickSubListener menuJoystickSubListener;
    protected int totalWidth;
    protected int totalHeight;
    protected boolean autoEnabled;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        totalWidth = mainApplication.getContext().getSettings().getWidth();
        totalHeight = mainApplication.getContext().getSettings().getHeight();
        initMenu();
        setEnabled(autoEnabled);
    }

    protected abstract void initMenu();

    protected void addTitle(String title) {
        int titleMarginTop = 70;
        int titleWidth = 300;
        Label lblTitle = new Label(title);
        lblTitle.setFontSize(32);
        lblTitle.setLocalTranslation(new Vector3f((totalWidth / 2f) - (titleWidth / 2f), totalHeight - titleMarginTop, 0));
        lblTitle.setPreferredSize(new Vector3f(titleWidth, 0, 0));
        lblTitle.setTextHAlignment(HAlignment.Center);
        guiNode.attachChild(lblTitle);
    }

    protected void addMenuGroup(MenuGroup menuGroup) {
        menuGroups.add(menuGroup);
    }

    private MenuGroup getMenuGroup(int controllerIndex) {
        // TODO
        return menuGroups.get(0);
    }

    protected void openMenu(Class<? extends MenuAppState> menuAppStateClass) {
        close();
        getAppState(menuAppStateClass).setEnabled(true);
    }

    protected void close() {
        setEnabled(false);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            mainApplication.getGuiNode().attachChild(guiNode);
            mainApplication.getJoystickListener().setMenuSubListener(menuJoystickSubListener);
        } else {
            mainApplication.getGuiNode().detachChild(guiNode);
            mainApplication.getJoystickListener().setMenuSubListener(null);
        }
    }
}
