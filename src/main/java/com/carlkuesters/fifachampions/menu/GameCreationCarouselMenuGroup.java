package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.game.InitialTeamInfo;

public abstract class GameCreationCarouselMenuGroup extends CarouselMenuGroup {

    public GameCreationCarouselMenuGroup(InitialTeamInfo initialTeamInfo, Runnable updateTeam, Runnable confirm) {
        this.initialTeamInfo = initialTeamInfo;
        this.updateTeam = updateTeam;
        this.confirm = confirm;
    }
    protected InitialTeamInfo initialTeamInfo;
    private Runnable updateTeam;
    private Runnable confirm;

    @Override
    public void setCarouselValue(int value) {
        updateTeam.run();
    }

    @Override
    public void confirm() {
        confirm.run();
    }
}
