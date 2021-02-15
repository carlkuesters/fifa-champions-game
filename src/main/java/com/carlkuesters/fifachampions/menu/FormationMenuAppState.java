package com.carlkuesters.fifachampions.menu;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.SpringGridLayout;

public class FormationMenuAppState extends MenuAppState {

    private int containerMarginOutside = 150;
    private int containerMarginBetween = 400;
    private int containerY;
    private int containerWidth;
    private int playerImageBigHeight = 60;
    private int formationLeftAndRightColumnWidth = 25;
    private int formationTrikotImageSize = 30;
    private Container[][] formationPlayers = new Container[2][11];

    @Override
    protected void initMenu() {
        addTitle("Aufstellung");
        containerY = (totalHeight - 100);
        containerWidth = ((totalWidth - (2 * containerMarginOutside) - containerMarginBetween) / 2);
        addSide(-1);
        addSide(1);
    }

    private void addSide(int side) {
        MenuGroup menuGroup = new MenuGroup(() -> openMenu(PauseIngameMenuAppState.class));

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
        IconComponent formationBackgroundIcon = new IconComponent("textures/logo.png");
        formationBackgroundIcon.setIconSize(new Vector2f(containerWidth, containerWidth));
        formationBackground.setBackground(formationBackgroundIcon);
        container.addChild(formationBackground);

        Container reservePlayers = new Container();
        reservePlayers.setBackground(null);
        for (int y = 0; y < 4; y++) {
            Container reservePlayersRow = new Container();
            reservePlayersRow.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
            reservePlayersRow.setBackground(null);
            for (int x = 0; x < 5; x++) {
                Container containerReservePlayerDetails = createPlayerDetailsSmall(menuGroup);
                reservePlayersRow.addChild(containerReservePlayerDetails);
            }
            reservePlayers.addChild(reservePlayersRow);
        }
        container.addChild(reservePlayers);

        guiNode.attachChild(container);

        int teamIndex = ((side + 1) / 2);
        for (int i = 0; i < formationPlayers[teamIndex].length; i++) {
            Container formationPlayer = createFormationPlayer(menuGroup);
            guiNode.attachChild(formationPlayer);
            formationPlayers[teamIndex][i] = formationPlayer;
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
        IconComponent playerIcon = new IconComponent("textures/logo.png");
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

    private Container createPlayerDetailsSmall(MenuGroup menuGroup) {
        Container container = new Container();
        container.setLayout(new SpringGridLayout(Axis.X, Axis.Y));

        Label lblPosition = new Label("XX");
        lblPosition.setTextHAlignment(HAlignment.Center);
        lblPosition.setTextVAlignment(VAlignment.Center);
        lblPosition.setFontSize(12);
        container.addChild(lblPosition);

        int playerImageHeight = 40;
        Panel playerImage = new Panel();
        IconComponent playerIcon = new IconComponent("textures/logo.png");
        playerIcon.setIconSize(new Vector2f(playerImageHeight, playerImageHeight));
        playerIcon.setHAlignment(HAlignment.Center);
        playerImage.setBackground(playerIcon);
        container.addChild(playerImage);

        Label lblSkil = new Label("99");
        lblSkil.setTextHAlignment(HAlignment.Center);
        lblSkil.setTextVAlignment(VAlignment.Center);
        lblSkil.setFontSize(12);
        container.addChild(lblSkil);

        menuGroup.addElement(new MenuElement(container, () -> openMenu(PauseIngameMenuAppState.class)));

        return container;
    }

    private Container createFormationPlayer(MenuGroup menuGroup) {
        Container container = new Container();
        container.setBackground(null);

        Container topRow = new Container();
        topRow.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        topRow.setBackground(null);

        Label lblSkill = new Label("99");
        lblSkill.setPreferredSize(new Vector3f(formationLeftAndRightColumnWidth, 0, 0));
        lblSkill.setTextHAlignment(HAlignment.Center);
        lblSkill.setTextVAlignment(VAlignment.Bottom);
        lblSkill.setFontSize(12);
        topRow.addChild(lblSkill);

        Panel trikotImage = new Panel();
        IconComponent trikotIcon = new IconComponent("textures/logo.png");
        trikotIcon.setIconSize(new Vector2f(formationTrikotImageSize, formationTrikotImageSize));
        trikotIcon.setHAlignment(HAlignment.Center);
        trikotImage.setBackground(trikotIcon);
        topRow.addChild(trikotImage);

        Panel placeholderRight = new Panel();
        placeholderRight.setPreferredSize(new Vector3f(formationLeftAndRightColumnWidth, formationLeftAndRightColumnWidth, 0));
        placeholderRight.setBackground(null);
        topRow.addChild(placeholderRight);

        container.addChild(topRow);

        Label lblName = new Label("Playername");
        lblName.setTextHAlignment(HAlignment.Center);
        lblName.setTextVAlignment(VAlignment.Center);
        lblName.setFontSize(12);
        container.addChild(lblName);

        menuGroup.addElement(new MenuElement(container, () -> openMenu(PauseIngameMenuAppState.class)));

        return container;
    }

    public void setFormationPlayerPosition(int side, int playerIndex, Vector2f formationLocation) {
        float x = getFormationPlayerX(side, formationLocation.getY());
        float y = getFormationPlayerY(formationLocation.getX());
        int teamIndex = ((side + 1) / 2);
        formationPlayers[teamIndex][playerIndex].setLocalTranslation(x, y, 1);
    }

    private float getFormationPlayerX(int side, float formationY) {
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
}
