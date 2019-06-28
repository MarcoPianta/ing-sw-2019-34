package network.messages;

/**
 * This message is sended by Client to Server as a response of ShootRequestr
 * with this message send the ID related to the square in which the target has to be moved
 *  */
public class TargetMoveResponse extends Message{

    private Integer targetPlayer;

    public TargetMoveResponse(Integer token, Integer targetSquare){
        this.token = token;
        this.actionType = ActionType.TARGETMOVERESPONSE;
        this.targetPlayer = targetSquare;
    }

    public Integer getTargetPlayer() {
        return targetPlayer;
    }

}
