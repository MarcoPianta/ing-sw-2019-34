package network.messages;

public class MoveResponse extends Message {
    private String squareId;

    public MoveResponse(Integer token, String id){
        this.token = token;
        this.actionType = ActionType.MOVERESPONSE;
        squareId = id;
    }

    public String getSquareId() {
        return squareId;
    }
}
