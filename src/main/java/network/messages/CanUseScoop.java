package network.messages;

public class CanUseScoop extends Message {

    public CanUseScoop(Integer token){
        this.token = token;
        this.actionType = ActionType.CANUSESCOOP;
    }
}
