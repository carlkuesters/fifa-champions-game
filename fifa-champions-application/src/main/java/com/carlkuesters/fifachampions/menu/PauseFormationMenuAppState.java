package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameAppState;
import com.carlkuesters.fifachampions.game.*;
import com.carlkuesters.fifachampions.shared.ArrayUtil;

public class PauseFormationMenuAppState extends FormationMenuAppState<PlayerObject> {

    @Override
    protected Formation getFormation(int teamIndex) {
        return getTeam(teamIndex).getFormation();
    }

    @Override
    protected void setFormation(int teamIndex, Formation formation) {
        getTeam(teamIndex).setFormation(formation);
    }

    @Override
    protected PlayerObject[] getFieldPlayers(int teamIndex) {
        return getTeam(teamIndex).getPlayers();
    }

    @Override
    protected PlayerObject[] getReservePlayers(int teamIndex) {
        return getTeam(teamIndex).getReservePlayers();
    }

    @Override
    protected Player getPlayer(PlayerObject playerObject) {
        return playerObject.getPlayer();
    }

    @Override
    protected boolean isMarkedForSwitch(PlayerObject playerObject) {
        return playerObject.isMarkedForSwitch();
    }

    @Override
    protected void swapFieldPlayers(int teamIndex, int playerIndex1, int playerIndex2) {
        PlayerObject[] playerObjects = getFieldPlayers(teamIndex);
        ArrayUtil.swap(playerObjects, playerIndex1, playerIndex2);
    }

    @Override
    protected void swapReservePlayers(int teamIndex, int playerIndex1, int playerIndex2) {
        PlayerObject[] reservePlayers = getReservePlayers(teamIndex);
        ArrayUtil.swap(reservePlayers, playerIndex1, playerIndex2);
    }

    @Override
    protected void swapFieldAndReservePlayer(int teamIndex, int fieldPlayerIndex, int reservePlayerIndex) {
        PlayerObject[] fieldPlayers = getFieldPlayers(teamIndex);
        PlayerObject[] reservePlayers = getReservePlayers(teamIndex);
        getTeam(teamIndex).addPlayerSwitch(new PlayerSwitch(fieldPlayers[fieldPlayerIndex], reservePlayers[reservePlayerIndex]));
    }

    private Team getTeam(int teamIndex) {
        return getAppState(GameAppState.class).getGame().getTeams()[teamIndex];
    }

    @Override
    protected void confirm() {
        openMenu(PauseIngameMenuAppState.class);
    }

    @Override
    protected void back() {
        openMenu(PauseIngameMenuAppState.class);
    }
}
