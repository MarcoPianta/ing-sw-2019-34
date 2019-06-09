package network.messages;

public class CanUseVenom extends Message {

    public CanUseVenom(Integer token){
        actionType=ActionType.CANUSEVENOM;
        this.token=token;
    }
}
