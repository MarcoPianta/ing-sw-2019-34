package network.messages;

public class Disconnect extends Message {

    public Disconnect(Integer token){
        this.token = token;
        this.actionType = ActionType.DISCONNECT;
    }
}
