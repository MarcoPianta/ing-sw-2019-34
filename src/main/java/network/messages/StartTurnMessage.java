package network.messages;
import Model.Player;

import java.util.ArrayList;

public class StartTurnMessage extends Message {
    private Player currentPlayer;
    private ArrayList<Player> deadPlayers;

    public StartTurnMessage(Integer token,Player currentPlayer,ArrayList<Player> deadPlayers){
        actionType=ActionType.STARTTURNMESSAGE;
        this.token=token;
        this.currentPlayer=currentPlayer;
        this.deadPlayers=deadPlayers;
    }

    public ArrayList<Player> getDeadPlayers() {
        return deadPlayers;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}

