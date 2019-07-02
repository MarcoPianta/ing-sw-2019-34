package network.messages;

public class Ping extends Message {

    public Ping(Integer token){
        this.token = token;
        this.actionType = ActionType.PING;
    }
}
