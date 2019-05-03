package network.messages;

import network.Server.Socket.SocketClient;

public class ConnectionRequest extends Message{
    private final ActionType TYPE=ActionType.CONNECTIONREQUEST;
    private SocketClient client;

    public ConnectionRequest(SocketClient client){
        this.client = client;
    }

    public ActionType getTYPE() {
        return TYPE;
    }

    public SocketClient getClient() {
        return client;
    }
}
