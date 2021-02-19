package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameAppState;
import com.carlkuesters.fifachampions.game.Formation;
import com.carlkuesters.fifachampions.game.Player;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.Team;

public class PauseFormationMenuAppState extends FormationMenuAppState {

    @Override
    protected Formation getFormation(int teamIndex) {
        return getTeam(teamIndex).getFormation();
    }

    @Override
    protected void setFormation(int teamIndex, Formation formation) {
        getTeam(teamIndex).setFormation(formation);
    }

    @Override
    protected Player[] getFieldPlayers(int teamIndex) {
        return getTeam(teamIndex).getPlayers().stream()
                .map(PlayerObject::getPlayer)
                .toArray(Player[]::new);
    }

    @Override
    protected Player[] getReservePlayers(int teamIndex) {
        return getTeam(teamIndex).getReservePlayers();
    }

    @Override
    protected void swapFieldPlayers(int teamIndex, int playerIndex1, int playerIndex2) {
        // TODO
    }

    @Override
    protected void swapReservePlayers(int teamIndex, int playerIndex1, int playerIndex2) {
        // TODO
    }

    @Override
    protected void swapFieldAndReservePlayer(int teamIndex, int fieldPlayerIndex, int reservePlayerIndex) {
        // TODO
    }

    private Team getTeam(int teamIndex) {
        // TODO: Why do I have to cast here?
        GameAppState gameAppState = (GameAppState) getAppState(GameAppState.class);
        return gameAppState.getGame().getTeams()[teamIndex];
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
