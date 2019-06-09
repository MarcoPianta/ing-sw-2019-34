package network.messages;

import Model.CardAmmo;
import Model.NormalSquare;

public class GrabNotOnlyAmmo extends Message{

    public GrabNotOnlyAmmo(Integer token){
        this.token = token;
        actionType=ActionType.GRABNOTONLYAMMO;
    }
}


