package network.messages;

/**
 * This message is used when a client connected to the server wants to connect to a new game
 * */
public class ConnectionRequest extends Message{

    public ConnectionRequest(){
        this.actionType = ActionType.CONNECTIONREQUEST;
    }
}
