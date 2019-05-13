package network.messages;

import Model.CardPowerUp;
import Model.CardWeapon;
import Model.Game;

public class ReloadMessage extends Message{
    private CardWeapon weapon;
    private CardPowerUp powerUp;

    public ReloadMessage(CardWeapon weapon, CardPowerUp powerUp){
        actionType=ActionType.RELOAD;
        this.weapon=weapon;
        this.powerUp=powerUp;
    }

    public CardPowerUp getPowerUp() {
        return powerUp;
    }

    public CardWeapon getWeapon() {
        return weapon;
    }
}
