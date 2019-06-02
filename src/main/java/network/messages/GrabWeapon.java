package network.messages;

import Model.CardWeapon;
import Model.Game;
import Model.NormalSquare;

public class GrabWeapon extends Message {

    private int maxMove;
    private NormalSquare newSquare;
    private int positionWeapon;
    private CardWeapon cardWeapon;

    public GrabWeapon(Integer tokenint,int  maxMove,NormalSquare newSquare,int positionWeapon){
        this.token = token;
        actionType=ActionType.GRABWEAPON;
        this.maxMove=maxMove;
        this.newSquare=newSquare;
        this.positionWeapon=positionWeapon;
        this.cardWeapon = newSquare.getWeapons().get(positionWeapon);
    }

    public int getMaxMove() {
        return maxMove;
    }

    public NormalSquare getNewSquare() {
        return newSquare;
    }

    public int getPositionWeapon() {
        return positionWeapon;
    }

    public CardWeapon getCard() {
        return cardWeapon;
    }
}
