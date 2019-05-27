package network.messages;

import Model.CardAmmo;

public class GrabResponse extends Message{
    private CardAmmo ammo;
    private boolean status;

    public GrabResponse(Integer token, CardAmmo cardAmmo, boolean status){
        this.token = token;
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
