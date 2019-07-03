package network.messages;

public class FinalTurnMessage extends Message {

    public FinalTurnMessage(Integer token){
        this.token = token;
        this.actionType = ActionType.FINALTURN;
    }
}
