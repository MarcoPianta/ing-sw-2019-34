package network.messages;

import Model.CardWeapon;

public class ReloadedMessage extends Message{
    private boolean status;
    private CardWeapon weapon;

    public ReloadedMessage(Integer token, CardWeapon weapon, boolean status){
        this.token = token;
        actionType = ActionType.RELOADED;
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
