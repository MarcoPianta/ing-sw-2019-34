package network.messages;

public class CanUseTagBack extends Message{

    public CanUseTagBack(Integer token){
        this.token = token;
        this.actionType = ActionType.CANUSEVENOM;
    }
}
