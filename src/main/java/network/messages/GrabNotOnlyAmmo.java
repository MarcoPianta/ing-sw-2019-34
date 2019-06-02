package network.messages;

import Model.CardAmmo;
import Model.NormalSquare;

public class GrabNotOnlyAmmo extends Message{
    private int maxMove;
    private NormalSquare newSquare;
    private CardAmmo cardNotOnlyAmmo;

    public GrabNotOnlyAmmo(Integer token, int maxMove, NormalSquare newSquare){
        this.token = token;
        actionType=ActionType.GRABNOTONLYAMMO;
        this.maxMove=maxMove;
        this.newSquare=newSquare;
        this.cardNotOnlyAmmo = newSquare.grabItem();
    }

    public int getMaxMove() {
        return maxMove;
    }

    public NormalSquare getNewSquare() {
        return newSquare;
    }

    public CardAmmo getCard() {
        return cardNotOnlyAmmo;
    }
}


