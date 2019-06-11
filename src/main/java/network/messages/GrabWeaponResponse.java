package network.messages;

import Model.CardWeapon;

public class GrabWeaponResponse extends Message {
    private Integer weapon;
    private boolean status;

    public GrabWeaponResponse(Integer token, Integer position, boolean status){
        this.token = token;
        actionType = ActionType.GRABWEAPONRESPONSE;
        this.weapon = weapon;
        this.status = status;
    }

    public Integer getWeapon() {
        return weapon;
    }

    public boolean getStatus() {
        return status;
    }
}
