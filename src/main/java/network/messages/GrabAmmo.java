package network.messages;

import Model.CardOnlyAmmo;
import Model.Game;
import Model.NormalSquare;

public class GrabAmmo extends Message{
    private int maxMove;
    private NormalSquare newSquare;

    public GrabAmmo(Integer token,int maxMove, NormalSquare newSquare){
        this.token = token;
        actionType=ActionType.GRABAMMO;
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
