package com.carlkuesters.fifachampions.visuals;

import com.carlkuesters.fifachampions.game.TeamInfo;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import lombok.Getter;

public class ScoreContainer {

    public ScoreContainer() {
        node = new Node();

        int padding = 5;
        int fontSize = 16;

        Container container = new Container();
        container.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        container.setBackground(null);

        Container containerTime = new Container();
        TbtQuadBackgroundComponent containerTimeBackground = (TbtQuadBackgroundComponent) containerTime.getBackground();
        containerTimeBackground.setColor(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.5f));
        lblTime = new Label("");
        lblTime.setInsets(new Insets3f(padding, 2 * padding, padding, 2 * padding));
        lblTime.setFontSize(fontSize);
        lblTime.setTextVAlignment(VAlignment.Center);
        containerTime.addChild(lblTime);
        container.addChild(containerTime);

        Container containerTeam1 = new Container();
        containerTeam1.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        panTeamIcon1 = new Panel();
        panTeamIcon1.setInsets(new Insets3f(padding, padding, padding, padding));
        containerTeam1.addChild(panTeamIcon1);
        lblTeamAbbreviation1 = new Label("");
        lblTeamAbbreviation1.setInsets(new Insets3f(padding, 0, padding, 2 * padding));
        lblTeamAbbreviation1.setFontSize(fontSize);
        lblTeamAbbreviation1.setTextVAlignment(VAlignment.Center);
        containerTeam1.addChild(lblTeamAbbreviation1);
        container.addChild(containerTeam1);

        Container containerGoals = new Container();
        lblGoals = new Label("");
        lblGoals.setInsets(new Insets3f(padding, 2 * padding, padding, 2 * padding));
        lblGoals.setFontSize(fontSize);
        lblGoals.setTextVAlignment(VAlignment.Center);
        containerGoals.addChild(lblGoals);
        container.addChild(containerGoals);

        Container containerTeam2 = new Container();
        containerTeam2.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        lblTeamAbbreviation2 = new Label("");
        lblTeamAbbreviation2.setInsets(new Insets3f(padding, 2 * padding, padding, 0));
        lblTeamAbbreviation2.setFontSize(fontSize);
        lblTeamAbbreviation2.setTextVAlignment(VAlignment.Center);
        containerTeam2.addChild(lblTeamAbbreviation2);
        panTeamIcon2 = new Panel();
        panTeamIcon2.setInsets(new Insets3f(padding, padding, padding, padding));
        containerTeam2.addChild(panTeamIcon2);
        container.addChild(containerTeam2);

        node.attachChild(container);

        containerOverTime = new Container();
        TbtQuadBackgroundComponent containerOverTimeBackground = (TbtQuadBackgroundComponent) containerOverTime.getBackground();
        containerOverTimeBackground.setColor(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.5f));
        lblOverTime = new Label("");
        lblOverTime.setInsets(new Insets3f(padding, 2 * padding, padding, 2 * padding));
        lblOverTime.setFontSize(fontSize);
        lblOverTime.setTextVAlignment(VAlignment.Center);
        containerOverTime.addChild(lblOverTime);
        containerOverTime.setLocalTranslation(0, -36, 0);
    }
    @Getter
    private Node node;
    private Label lblTime;
    private Panel panTeamIcon1;
    private Label lblTeamAbbreviation1;
    private Label lblTeamAbbreviation2;
    private Panel panTeamIcon2;
    private Label lblGoals;
    private Container containerOverTime;
    private Label lblOverTime;

    public void setTeams(TeamInfo teamInfo1, TeamInfo teamInfo2) {
        setTeamIcon(panTeamIcon1, teamInfo1);
        lblTeamAbbreviation1.setText(teamInfo1.getAbbreviation());
        lblTeamAbbreviation2.setText(teamInfo2.getAbbreviation());
        setTeamIcon(panTeamIcon2, teamInfo2);
    }

    private void setTeamIcon(Panel panTeamIcon, TeamInfo teamInfo) {
        IconComponent teamIcon = new IconComponent("textures/teams/" + teamInfo.getName() + ".png");
        teamIcon.setIconSize(new Vector2f(24, 24));
        teamIcon.setHAlignment(HAlignment.Center);
        teamIcon.setVAlignment(VAlignment.Center);
        panTeamIcon.setBackground(teamIcon);
    }

    public void setTimeAndGoals(String time, String overTime, int[] goals) {
        lblTime.setText(time);
        if (overTime != null) {
            lblOverTime.setText(overTime);
            node.attachChild(containerOverTime);
        } else {
            node.detachChild(containerOverTime);
        }
        lblGoals.setText(goals[0] + " - " + goals[1]);
    }
}
