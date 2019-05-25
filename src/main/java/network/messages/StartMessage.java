package network.messages;

/**
 * This message is used to communicate to the client that the Game or the turn is started.
 * To differentiate the start of the turn or the Game the String type is used
 */
public class StartMessage extends Message{
    private String type; //This String can assume values "turn" or "game"

    public StartMessage(Integer token, String type){
        this.actionType = ActionType.START;
        this.token = token;
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
