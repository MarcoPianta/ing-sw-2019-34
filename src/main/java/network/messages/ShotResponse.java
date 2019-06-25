package network.messages;

import java.util.ArrayList;

public class ShotResponse extends Message{
    private ArrayList<String> squareID;
    private String type;

    public ShotResponse(Integer token, ArrayList<String> squareId, String type){
        this.token = token;
        this.actionType = ActionType.SHOTRESPONSE;
        this.squareID = squareId;
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
