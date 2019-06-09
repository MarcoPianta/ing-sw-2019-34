package network.messages;

import Model.CardNotOnlyAmmo;
import Model.Game;
import Model.NormalSquare;

public class GrabNotOnlyAmmo extends Message{

    public GrabNotOnlyAmmo(Integer token){
        this.token = token;
        actionType=ActionType.GRABNOTONLYAMMO;
    }
}


