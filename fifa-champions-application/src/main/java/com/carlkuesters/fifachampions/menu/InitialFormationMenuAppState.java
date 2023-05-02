package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.game.Formation;
import com.carlkuesters.fifachampions.game.InitialTeamInfo;
import com.carlkuesters.fifachampions.game.Player;
import com.carlkuesters.fifachampions.shared.ArrayUtil;

public class InitialFormationMenuAppState extends FormationMenuAppState<Player> {

    @Override
    protected Formation getFormation(int teamIndex) {
        return getInitialTeamInfo(teamIndex).getFormation();
    }

    @Override
    protected void setFormation(int teamIndex, Formation formation) {
        getInitialTeamInfo(teamIndex).setFormation(formation);
    }

    @Override
    protected Player[] getFieldPlayers(int teamIndex) {
        return getInitialTeamInfo(teamIndex).getFieldPlayers();
    }

    @Override
    protected Player[] getReservePlayers(int teamIndex) {
        return getInitialTeamInfo(teamIndex).getReservePlayers();
    }

    @Override
    protected Player getPlayer(Player player) {
        return player;
    }

    @Override
    protected boolean isMarkedForSwitch(Player playerObject) {
        return false;
    }

    @Override
    protected void swapFieldPlayers(int teamIndex, int playerIndex1, int playerIndex2) {
        Player[] fieldPlayers = getFieldPlayers(teamIndex);
        ArrayUtil.swap(fieldPlayers, playerIndex1, playerIndex2);
    }

    @Override
    protected void swapReservePlayers(int teamIndex, int playerIndex1, int playerIndex2) {
        Player[] reservePlayers = getReservePlayers(teamIndex);
        ArrayUtil.swap(reservePlayers, playerIndex1, playerIndex2);
    }

    @Override
    protected void swapFieldAndReservePlayer(int teamIndex, int fieldPlayerIndex, int reservePlayerIndex) {
        Player[] fieldPlayers = getFieldPlayers(teamIndex);
        Player[] reservePlayers = getReservePlayers(teamIndex);
        ArrayUtil.swap(fieldPlayers, fieldPlayerIndex, reservePlayers, reservePlayerIndex);
    }

    private InitialTeamInfo getInitialTeamInfo(int teamIndex) {
        return mainApplication.getGameCreationInfo().getTeams()[teamIndex];
    }

    @Override
    protected void confirm() {
        openMenu(GameSettingsMenuAppState.class);
    }
}
