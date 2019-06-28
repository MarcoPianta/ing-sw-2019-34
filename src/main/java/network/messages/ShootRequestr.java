package network.messages;

import java.util.List;

public class ShootRequestr extends Message{

    private List<String> roomTargetable;

    public ShootRequestr(Integer token, List<String> roomTargetable){
        this.token = token;
        this.actionType = ActionType.SHOOTREQUESTR;
        this.roomTargetable = roomTargetable;
    }

    public List<String> getRoomTargetable() {
        return roomTargetable;
    }

}
