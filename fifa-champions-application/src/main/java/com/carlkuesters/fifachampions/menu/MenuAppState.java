package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.BaseDisplayAppState;
import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.joystick.MenuJoystickSubListener;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;

import java.util.LinkedList;

public abstract class MenuAppState extends BaseDisplayAppState {

    public MenuAppState() {
        guiNode = new Node();
        menuGroups = new LinkedList<>();
        menuJoystickSubListener = new MenuJoystickSubListener(this::back, this::showDetails, this::getMenuGroup);
    }
    protected Node guiNode;
    private LinkedList<MenuGroup> menuGroups;
    protected MenuJoystickSubListener menuJoystickSubListener;
    protected int totalWidth;
    protected int totalHeight;
    protected MenuMode mode = MenuMode.DEFAULT;
    private MenuAppState parentMenuAppState;
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
        int titleWidth = 400;
        Label lblTitle = new Label(title);
        lblTitle.setFontSize(32);
        lblTitle.setLocalTranslation(new Vector3f((totalWidth / 2f) - (titleWidth / 2f), totalHeight - titleMarginTop, 0));
        lblTitle.setPreferredSize(new Vector3f(titleWidth, 0, 0));
        lblTitle.setTextHAlignment(HAlignment.Center);
        lblTitle.setColor(ColorRGBA.White);
        guiNode.attachChild(lblTitle);
    }

    protected void addMenuGroup(MenuGroup menuGroup) {
        menuGroups.add(menuGroup);
    }

    protected MenuGroup getMenuGroup(int joyId) {
        int menuGroupIndex = 0;
        if (menuGroups.size() > 1) {
            Controller controller = mainApplication.getControllers().get(joyId);
            Integer controllerTeamIndex = controller.getTeamIndex();
            if (controllerTeamIndex == null) {
                return null;
            }
            menuGroupIndex = controllerTeamIndex;
        }
        return menuGroups.get(menuGroupIndex);
    }

    protected void back() {
        if (parentMenuAppState != null) {
            openMenu(parentMenuAppState);
        } else {
            close();
        }
    }

    protected void showDetails() {

    }

    protected void openMenu(Class<? extends MenuAppState> menuAppStateClass) {
        openMenu(getAppState(menuAppStateClass));
    }

    private void openMenu(MenuAppState newMenu) {
        close();
        if (newMenu.mode != MenuMode.ROOT) {
            if ((newMenu.parentMenuAppState == null) || (newMenu.mode == MenuMode.FREE_CHILD)) {
                newMenu.parentMenuAppState = this;
            }
        }
        newMenu.setEnabled(true);
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
