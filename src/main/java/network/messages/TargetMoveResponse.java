package network.messages;

/**
 * This message is sended by Client to Server as a response of TargetMoveRequest
 * with this message send the ID related to the square in which the target has to be moved
 *  */
public class TargetMoveResponse extends Message{

    private String targetSquare;

    public TargetMoveResponse(Integer token, String targetSquare){
        this.token = token;
        this.actionType = ActionType.TARGETMOVERESPONSE;
        this.targetSquare = targetSquare;
    }

    public String getTargetSquare() {
        return targetSquare;
    }

}
