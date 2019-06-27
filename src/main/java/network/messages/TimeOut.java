package network.messages;

public class TimeOut extends Message {

    public TimeOut(Integer token){
        this.token = token;
        this.actionType = ActionType.TIMEOUT;
    }
}

