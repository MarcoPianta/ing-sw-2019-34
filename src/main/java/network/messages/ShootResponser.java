package network.messages;

/**
 * This message is sended by Client to Server as a response of ShootRequestr
 * with this message send the ID related to a square of the selected Room for  a RoomShoot
 */
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
