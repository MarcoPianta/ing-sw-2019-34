package network.messages;

import Model.CardNotOnlyAmmo;
import Model.Game;
import Model.NormalSquare;

public class GrabNotOnlyAmmo extends Message{
    private int maxMove;
    private NormalSquare newSquare;
    private CardNotOnlyAmmo card;

    public GrabNotOnlyAmmo(CardNotOnlyAmmo card, int maxMove, NormalSquare newSquare){
        actionType=ActionType.GRABNOTONLYAMMO;
        this.card=card;
        this.maxMove=maxMove;
        this.newSquare=newSquare;
    }

    public int getMaxMove() {
        return maxMove;
    }
    public NormalSquare getNewSquare() {
        return newSquare;
    }

    public CardNotOnlyAmmo getCard() {
        return card;
    }
}


