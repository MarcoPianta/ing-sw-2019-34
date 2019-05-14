package network.messages;

import Model.CardAmmo;

public class GrabResponse extends Message{
    private CardAmmo ammo;
    private boolean status;

    public GrabResponse(CardAmmo cardAmmo, boolean status){
        actionType = ActionType.GRABRESPONSE;
        ammo = cardAmmo;
        this.status = status;
    }

    public CardAmmo getAmmo() {
        return ammo;
    }

    public boolean getStatus() {
        return status;
    }
}
