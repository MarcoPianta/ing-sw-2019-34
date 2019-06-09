package network.messages;

import Model.CardOnlyAmmo;
import Model.Game;
import Model.NormalSquare;

public class GrabAmmo extends Message{

    public GrabAmmo(Integer token){
        this.token = token;
        actionType=ActionType.GRABAMMO;

    }
}
