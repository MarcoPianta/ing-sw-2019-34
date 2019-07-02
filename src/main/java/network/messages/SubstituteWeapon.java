package network.messages;

import Model.CardWeapon;

import java.util.ArrayList;
import java.util.List;

public class SubstituteWeapon extends Message {


    public SubstituteWeapon(Integer token){
        actionType=ActionType.SUBSTITUTEWEAPON;
        this.token=token;

    }

}
