package network.messages;

import Model.Colors;
import Model.NormalSquare;
import Model.Player;

import java.util.ArrayList;
import java.util.List;

public class UpdateClient extends Message {
    private List<Colors> damageBar; //The damageBar is made of colors to be displayed
    private String squareID; //The id of the square to send in the message
    private ArrayList<String> reachableSquares; //ArrayList of IDs of squares to be send
    private String type; //Type of update (position, damageBar, etc.)
    private String message;

    private void setMessageInfo(Integer token, String type){
        this.actionType = ActionType.UPDATECLIENTS;
        this.token = token;
        this.type = type;
    }

    /**
     * This constructor is used to build messages to update the damage bar status in clients
     * @param token token of the clients who need to be updated
     * @param damageBar the array which contains the new damageBar to send to the client
     */
    public UpdateClient(Integer token, Player[] damageBar){
        setMessageInfo(token, DAMAGEBAR);
        this.damageBar = new ArrayList<>();
        for (Player p: damageBar){
            this.damageBar.add(p.getColor());
        }
    }

    /**
     * This constructor is used to build messages to update the position of a player
     * @param token token of the clients who need to be updated
     * @param square the square which indicates the new position of the player
     */
    public UpdateClient(Integer token, NormalSquare square){
        setMessageInfo(token, POSITION);
        this.squareID = square.getId();
    }

    /**
     * This constructor is used to build messages to send to the client the reachable squares.
     * @param token token of the clients who need to be updated
     * @param squares a list that contains the reachable squares
     */
    public UpdateClient(Integer token, List<NormalSquare> squares){
        setMessageInfo(token, POSSIBLESQUARES);
        for (NormalSquare s: squares)
            this.reachableSquares.add(s.getId());
    }

    /**
     * This constructor is used to build messages to send to the client the possible target.
     * @param token token of the clients who need to be updated
     * @param players a list that contains the reachable squares
     */
    public UpdateClient(Integer token, ArrayList<Player> players){
        setMessageInfo(token, POSSIBLESQUARES);
        for (Player p: players)
            this.reachableSquares.add(p.getPosition().getId());
    }

    /**
     * This constructor is used to build messages to send to the client a text message to be displayed.
     * @param token token of the clients who need to be updated
     * @param message a String that is the message to be sent to the client (for example to send error message)
     */
    public UpdateClient(Integer token, String message){
        setMessageInfo(token, MESSAGE);
        this.message = message;
    }

    public String getUpdateType() {
        return type;
    }

    /**
     * The following static values are used to distinguish the UpdateClient message type.
     */
    public static final String DAMAGEBAR ="DAMAGEBAR";
    public static final String POSITION = "POSITION";
    public static final String POSSIBLESQUARES = "POSSIBLESQUARES";
    public static final String POSSIBLETARGET = "POSSIBLETARGET";
    public static final String MESSAGE = "MESSAGE";


    public List<Colors> getDamageBar() {
        return damageBar;
    }

    public String getSquareID() {
        return squareID;
    }

    public String getMessage() {
        return message;
    }
}
