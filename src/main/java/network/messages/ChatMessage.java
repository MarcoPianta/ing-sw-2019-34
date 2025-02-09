
package network.messages;

/**
 * This message is used to send chat messages
 */
public class ChatMessage extends Message{
    private String message;

    public ChatMessage(Integer token, String message){
        this.token = token;
        this.actionType = ActionType.MESSAGE;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

