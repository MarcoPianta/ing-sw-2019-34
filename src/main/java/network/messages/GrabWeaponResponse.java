package network.messages;

import Model.CardWeapon;

public class GrabWeaponResponse extends Message {
    private CardWeapon weapon;
    private boolean status;

    public GrabWeaponResponse(Integer token, CardWeapon weapon, boolean status){
        this.token = token;
        actionType = ActionType.GRABWEAPONRESPONSE;
        this.weapon = weapon;
        this.status = status;
    }

    public CardWeapon getWeapon() {
        return weapon;
    }

    public boolean getStatus() {
        return status;
    }
}
