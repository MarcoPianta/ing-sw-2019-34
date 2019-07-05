package network.messages;

import java.io.Serializable;

/**
 * This message is the class which represent a generic message
 */
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
