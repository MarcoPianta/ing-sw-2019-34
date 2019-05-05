package network.messages;

import network.Server.Client;

/**
 * This message is used when a client connected to the server wants to connect to a new game
 * */
public class ConnectionRequest extends Message{

    public ConnectionRequest(Client client){
        this.client = client;
        this.actionType = ActionType.CONNECTIONREQUEST;
    }
}
