package network.messages;

public class CanUseScoopResponse extends Message {
    public CanUseScoopResponse(Integer token){
        this.token = token;
        this.actionType = ActionType.CANUSESCOOPRESPONSE;
    }
}
