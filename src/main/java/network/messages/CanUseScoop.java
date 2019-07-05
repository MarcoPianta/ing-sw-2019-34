package network.messages;

/**
 * This message is used to send to the client a message that it can use scope power up
 */
public class CanUseScoop extends Message {

    public CanUseScoop(Integer token){
        this.token = token;
        this.actionType = ActionType.CANUSESCOOP;
    }
}
