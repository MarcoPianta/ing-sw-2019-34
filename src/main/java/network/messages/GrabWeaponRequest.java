package network.messages;

/**
 * This message send a grab request to the player
 */
public class GrabWeaponRequest extends Message{

    public GrabWeaponRequest(Integer token){
        this.token = token;
        this.actionType = ActionType.GRABWEAPONREQUEST;
    }
}
