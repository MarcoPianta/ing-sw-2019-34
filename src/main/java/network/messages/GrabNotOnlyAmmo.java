package network.messages;

import Model.CardNotOnlyAmmo;
import Model.Game;
import Model.NormalSquare;

public class GrabNotOnlyAmmo extends Message{
    private int maxMove;
    private NormalSquare newSquare;

    public GrabNotOnlyAmmo(Integer token, int maxMove, NormalSquare newSquare){
        this.token = token;
        actionType=ActionType.GRABNOTONLYAMMO;
        this.maxMove=maxMove;
        this.newSquare=newSquare;
    }

    public int getMaxMove() {
        return maxMove;
    }
    public NormalSquare getNewSquare() {
        return newSquare;
    }

}


