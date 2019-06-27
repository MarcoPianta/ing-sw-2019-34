package network.messages;

import Model.CardWeapon;

import java.util.List;

public class SubstituteWeaponResponse extends Message {
    private int weapon;

    public SubstituteWeaponResponse(Integer token, int weapon){
        actionType=ActionType.SUBSTITUTEWEAPON;
        this.token=token;
        this.weapon=weapon;
    }
    public int getWeapon() {
        return weapon;
    }
}
