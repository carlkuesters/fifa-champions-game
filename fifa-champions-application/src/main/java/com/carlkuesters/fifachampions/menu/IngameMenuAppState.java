package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameAppState;
import com.carlkuesters.fifachampions.IngameAppState;
import com.carlkuesters.fifachampions.ReplayAppState;
import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.game.Team;
import com.carlkuesters.fifachampions.game.TeamStatistics;
import com.carlkuesters.fifachampions.visuals.TimeFormatter;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.SpringGridLayout;

public class IngameMenuAppState extends MenuAppState {

    public IngameMenuAppState() {
        mode = MenuMode.ROOT;
    }
    protected int marginX = 100;
    protected int containerHeight = 600;
    protected int marginBetween = 20;
    protected int yStart;
    private int teamLogoSize = 150;
    private ElementsMenuGroup menuGroup;
    private Panel[] panTeamLogo = new Panel[2];
    private Label lblTime = new Label("");
    private Label lblScore = new Label("");
    private Label[][] lblStatistics;

    @Override
    protected void initMenu() {
        yStart = ((totalHeight / 2) + (containerHeight / 2));

        addGameInfo();

        menuGroup = new ElementsMenuGroup();
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

        Panel teamLogoLeft = new Panel();
        rowTeams.addChild(teamLogoLeft);
        panTeamLogo[0] = teamLogoLeft;

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
        rowTeams.addChild(teamLogoRight);
        panTeamLogo[1] = teamLogoRight;

        container.addChild(rowTeams);

        lblStatistics = new Label[11][2];
        String[] lblStatisticsTitles = new String[] {
            "Schüsse",
            "Schüsse aufs Tor",
            "Ballbesitz",
            "Zweikämpfe",
            "Fouls",
            "Gelbe Karten",
            "Rote Karten",
            "Abseits",
            "Ecken",
            "Schussgenauigkeit",
            "Passgenauigkeit",
        };
        for (int i = 0; i < lblStatistics.length; i++) {
            Container row = new Container();
            row.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
            row.setBackground(null);
            if (i == (lblStatistics.length - 1)) {
                row.setInsets(new Insets3f(0, 0, 20, 0));
            }

            Label lblLeft = new Label("");
            lblLeft.setFontSize(20);
            lblLeft.setTextHAlignment(HAlignment.Center);
            lblLeft.setTextVAlignment(VAlignment.Center);
            lblLeft.setPreferredSize(new Vector3f());
            row.addChild(lblLeft);

            Label lblMiddle = new Label(lblStatisticsTitles[i]);
            lblMiddle.setFontSize(20);
            lblMiddle.setTextHAlignment(HAlignment.Center);
            lblMiddle.setTextVAlignment(VAlignment.Center);
            lblMiddle.setPreferredSize(new Vector3f());
            row.addChild(lblMiddle);

            Label lblRight = new Label("");
            lblRight.setFontSize(20);
            lblRight.setTextHAlignment(HAlignment.Center);
            lblRight.setTextVAlignment(VAlignment.Center);
            lblRight.setPreferredSize(new Vector3f());
            row.addChild(lblRight);

            container.addChild(row);

            lblStatistics[i][0] = lblLeft;
            lblStatistics[i][1] = lblRight;
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

    @Override
    public void update(float tpf) {
        super.update(tpf);
        GameAppState gameAppState = getAppState(GameAppState.class);
        Game game = gameAppState.getGame();

        TimeFormatter timeFormatter = gameAppState.getTimeFormatter();
        lblTime.setText(timeFormatter.getCombinedTime());
        lblScore.setText(game.getGoals()[0] + " - " + game.getGoals()[1]);

        for (int teamIndex = 0; teamIndex < game.getTeams().length; teamIndex++) {
            Team team = game.getTeams()[teamIndex];

            // TODO: Create IconComponent only once and update it
            IconComponent logoIcon = new IconComponent("textures/teams/" + team.getTeamInfo().getName() + ".png");
            logoIcon.setHAlignment(HAlignment.Center);
            logoIcon.setVAlignment(VAlignment.Center);
            logoIcon.setIconSize(new Vector2f(teamLogoSize, teamLogoSize));
            panTeamLogo[teamIndex].setBackground(logoIcon);

            TeamStatistics statistics = team.getStatistics();
            float ballPossession = (statistics.getTimeWithBall() / game.getPassedTime());
            lblStatistics[0][teamIndex].setText("" + statistics.getShotsTotal());
            lblStatistics[1][teamIndex].setText("" + statistics.getShotsOnGOal());
            lblStatistics[2][teamIndex].setText(getRoundedPercentage(ballPossession) + "%");
            lblStatistics[3][teamIndex].setText("" + statistics.getFightsWon());
            lblStatistics[4][teamIndex].setText("" + statistics.getFouls());
            lblStatistics[5][teamIndex].setText("" + 0); // TODO: Yellow cards
            lblStatistics[6][teamIndex].setText("" + 0); // TODO: Red cards
            lblStatistics[7][teamIndex].setText("" + statistics.getOffsides());
            lblStatistics[8][teamIndex].setText("" + statistics.getCorners());
            lblStatistics[9][teamIndex].setText(getRoundedPercentage(statistics.getShotAccuracy())+ "%");
            lblStatistics[10][teamIndex].setText(getRoundedPercentage(statistics.getPassAccuracy())+ "%");
        }
    }

    private int getRoundedPercentage(float ratio) {
        return Math.round(ratio * 100);
    }

    protected void endGame() {
        mainApplication.getStateManager().detach(mainApplication.getStateManager().getState(IngameAppState.class));
        mainApplication.getStateManager().detach(mainApplication.getStateManager().getState(ReplayAppState.class));
        mainApplication.getStateManager().detach(mainApplication.getStateManager().getState(GameAppState.class));
        openMenu(MainMenuAppState.class);
    }
}
