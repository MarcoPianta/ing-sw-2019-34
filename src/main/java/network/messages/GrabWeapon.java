package network.messages;

import Model.CardWeapon;
import Model.Game;
import Model.NormalSquare;

public class GrabWeapon extends Message {

    private int positionWeapon;

    public GrabWeapon(Integer tokenint,int positionWeapon){
        this.token = token;
        actionType=ActionType.GRABWEAPON;
        this.positionWeapon=positionWeapon;

    }

    public int getPositionWeapon() {
        return positionWeapon;
    }

}
