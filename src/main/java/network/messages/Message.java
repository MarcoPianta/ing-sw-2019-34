package network.messages;

import network.Server.Client;

import java.io.Serializable;

public class Message implements Serializable {
    protected ActionType actionType;
    protected Client client;

    public ActionType getActionType() {
        return actionType;
    }

    public Client getClient() {
        return client;
    }
}
