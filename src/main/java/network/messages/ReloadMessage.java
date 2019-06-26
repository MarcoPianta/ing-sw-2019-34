package network.messages;

import Model.CardPowerUp;
import Model.CardWeapon;
import Model.Game;

import java.util.ArrayList;
import java.util.List;

public class ReloadMessage extends Message{
    private Integer weaponPosition;

    public ReloadMessage(Integer token, Integer weaponPosition){
        this.token = token;
        actionType=ActionType.RELOAD;
        this.weaponPosition=weaponPosition;
    }

    public Integer getWeapon() {
        return weaponPosition;
    }
}
