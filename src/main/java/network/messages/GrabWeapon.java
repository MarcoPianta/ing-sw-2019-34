package network.messages;

import Model.CardWeapon;
import Model.Game;
import Model.NormalSquare;

public class GrabWeapon extends Message {

    private int maxMove;
    private NormalSquare newSquare;
    private CardWeapon card;

    public GrabWeapon(Integer token, CardWeapon card,int maxMove,NormalSquare newSquare){
        this.token = token;
        actionType=ActionType.GRABWEAPON;
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

    public CardWeapon getCard() {
        return card;
    }
}
