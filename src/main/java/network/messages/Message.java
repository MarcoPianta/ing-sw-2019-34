package network.messages;

import java.io.Serializable;

public class Message implements Serializable {
    protected ActionType actionType;
    protected Integer token;

    public ActionType getActionType() {
        return actionType;
    }

    public Integer getToken() {
        return token;
    }
}
