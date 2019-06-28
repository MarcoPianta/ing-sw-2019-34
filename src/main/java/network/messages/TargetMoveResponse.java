package network.messages;

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
