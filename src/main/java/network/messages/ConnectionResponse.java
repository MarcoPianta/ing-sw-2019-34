package network.messages;

/**
 * This message is used when a client connected to the server wants to connect to a new game
 * */
public class ConnectionResponse extends Message{
    private Integer token;

    public ConnectionResponse(Integer token){
        this.actionType = ActionType.CONNECTIONRESPONSE;
        this.token = token;
    }
}
