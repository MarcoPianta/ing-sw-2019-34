package network.messages;

/**
 * This message send a disconnection request to the client
 */
public class Disconnect extends Message {

    public Disconnect(Integer token){
        this.token = token;
        this.actionType = ActionType.DISCONNECT;
    }
}
