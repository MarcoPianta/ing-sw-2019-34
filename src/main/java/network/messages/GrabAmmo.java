package network.messages;

import Model.CardAmmo;
import Model.CardOnlyAmmo;
import Model.Game;
import Model.NormalSquare;

public class GrabAmmo extends Message{
    private int maxMove;
    private NormalSquare newSquare;
    private CardAmmo cardAmmo;

    public GrabAmmo(Integer token,int maxMove, NormalSquare newSquare){
        this.token = token;
        actionType=ActionType.GRABAMMO;
        this.maxMove=maxMove;
        this.newSquare=newSquare;
        this.cardAmmo = newSquare.grabItem();
    }

    public int getMaxMove() {
        return maxMove;
    }

    public NormalSquare getNewSquare() {
        return newSquare;
    }

    public CardAmmo getCard() {
        return cardAmmo;
    }
}
