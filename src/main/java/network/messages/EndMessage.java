package network.messages;

/**
 * This message is used to send an end of turn to the client
 */
public class EndMessage extends Message {

    public EndMessage(Integer token){
        this.token = token;
        this.actionType = ActionType.END;
    }
}
