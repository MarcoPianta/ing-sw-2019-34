package network.messages;

/**
 * This message is sended by Client to Server as a response of ShootRequests
 * with this message send the ID of selected square
 */
public class ShootResponses extends Message{

    private String targetSquare;

    public ShootResponses(Integer token, String targetSquare){
        this.token = token;
        this.actionType = ActionType.SHOOTRESPONSES;
        this.targetSquare = targetSquare;
    }


    public String getTargetSquare() {
        return targetSquare;
    }

}
