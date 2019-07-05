package network.messages;

/**
 * This message is used to send a message to tell the client he conclude an action
 */
public class FinalAction extends Message {
    public FinalAction(Integer token){
        this.token = token;
        this.actionType = ActionType.FINALACTION;
    }
}
