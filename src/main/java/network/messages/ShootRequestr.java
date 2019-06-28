package network.messages;

import java.util.List;

/**
 * This message is sended by GameLobby to the Client when a Shoot Action contain a 'r' element in its actionSequence
 * with this message send the List of ID related to the square that should be selected as target for a RoomShot
 */
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
