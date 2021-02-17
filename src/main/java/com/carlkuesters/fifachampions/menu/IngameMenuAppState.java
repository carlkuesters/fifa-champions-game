package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameAppState;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.SpringGridLayout;

public class IngameMenuAppState extends MenuAppState {

    protected int marginX = 100;
    protected int containerHeight = 600;
    protected int marginBetween = 20;
    protected int yStart;
    private ElementsMenuGroup menuGroup;
    private Label lblTime = new Label("");
    private Label lblScore = new Label("");

    @Override
    protected void initMenu() {
        yStart = ((totalHeight / 2) + (containerHeight / 2));

        addGameInfo();

        menuGroup = new ElementsMenuGroup(this::close);
        addMenuGroup(menuGroup);
    }

    protected void addGameInfo() {
        int containerWidth = ((totalWidth / 2) - (marginBetween / 2) - marginX);
        int containerX = ((totalWidth / 2) - (marginBetween / 2) - containerWidth);

        Container container = new Container();
        container.setPreferredSize(new Vector3f(containerWidth, containerHeight, 0));
        container.setLocalTranslation(new Vector3f(containerX, yStart, 0));

        Container rowTeams = new Container();
        rowTeams.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        rowTeams.setBackground(null);

        int logoSize = 150;
        Panel teamLogoLeft = new Panel();
        IconComponent logoIconComponentLeft = new IconComponent("textures/logo.png");
        logoIconComponentLeft.setHAlignment(HAlignment.Center);
        logoIconComponentLeft.setVAlignment(VAlignment.Center);
        logoIconComponentLeft.setIconSize(new Vector2f(logoSize, logoSize));
        teamLogoLeft.setBackground(logoIconComponentLeft);
        rowTeams.addChild(teamLogoLeft);

        Container containerScoreMiddle = new Container();
        containerScoreMiddle.setBackground(null);
        lblTime.setFontSize(20);
        lblTime.setTextHAlignment(HAlignment.Center);
        lblTime.setTextVAlignment(VAlignment.Bottom);
        containerScoreMiddle.addChild(lblTime);
        lblScore.setFontSize(40);
        lblScore.setTextHAlignment(HAlignment.Center);
        lblScore.setTextVAlignment(VAlignment.Top);
        containerScoreMiddle.addChild(lblScore);
        rowTeams.addChild(containerScoreMiddle);

        Panel teamLogoRight = new Panel();
        IconComponent logoIconComponentRight = new IconComponent("textures/logo.png");
        logoIconComponentRight.setHAlignment(HAlignment.Center);
        logoIconComponentRight.setVAlignment(VAlignment.Center);
        logoIconComponentRight.setIconSize(new Vector2f(logoSize, logoSize));
        teamLogoRight.setBackground(logoIconComponentRight);
        rowTeams.addChild(teamLogoRight);

        container.addChild(rowTeams);

        for (int i = 0; i < 8; i++) {
            Container row = new Container();
            row.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
            row.setBackground(null);

            Label lblLeft = new Label("XX");
            lblLeft.setFontSize(20);
            lblLeft.setTextHAlignment(HAlignment.Center);
            lblLeft.setTextVAlignment(VAlignment.Center);
            row.addChild(lblLeft);

            Label lblMiddle = new Label("Category");
            lblMiddle.setFontSize(20);
            lblMiddle.setTextHAlignment(HAlignment.Center);
            lblMiddle.setTextVAlignment(VAlignment.Center);
            row.addChild(lblMiddle);

            Label lblRight = new Label("XX");
            lblRight.setFontSize(20);
            lblRight.setTextHAlignment(HAlignment.Center);
            lblRight.setTextVAlignment(VAlignment.Center);
            row.addChild(lblRight);

            container.addChild(row);
        }

        guiNode.attachChild(container);
    }

    protected void addButton(Vector3f position, int buttonWidth, int buttonHeight, String text, Runnable action) {
        Button button = new Button(text);
        button.setLocalTranslation(position);
        button.setPreferredSize(new Vector3f(buttonWidth, buttonHeight, 0));
        button.setTextHAlignment(HAlignment.Center);
        button.setTextVAlignment(VAlignment.Center);
        button.setFontSize(20);
        guiNode.attachChild(button);
        menuGroup.addElement(new MenuElement(button, action));
    }

    public void setScore(int goalsTeam1, int goalsTeam2) {
        lblScore.setText(goalsTeam1 + " - " + goalsTeam2);
    }

    public void setTime(String time) {
        lblTime.setText(time);
    }

    protected void endGame() {
        mainApplication.getStateManager().detach(mainApplication.getStateManager().getState(GameAppState.class));
        openMenu(MainMenuAppState.class);
    }
}
