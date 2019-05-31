package network.messages;

import Model.CardWeapon;

import java.util.ArrayList;
import java.util.List;

public class SubstituteWeapon extends Message {
    private List<CardWeapon> weapons;

    public SubstituteWeapon(Integer token, List<CardWeapon> weapons){
        actionType=ActionType.SUBSTITUTEWEAPON;
        this.token=token;
        this.weapons=weapons;
    }

    public List<CardWeapon> getWeapons() {
        return weapons;
    }
}
