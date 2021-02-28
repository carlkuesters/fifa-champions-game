package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameAppState;
import com.carlkuesters.fifachampions.GameCreationInfo;
import com.carlkuesters.fifachampions.game.BaseDisplayAppState;
import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.Team;
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
        menuJoystickSubListener = new MenuJoystickSubListener(this::back, this::getMenuGroup);
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
        lblTitle.setColor(ColorRGBA.White);
        guiNode.attachChild(lblTitle);
    }

    protected void addMenuGroup(MenuGroup menuGroup) {
        menuGroups.add(menuGroup);
    }

    protected MenuGroup getMenuGroup(int joyId) {
        int menuGroupIndex = 0;
        if (menuGroups.size() > 1) {
            Integer controllerTeamIndex = getControllerTeamIndex(joyId);
            if (controllerTeamIndex == null) {
                return null;
            }
            menuGroupIndex = controllerTeamIndex;
        }
        return menuGroups.get(menuGroupIndex);
    }

    private Integer getControllerTeamIndex(int joyId) {
        GameAppState gameAppState = (GameAppState) getAppState(GameAppState.class);
        if (gameAppState != null) {
            Controller controller = gameAppState.getControllers().get(joyId);
            Team team = controller.getTeam();
            if (team != null) {
                return ((team.getSide() == 1) ? 0 : 1);
            }
        } else {
            GameCreationInfo gameCreationInfo = mainApplication.getGameCreationInfo();
            int sideSelectionSide = gameCreationInfo.getControllerTeamSides().get(joyId);
            if (sideSelectionSide != 0) {
                return ((sideSelectionSide == -1) ? 0 : 1);
            }
        }
        return null;
    }

    protected abstract void back();

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
