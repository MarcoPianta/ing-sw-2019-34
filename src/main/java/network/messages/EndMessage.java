package network.messages;

public class EndMessage extends Message {

    public EndMessage(Integer token){
        this.token = token;
        this.actionType = ActionType.END;
    }
}
