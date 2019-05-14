package network.messages;

import network.Server.Client;

import java.io.Serializable;

public class Message implements Serializable {
    protected ActionType actionType;

    public ActionType getActionType() {
        return actionType;
    }
}
