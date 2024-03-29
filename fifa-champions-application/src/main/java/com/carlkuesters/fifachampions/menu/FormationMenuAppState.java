package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.game.Formation;
import com.carlkuesters.fifachampions.game.Player;
import com.carlkuesters.fifachampions.game.PlayerPosition;
import com.carlkuesters.fifachampions.visuals.PlayerSkin;
import com.carlkuesters.fifachampions.visuals.PlayerSkins;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.texture.Texture;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.SpringGridLayout;

import java.util.HashMap;

public abstract class FormationMenuAppState<P> extends MenuAppState {

    private int containerMarginOutsideX = 50;
    private int containerMarginOutsideY = 50;
    private int containerMarginBetween = 200;
    private int playerDetailsImageSize = 60;
    private int playerImageSize = 40;
    private int fieldPlayerLeftAndRightColumnWidth = 25;
    private int reservePlayersX = 7;
    private int reservePlayersY = 4;
    private int containerY;
    private int containerWidth;
    private int backgroundSize;
    private FormationMenuGroup[] menuGroups = new FormationMenuGroup[2];
    private PlayerDetailsContainer[][] playerDetails = new PlayerDetailsContainer[2][2];
    private FieldPlayerContainer[][] fieldPlayers = new FieldPlayerContainer[2][11];
    private HashMap<MenuElement, Integer> fieldPlayerElementIndices = new HashMap<>();
    private ReservePlayerContainer[][] reservePlayers = new ReservePlayerContainer[2][28];
    private HashMap<MenuElement, Integer> reservePlayerElementIndices = new HashMap<>();

    @Override
    protected void initMenu() {
        addTitle("Aufstellung");
        containerY = (totalHeight - containerMarginOutsideY);
        containerWidth = ((totalWidth - (2 * containerMarginOutsideX) - containerMarginBetween) / 2);
        backgroundSize = (totalHeight - (2 * containerMarginOutsideY) - playerDetailsImageSize - (reservePlayersY * (playerImageSize + 20)));
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
            selectedElement -> updatePlayerDetails(teamIndex, selectedElement, 1),
            (element1, element2) -> swapPlayers(teamIndex, element1, element2)
        );
        menuGroups[teamIndex] = menuGroup;

        int containerX = getContainerX(side);
        Container container = new Container();
        container.setLocalTranslation(containerX, containerY, 0);

        Container playerDetailsRow = new Container();
        playerDetailsRow.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        playerDetailsRow.setBackground(null);

        PlayerDetailsContainer playerDetailsLeft = createPlayerDetails();
        playerDetailsRow.addChild(playerDetailsLeft.getContainer());
        playerDetails[teamIndex][0] = playerDetailsLeft;
        updatePlayerDetails(teamIndex, 0, null, false);

        PlayerDetailsContainer playerDetailsRight = createPlayerDetails();
        playerDetailsRow.addChild(playerDetailsRight.getContainer());
        playerDetails[teamIndex][1] = playerDetailsRight;
        updatePlayerDetails(teamIndex, 1, null, false);

        container.addChild(playerDetailsRow);

        Panel formationBackground = new Panel();
        IconComponent formationBackgroundIcon = new IconComponent("textures/formation_background.png");
        formationBackgroundIcon.setIconSize(new Vector2f(backgroundSize, backgroundSize));
        formationBackgroundIcon.setHAlignment(HAlignment.Center);
        formationBackground.setBackground(formationBackgroundIcon);
        container.addChild(formationBackground);

        Container reservePlayersContainer = new Container();
        reservePlayersContainer.setBackground(null);
        int reservePlayerIndex = 0;
        for (int y = 0; y < reservePlayersY; y++) {
            Container reservePlayersRow = new Container();
            reservePlayersRow.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
            reservePlayersRow.setBackground(null);
            for (int x = 0; x < reservePlayersX; x++) {
                ReservePlayerContainer reservePlayerContainer = createReservePlayer();
                reservePlayersRow.addChild(reservePlayerContainer.getContainer());
                reservePlayers[teamIndex][reservePlayerIndex] = reservePlayerContainer;
                reservePlayerElementIndices.put(reservePlayerContainer.getMenuElement(), reservePlayerIndex);
                menuGroup.addElement(reservePlayerContainer.getMenuElement());
                reservePlayerIndex++;
                if (reservePlayerIndex == 28) {
                    break;
                }
            }
            reservePlayersContainer.addChild(reservePlayersRow);
        }
        container.addChild(reservePlayersContainer);

        guiNode.attachChild(container);

        for (int i = 0; i < fieldPlayers[teamIndex].length; i++) {
            FieldPlayerContainer fieldPlayerContainer = createFieldPlayer();
            guiNode.attachChild(fieldPlayerContainer.getContainer());
            fieldPlayers[teamIndex][i] = fieldPlayerContainer;
            fieldPlayerElementIndices.put(fieldPlayerContainer.getMenuElement(), i);
            menuGroup.addElement(fieldPlayerContainer.getMenuElement());
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

    private PlayerDetailsContainer createPlayerDetails() {
        Container container = new Container();
        container.setLayout(new SpringGridLayout(Axis.X, Axis.Y));

        Panel playerImage = new Panel();
        IconComponent playerIcon = new IconComponent("textures/players/unknown_80.png");
        playerIcon.setIconSize(new Vector2f(playerDetailsImageSize, playerDetailsImageSize));
        playerIcon.setHAlignment(HAlignment.Center);
        playerIcon.setVAlignment(VAlignment.Center);
        playerImage.setBackground(playerIcon);
        container.addChild(playerImage);

        Container skillsColumn = new Container();
        skillsColumn.setBackground(null);
        Label[][] lblSkills = new Label[3][3];
        for (int y = 0; y < lblSkills.length; y++) {
            Container skillsRow = new Container();
            skillsRow.setBackground(null);
            skillsRow.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
            for (int x = 0; x < lblSkills[y].length; x++) {
                // Use this placeholder text, so the hidden right side has width and the layout isn't messed up
                Label lblSkill = new Label("XXX: XX");
                lblSkill.setTextHAlignment(HAlignment.Center);
                lblSkill.setTextVAlignment(VAlignment.Center);
                lblSkill.setFontSize(12);
                skillsRow.addChild(lblSkill);
                lblSkills[y][x] = lblSkill;
            }
            skillsColumn.addChild(skillsRow);
        }
        container.addChild(skillsColumn);

        return new PlayerDetailsContainer(container, playerIcon, lblSkills);
    }

    private ReservePlayerContainer createReservePlayer() {
        Container container = new Container();

        Container topRow = new Container();
        topRow.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        // We need to subtract 10, because otherwise for some reason the reserve players row is 10px wider than the container width
        topRow.setPreferredSize(new Vector3f(((containerWidth - 10) / ((float) reservePlayersX)), playerImageSize, 0));
        topRow.setBackground(null);

        Label lblPosition = new Label("");
        lblPosition.setTextHAlignment(HAlignment.Center);
        lblPosition.setTextVAlignment(VAlignment.Center);
        lblPosition.setFontSize(12);
        topRow.addChild(lblPosition);

        Panel playerImage = new Panel();
        IconComponent playerIcon = new IconComponent("textures/players/unknown_80.png");
        playerIcon.setIconSize(new Vector2f(playerImageSize, playerImageSize));
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

        return new ReservePlayerContainer(container, lblPosition, lblSkill, lblName, playerIcon, menuElement);
    }

    private FieldPlayerContainer createFieldPlayer() {
        Container container = new Container();
        container.setBackground(null);

        Container topRow = new Container();
        topRow.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        topRow.setBackground(null);

        Label lblSkill = new Label("");
        lblSkill.setPreferredSize(new Vector3f(fieldPlayerLeftAndRightColumnWidth, 0, 0));
        lblSkill.setTextHAlignment(HAlignment.Center);
        lblSkill.setTextVAlignment(VAlignment.Bottom);
        lblSkill.setFontSize(14);
        lblSkill.setColor(ColorRGBA.White);
        topRow.addChild(lblSkill);

        Panel playerImage = new Panel();
        IconComponent playerIcon = new IconComponent("textures/players/unknown_80.png");
        playerIcon.setIconSize(new Vector2f(playerImageSize, playerImageSize));
        playerIcon.setHAlignment(HAlignment.Center);
        playerImage.setBackground(playerIcon);
        topRow.addChild(playerImage);

        Panel placeholderRight = new Panel();
        placeholderRight.setPreferredSize(new Vector3f(fieldPlayerLeftAndRightColumnWidth, fieldPlayerLeftAndRightColumnWidth, 0));
        placeholderRight.setBackground(null);
        topRow.addChild(placeholderRight);

        container.addChild(topRow);

        Label lblName = new Label("");
        lblName.setTextHAlignment(HAlignment.Center);
        lblName.setTextVAlignment(VAlignment.Center);
        lblName.setFontSize(14);
        lblName.setColor(ColorRGBA.White);
        container.addChild(lblName);

        MenuElement menuElement = new MenuElement(container, this::confirm);

        return new FieldPlayerContainer(container, lblSkill, lblName, playerIcon, menuElement);
    }

    protected abstract Formation getFormation(int teamIndex);

    protected abstract void setFormation(int teamIndex, Formation formation);

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            for (int teamIndex = 0; teamIndex < 2; teamIndex++) {
                int _teamIndex = teamIndex;
                menuGroups[teamIndex].setOnElementHovered(element -> updatePlayerDetails(_teamIndex, element, 0));
                updateFieldPlayers(teamIndex);
                updateReservePlayers(teamIndex);
            }
        }
    }

    private void updatePlayerDetails(int teamIndex, MenuElement menuElement, int detailsIndex) {
        Player player = null;
        boolean useGoalkeeperStats = false;
        if (menuElement != null) {
            Integer fieldPlayerIndex = fieldPlayerElementIndices.get(menuElement);
            if (fieldPlayerIndex != null) {
                player = getPlayer(getFieldPlayers(teamIndex)[fieldPlayerIndex]);
                useGoalkeeperStats = (fieldPlayerIndex == 0);
            } else {
                int reservePlayerIndex = reservePlayerElementIndices.get(menuElement);
                player = getPlayer(getReservePlayers(teamIndex)[reservePlayerIndex]);
                useGoalkeeperStats = (player.getPosition() == PlayerPosition.TW);
            }
        }
        updatePlayerDetails(teamIndex, detailsIndex, player, useGoalkeeperStats);
    }

    private void updatePlayerDetails(int teamIndex, int detailsIndex, Player player, boolean useGoalkeeperStats) {
        PlayerDetailsContainer playerDetailsContainer = playerDetails[teamIndex][detailsIndex];
        Label[][] lblSkills = playerDetailsContainer.getLblSkills();
        if (player != null) {
            playerDetailsContainer.getPlayerIcon().setImageTexture(getPlayerIconTexture(player));
            if (useGoalkeeperStats) {
                lblSkills[0][0].setText("BEW: " + player.getGoalkeeperSkills().getAgility());
                lblSkills[0][1].setText("SPR: " + player.getGoalkeeperSkills().getJumpStrength());
                lblSkills[0][2].setText("REF: " + player.getGoalkeeperSkills().getReflexes());
                lblSkills[1][0].setText("FST: " + player.getGoalkeeperSkills().getBallCling());
                lblSkills[1][1].setText("");
                lblSkills[1][2].setText("");
                lblSkills[2][0].setText("");
                lblSkills[2][1].setText("");
                lblSkills[2][2].setText("");
            } else {
                lblSkills[0][0].setText("BES: " + player.getFieldPlayerSkills().getAcceleration());
                lblSkills[0][1].setText("GES: " + player.getFieldPlayerSkills().getMaximumSpeed());
                lblSkills[0][2].setText("KND: " + player.getFieldPlayerSkills().getStamina());
                lblSkills[1][0].setText("BKT: " + player.getFieldPlayerSkills().getBallControl());
                lblSkills[1][1].setText("SST; " + player.getFieldPlayerSkills().getShootingStrength());
                lblSkills[1][2].setText("SGN: " + player.getFieldPlayerSkills().getShootingAccuracy());
                lblSkills[2][0].setText("ZWK: " + player.getFieldPlayerSkills().getFootDuel());
                lblSkills[2][1].setText("KOP: " + player.getFieldPlayerSkills().getHeaderDuel());
                lblSkills[2][2].setText("TRK: " + player.getFieldPlayerSkills().getTricks());
            }
            playerDetailsContainer.getContainer().setAlpha(1);
        } else {
            playerDetailsContainer.getContainer().setAlpha(0);
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
        int skill = ((playerIndex == 0) ? player.getGoalkeeperSkills().getAverageSkill() : player.getFieldPlayerSkills().getAverageSkill());

        FieldPlayerContainer fieldPlayerContainer = fieldPlayers[teamIndex][playerIndex];
        fieldPlayerContainer.getLblName().setText(player.getName());
        fieldPlayerContainer.getLblSkill().setText("" + skill);
        fieldPlayerContainer.getPlayerIcon().setImageTexture(getPlayerIconTexture(player));
        float x = getFormationPlayerX(teamIndex, formationLocation.getY());
        float y = getFormationPlayerY(formationLocation.getX());
        fieldPlayerContainer.getContainer().setLocalTranslation(x, y, 1);

        menuGroups[teamIndex].setMarkedForSwitch(fieldPlayerContainer.getMenuElement(), markedForSwitch);
    }

    private float getFormationPlayerX(int teamIndex, float formationX) {
        int side = ((teamIndex == 0) ? -1 : 1);
        int startX = getContainerX(side) + ((containerWidth - backgroundSize) / 2);
        int maximumDistanceX = (backgroundSize - ((2 * fieldPlayerLeftAndRightColumnWidth) + playerImageSize));
        float progressX = ((formationX + 1) / 2);
        return (startX + (progressX * maximumDistanceX));
    }

    private float getFormationPlayerY(float formationY) {
        // - 11 because of visual aesthetics
        int startY = (containerY - (playerDetailsImageSize - 11));
        // + 20 because of the player name below the image
        // - 6 because of visual aesthetics
        int maximumDistanceY = (backgroundSize - (playerImageSize + 20 - 6));
        float progressY = (1 - ((formationY + 1) / 2));
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
        PlayerPosition position = player.getPosition();
        int skill = ((position == PlayerPosition.TW) ? player.getGoalkeeperSkills().getAverageSkill() : player.getFieldPlayerSkills().getAverageSkill());

        ReservePlayerContainer reservePlayerContainer = reservePlayers[teamIndex][playerIndex];
        reservePlayerContainer.getLblPosition().setText(position.name());
        reservePlayerContainer.getLblSkill().setText("" + skill);
        reservePlayerContainer.getLblName().setText(player.getName());
        reservePlayerContainer.getPlayerIcon().setImageTexture(getPlayerIconTexture(player));

        menuGroups[teamIndex].setMarkedForSwitch(reservePlayerContainer.getMenuElement(), markedForSwitch);
    }

    private Texture getPlayerIconTexture(Player player) {
        PlayerSkin playerSKin = PlayerSkins.get(player);
        String playerIconPath = "textures/players/" + (playerSKin.isCustomPortrait() ? playerSKin.getFaceName() : "unknown") + "_80.png";
        return GuiGlobals.getInstance().loadTexture(playerIconPath, false, false);
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
}
