package network.messages;

import Model.NormalSquare;
import Model.Player;

import java.util.List;

public class UpdateClient extends Message {
    private List<Player> damageBar;
    private NormalSquare square;

    public UpdateClient(Integer token){
        this.actionType = ActionType.UPDATECLIENTS;
        this.token = token;
    }

    /**
     * This constructor is used to build messages to update the damage bar status in clients
     * @param token token of the clients who need to be updated
     * @param damageBar the list which contains the new damageBar to send to the client
     */
    public UpdateClient(Integer token, List<Player> damageBar){
        this(token);
        this.damageBar = damageBar;
    }

    /**
     * This constructor is used to build messages to update the position of a player
     * @param token token of the clients who need to be updated
     * @param square the square which indicates the new position of the player
     */
    public UpdateClient(Integer token, NormalSquare square){
        this(token);
        this.square = square;
    }
}
