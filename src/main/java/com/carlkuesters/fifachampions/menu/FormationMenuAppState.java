package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.game.Formation;
import com.carlkuesters.fifachampions.game.Player;
import com.carlkuesters.fifachampions.game.PlayerPosition;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.SpringGridLayout;

import java.util.HashMap;

public abstract class FormationMenuAppState<P> extends MenuAppState {

    private int containerMarginOutside = 150;
    private int containerMarginBetween = 400;
    private int containerY;
    private int containerWidth;
    private int playerImageBigHeight = 60;
    private int formationLeftAndRightColumnWidth = 25;
    private int formationTrikotImageSize = 30;
    private FormationMenuGroup[] menuGroups = new FormationMenuGroup[2];
    private FieldPlayerContainer[][] fieldPlayers = new FieldPlayerContainer[2][11];
    private ReservePlayerContainer[][] reservePlayers = new ReservePlayerContainer[2][20];
    private HashMap<MenuElement, Integer> fieldPlayerElementIndices = new HashMap<>();
    private HashMap<MenuElement, Integer> reservePlayerElementIndices = new HashMap<>();

    @Override
    protected void initMenu() {
        addTitle("Aufstellung");
        containerY = (totalHeight - 80);
        containerWidth = ((totalWidth - (2 * containerMarginOutside) - containerMarginBetween) / 2);
        addSide(-1);
        addSide(1);
    }

    private void addSide(int side) {
        int teamIndex = ((side + 1) / 2);

        FormationMenuGroup menuGroup = new FormationMenuGroup(
            () -> getFormation(teamIndex),
            formation -> {
                setFormation(teamIndex, formation);
                updateFieldPlayers(teamIndex);
            },
            (element1, element2) -> swapPlayers(teamIndex, element1, element2)
        );
        menuGroups[teamIndex] = menuGroup;

        int containerX = getContainerX(side);
        Container container = new Container();
        container.setLocalTranslation(containerX, containerY, 0);

        Container playerDetailsRow = new Container();
        playerDetailsRow.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        playerDetailsRow.setBackground(null);

        Container containerPlayerDetailsLeft = createPlayerDetailsBig();
        playerDetailsRow.addChild(containerPlayerDetailsLeft);
        Container containerPlayerDetailsRight = createPlayerDetailsBig();
        playerDetailsRow.addChild(containerPlayerDetailsRight);

        container.addChild(playerDetailsRow);

        Panel formationBackground = new Panel();
        IconComponent formationBackgroundIcon = new IconComponent("textures/formation_background.png");
        formationBackgroundIcon.setIconSize(new Vector2f(containerWidth, containerWidth));
        formationBackground.setBackground(formationBackgroundIcon);
        container.addChild(formationBackground);

        Container reservePlayersContainer = new Container();
        reservePlayersContainer.setBackground(null);
        int reservePlayerIndex = 0;
        for (int y = 0; y < 4; y++) {
            Container reservePlayersRow = new Container();
            reservePlayersRow.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
            reservePlayersRow.setBackground(null);
            for (int x = 0; x < 5; x++) {
                ReservePlayerContainer reservePlayerContainer = createReservePlayer(menuGroup);
                reservePlayersRow.addChild(reservePlayerContainer.getContainer());
                reservePlayers[teamIndex][reservePlayerIndex] = reservePlayerContainer;
                reservePlayerIndex++;
            }
            reservePlayersContainer.addChild(reservePlayersRow);
        }
        container.addChild(reservePlayersContainer);

        guiNode.attachChild(container);

        for (int i = 0; i < fieldPlayers[teamIndex].length; i++) {
            FieldPlayerContainer fieldPlayerContainer = createFieldPlayer(menuGroup);
            guiNode.attachChild(fieldPlayerContainer.getContainer());
            fieldPlayers[teamIndex][i] = fieldPlayerContainer;
        }

        addMenuGroup(menuGroup);
    }

    private int getContainerX(int side) {
        int containerX = ((totalWidth / 2) + (side * (containerMarginBetween / 2)));
        if (side == -1) {
            containerX -= containerWidth;
        }
        return containerX;
    }

    private Container createPlayerDetailsBig() {
        Container container = new Container();
        container.setLayout(new SpringGridLayout(Axis.X, Axis.Y));

        Panel playerImage = new Panel();
        IconComponent playerIcon = new IconComponent("textures/player_face.png");
        playerIcon.setIconSize(new Vector2f(playerImageBigHeight, playerImageBigHeight));
        playerIcon.setHAlignment(HAlignment.Center);
        playerIcon.setVAlignment(VAlignment.Center);
        playerImage.setBackground(playerIcon);
        container.addChild(playerImage);

        Container skillsColumn = new Container();
        skillsColumn.setBackground(null);
        for (int y = 0; y < 3; y++) {
            Container skillsRow = new Container();
            skillsRow.setBackground(null);
            skillsRow.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
            for (int x = 0; x < 3; x++) {
                Label lblSkill = new Label("XXX: 99");
                lblSkill.setTextHAlignment(HAlignment.Center);
                lblSkill.setTextVAlignment(VAlignment.Center);
                lblSkill.setFontSize(12);
                skillsRow.addChild(lblSkill);
            }
            skillsColumn.addChild(skillsRow);
        }
        container.addChild(skillsColumn);

        return container;
    }

    private ReservePlayerContainer createReservePlayer(FormationMenuGroup menuGroup) {
        Container container = new Container();

        Container topRow = new Container();
        topRow.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        topRow.setBackground(null);

        Label lblPosition = new Label("");
        lblPosition.setTextHAlignment(HAlignment.Center);
        lblPosition.setTextVAlignment(VAlignment.Center);
        lblPosition.setFontSize(12);
        topRow.addChild(lblPosition);

        int playerImageHeight = 40;
        Panel playerImage = new Panel();
        IconComponent playerIcon = new IconComponent("textures/player_face.png");
        playerIcon.setIconSize(new Vector2f(playerImageHeight, playerImageHeight));
        playerIcon.setHAlignment(HAlignment.Center);
        playerImage.setBackground(playerIcon);
        topRow.addChild(playerImage);

        Label lblSkill = new Label("");
        lblSkill.setTextHAlignment(HAlignment.Center);
        lblSkill.setTextVAlignment(VAlignment.Center);
        lblSkill.setFontSize(12);
        topRow.addChild(lblSkill);

        container.addChild(topRow);

        Label lblName = new Label("");
        lblName.setTextHAlignment(HAlignment.Center);
        lblName.setTextVAlignment(VAlignment.Center);
        lblName.setFontSize(12);
        container.addChild(lblName);

        MenuElement menuElement = new MenuElement(container, this::confirm);
        menuGroup.addElement(menuElement);

        return new ReservePlayerContainer(container, lblPosition, lblSkill, lblName, menuElement);
    }

    private FieldPlayerContainer createFieldPlayer(FormationMenuGroup menuGroup) {
        Container container = new Container();
        container.setBackground(null);

        Container topRow = new Container();
        topRow.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        topRow.setBackground(null);

        Label lblSkill = new Label("");
        lblSkill.setPreferredSize(new Vector3f(formationLeftAndRightColumnWidth, 0, 0));
        lblSkill.setTextHAlignment(HAlignment.Center);
        lblSkill.setTextVAlignment(VAlignment.Bottom);
        lblSkill.setFontSize(12);
        topRow.addChild(lblSkill);

        Panel trikotImage = new Panel();
        IconComponent trikotIcon = new IconComponent("textures/player_face.png");
        trikotIcon.setIconSize(new Vector2f(formationTrikotImageSize, formationTrikotImageSize));
        trikotIcon.setHAlignment(HAlignment.Center);
        trikotImage.setBackground(trikotIcon);
        topRow.addChild(trikotImage);

        Panel placeholderRight = new Panel();
        placeholderRight.setPreferredSize(new Vector3f(formationLeftAndRightColumnWidth, formationLeftAndRightColumnWidth, 0));
        placeholderRight.setBackground(null);
        topRow.addChild(placeholderRight);

        container.addChild(topRow);

        Label lblName = new Label("");
        lblName.setTextHAlignment(HAlignment.Center);
        lblName.setTextVAlignment(VAlignment.Center);
        lblName.setFontSize(12);
        container.addChild(lblName);

        MenuElement menuElement = new MenuElement(container, this::confirm);
        menuGroup.addElement(menuElement);

        return new FieldPlayerContainer(container, lblSkill, lblName, menuElement);
    }

    protected abstract Formation getFormation(int teamIndex);

    protected abstract void setFormation(int teamIndex, Formation formation);

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            for (int teamIndex = 0; teamIndex < 2; teamIndex++) {
                updateFieldPlayers(teamIndex);
                updateReservePlayers(teamIndex);
            }
        }
    }

    private void updateFieldPlayers(int teamIndex) {
        Formation formation = getFormation(teamIndex);
        P[] fieldPlayers = getFieldPlayers(teamIndex);
        for (int playerIndex = 0; playerIndex < fieldPlayers.length; playerIndex++) {
            P playerObject = fieldPlayers[playerIndex];
            updateFieldPlayer(teamIndex, playerIndex, getPlayer(playerObject), isMarkedForSwitch(playerObject), formation.getLocation(playerIndex));
        }
    }

    protected abstract P[] getFieldPlayers(int teamIndex);

    private void updateFieldPlayer(int teamIndex, int playerIndex, Player player, boolean markedForSwitch, Vector2f formationLocation) {
        String name = player.getName();
        int skill = ((playerIndex == 0) ? player.getGoalkeeperSkills().getAverageSkill() : player.getFieldPlayerSkills().getAverageSkill());

        FieldPlayerContainer fieldPlayerContainer = fieldPlayers[teamIndex][playerIndex];
        fieldPlayerContainer.getLblName().setText(name);
        fieldPlayerContainer.getLblSkill().setText("" + skill);
        float x = getFormationPlayerX(teamIndex, formationLocation.getY());
        float y = getFormationPlayerY(formationLocation.getX());
        fieldPlayerContainer.getContainer().setLocalTranslation(x, y, 1);

        menuGroups[teamIndex].setMarkedForSwitch(fieldPlayerContainer.getMenuElement(), markedForSwitch);

        fieldPlayerElementIndices.put(fieldPlayerContainer.getMenuElement(), playerIndex);
    }

    private float getFormationPlayerX(int teamIndex, float formationY) {
        int side = ((teamIndex == 0) ? -1 : 1);
        int startX = getContainerX(side);
        int maximumDistanceX = (containerWidth - ((2 * formationLeftAndRightColumnWidth) + formationTrikotImageSize));
        float progressX = ((formationY + 1) / 2);
        return (startX + (progressX * maximumDistanceX));
    }

    private float getFormationPlayerY(float formationX) {
        int startY = (containerY - playerImageBigHeight);
        int maximumDistanceY = (containerWidth - (formationTrikotImageSize + 20));
        float progressY = (1 - ((formationX + 1) / 2));
        return (startY - (progressY * maximumDistanceY));
    }

    private void updateReservePlayers(int teamIndex) {
        P[] reservePlayers = getReservePlayers(teamIndex);
        for (int playerIndex = 0; playerIndex < reservePlayers.length; playerIndex++) {
            P playerObject = reservePlayers[playerIndex];
            updateReservePlayer(teamIndex, playerIndex, getPlayer(playerObject), isMarkedForSwitch(playerObject));
        }
    }

    protected abstract P[] getReservePlayers(int teamIndex);

    protected abstract Player getPlayer(P playerObject);

    protected abstract boolean isMarkedForSwitch(P playerObject);

    private void updateReservePlayer(int teamIndex, int playerIndex, Player player, boolean markedForSwitch) {
        String name = player.getName();
        PlayerPosition position = player.getPosition();
        int skill = ((position == PlayerPosition.TW) ? player.getGoalkeeperSkills().getAverageSkill() : player.getFieldPlayerSkills().getAverageSkill());

        ReservePlayerContainer reservePlayerContainer = reservePlayers[teamIndex][playerIndex];
        reservePlayerContainer.getLblPosition().setText(position.name());
        reservePlayerContainer.getLblSkill().setText("" + skill);
        reservePlayerContainer.getLblName().setText(name);

        menuGroups[teamIndex].setMarkedForSwitch(reservePlayerContainer.getMenuElement(), markedForSwitch);

        reservePlayerElementIndices.put(reservePlayerContainer.getMenuElement(), playerIndex);
    }

    private void swapPlayers(int teamIndex, MenuElement element1, MenuElement element2) {
        Integer fieldPlayerIndex1 = fieldPlayerElementIndices.get(element1);
        Integer fieldPlayerIndex2 = fieldPlayerElementIndices.get(element2);
        Integer reservePlayerIndex1 = reservePlayerElementIndices.get(element1);
        Integer reservePlayerIndex2 = reservePlayerElementIndices.get(element2);
        if ((fieldPlayerIndex1 != null) && (fieldPlayerIndex2 != null)) {
            swapFieldPlayers(teamIndex, fieldPlayerIndex1, fieldPlayerIndex2);
            updateFieldPlayers(teamIndex);
        } else if ((reservePlayerIndex1 != null) && (reservePlayerIndex2 != null)) {
            swapReservePlayers(teamIndex, reservePlayerIndex1, reservePlayerIndex2);
            updateReservePlayers(teamIndex);
        } else {
            int fieldPlayerIndex = ((fieldPlayerIndex1 != null) ? fieldPlayerIndex1 : fieldPlayerIndex2);
            int reservePlayerIndex = ((reservePlayerIndex1 != null) ? reservePlayerIndex1 : reservePlayerIndex2);
            swapFieldAndReservePlayer(teamIndex, fieldPlayerIndex, reservePlayerIndex);
            updateFieldPlayers(teamIndex);
            updateReservePlayers(teamIndex);
        }
    }

    protected abstract void swapFieldPlayers(int teamIndex, int playerIndex1, int playerIndex2);

    protected abstract void swapReservePlayers(int teamIndex, int playerIndex1, int playerIndex2);

    protected abstract void swapFieldAndReservePlayer(int teamIndex, int fieldPlayerIndex, int reservePlayerIndex);

    protected abstract void confirm();

    protected abstract void back();
}
