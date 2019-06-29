package network.messages;

public class FinalAction extends Message {
    public FinalAction(Integer token){
        this.token = token;
        this.actionType = ActionType.FINALACTION;
    }
}
