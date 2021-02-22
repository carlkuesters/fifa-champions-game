package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.game.InitialTeamInfo;

public class TrikotMenuGroup extends GameCreationCarouselMenuGroup {

    public TrikotMenuGroup(InitialTeamInfo initialTeamInfo, Runnable updateTeam, Runnable confirm) {
        super(initialTeamInfo, updateTeam, confirm);
    }

    @Override
    public int getCarouselValue() {
        return initialTeamInfo.getTrikotIndex();
    }

    @Override
    public void setCarouselValue(int value) {
        initialTeamInfo.setTrikotIndex(value);
        super.setCarouselValue(value);
    }

    @Override
    public int getCarouselMaximumValue() {
        return (initialTeamInfo.getTeamInfo().getTrikotNames().length - 1);
    }
}
