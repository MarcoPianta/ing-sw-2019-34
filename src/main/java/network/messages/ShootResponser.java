package network.messages;

import java.util.List;

public class ShootResponser extends Message{

    private String targetRoom;

    public ShootResponser(Integer token, String targetRoom){
        this.token = token;
        this.actionType = ActionType.SHOOTRESPONSER;
        this.targetRoom = targetRoom;
    }


    public String getTargetRoom() {
        return targetRoom;
    }

}
