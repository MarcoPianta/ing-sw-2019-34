package network.messages;

/**
 * This message is used to communicate to the client that the Game or the turn is started.
 * To differentiate the start of the turn or the Game the String type is used
 */
public class StartMessage extends Message{
    private String type; //This String can assume values "turn" or "game"
    private int skullNumber;
    private String map;

    public StartMessage(Integer token, String type, int skullNumber, String map){
        this.actionType = ActionType.START;
        this.token = token;
        this.type = type;
        this.skullNumber = skullNumber;
        this.map = map;
    }

    public String getType() {
        return type;
    }


}
