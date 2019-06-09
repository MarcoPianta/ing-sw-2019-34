package network.messages;

import Model.CardPowerUp;
import Model.CardWeapon;
import Model.Game;

import java.util.ArrayList;
import java.util.List;

public class ReloadMessage extends Message{
    private Integer weaponPosition;
    private List<Integer> positionPowerUp;

    public ReloadMessage(Integer token, Integer weaponPosition, List<Integer> positionPowerUp){
        this.token = token;
        actionType=ActionType.RELOAD;
        this.weaponPosition=weaponPosition;
        this.positionPowerUp=positionPowerUp;
    }

    public List<Integer> getPowerUp() {
        return positionPowerUp;
    }

    public Integer getWeapon() {
        return weaponPosition;
    }
}
