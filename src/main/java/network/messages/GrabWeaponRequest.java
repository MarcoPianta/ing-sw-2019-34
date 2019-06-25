package network.messages;

public class GrabWeaponRequest extends Message{

    public GrabWeaponRequest(Integer token){
        this.token = token;
        this.actionType = ActionType.GRABWEAPONREQUEST;
    }
}
