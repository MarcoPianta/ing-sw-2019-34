package network.messages;

import Model.*;

import java.util.ArrayList;
import java.util.List;

public class UpdateClient extends Message {
    private List<Colors> damageBar; //The damageBar is made of colors to be displayed
    private String squareID; //The id of the square to send in the message
    private ArrayList<String> reachableSquares; //ArrayList of IDs of squares to be send
    private ArrayList<String> reachableTarget; //ArrayList of possible target Players' square ID
    private String type; //Type of update (position, damageBar, etc.)
    private CardPowerUp powerUp;
    private Hand handPlayer;
    private CardWeapon weapon;
    private CardAmmo ammo;
    private ArrayList<Colors> marks;
    private String textMessage;

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
    public UpdateClient(Integer token, List<Player> damageBar,List<Player> marks){
        setMessageInfo(token, DAMAGEBARUPDATE);
        this.damageBar = new ArrayList<>();
        for (Player p: damageBar){
            this.damageBar.add(p.getColor());
        }
        this.marks = new ArrayList<>();
        for (Player p: marks){
            this.marks.add(p.getColor());
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
        this.reachableSquares = new ArrayList<>();
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
        this.reachableTarget = new ArrayList<>();
        for (Player p: players)
            this.reachableTarget.add(p.getPosition().getId());
    }

    public UpdateClient(Integer token, Hand handPlayer){
        setMessageInfo(token, HANDPLAYER);
        this.handPlayer = handPlayer;
    }

    public UpdateClient(Integer token, CardPowerUp powerUp){
        setMessageInfo(token, RESPAWN);
        this.powerUp = powerUp;
    }
    public UpdateClient(Integer token, String squareId,CardWeapon weapon){
        setMessageInfo(token, FILLSPAWN);
        this.squareID=squareId;
        this.weapon=weapon;
    }
    public UpdateClient(Integer token, String squareId,CardAmmo ammo){
        setMessageInfo(token, FILLSQUARE);
        this.squareID=squareId;
        this.ammo=ammo;
    }

    /**
     * This constructor is used to build messages to send to the client a text message to be displayed.
     * @param token token of the clients who need to be updated
     * @param message a String that is the message to be sent to the client (for example to send error message)
     */
    public UpdateClient(Integer token, String message){
        setMessageInfo(token, MESSAGE);
        this.textMessage = message;
    }

    public String getUpdateType() {
        return type;
    }

    /**
     * The following static values are used to distinguish the UpdateClient message type.
     */
    public static final String POSSIBLETARGET = "POSSIBLETARGET";
    public static final String DAMAGEBARUPDATE ="DAMAGEBAR";
    public static final String POSSIBLESQUARES = "POSSIBLESQUARES";
    public static final String POSITION = "POSITION";
    public static final String RESPAWN = "RESPAWN";
    public static final String MESSAGE = "MESSAGE";
    public static final String HANDPLAYER = "HANDPLAYER";
    public static final String FILLSPAWN = " FILL SPAWN";
    public static final String FILLSQUARE= " FILL SQUARE";

    public ArrayList<String> getReachableTarget() {
        return reachableTarget;
    }

    public List<Colors> getDamageBar() {
        return damageBar;
    }

    public ArrayList<String> getReachableSquares() {
        return reachableSquares;
    }

    public String getSquareID() {
        return squareID;
    }

    public CardPowerUp getPowerUp() {
        return powerUp;
    }

    public String getMessage() {
        return textMessage;
    }

    public CardAmmo getAmmo() {
        return ammo;
    }

    public Hand getHandPlayer() {
        return handPlayer;
    }

    public CardWeapon getWeapon() {
        return weapon;
    }

    public ArrayList<Colors> getMarks() {
        return marks;
    }
}
