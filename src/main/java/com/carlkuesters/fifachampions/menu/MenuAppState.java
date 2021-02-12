package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.game.BaseDisplayAppState;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;

import java.util.LinkedList;

public abstract class MenuAppState extends BaseDisplayAppState {

    public MenuAppState() {
        menuGroups = new LinkedList<>();
        menuJoystickListener = new MenuJoystickListener(this::getMenuGroup);
    }
    protected Node guiNode;
    private LinkedList<MenuGroup> menuGroups;
    private MenuJoystickListener menuJoystickListener;
    protected int totalWidth;
    protected int totalHeight;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        totalWidth = mainApplication.getContext().getSettings().getWidth();
        totalHeight = mainApplication.getContext().getSettings().getHeight();
        if (guiNode == null) {
            guiNode = new Node();
            initMenu();
        }
        mainApplication.getGuiNode().attachChild(guiNode);
        mainApplication.getInputManager().addRawInputListener(menuJoystickListener);
    }

    protected abstract void initMenu();

    protected void addMenuGroup(MenuGroup menuGroup) {
        menuGroups.add(menuGroup);
    }

    private MenuGroup getMenuGroup(int controllerIndex) {
        // TODO
        return menuGroups.get(0);
    }

    protected void close() {
        mainApplication.getStateManager().detach(this);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getGuiNode().detachChild(guiNode);
        mainApplication.getInputManager().removeRawInputListener(menuJoystickListener);
    }
}
