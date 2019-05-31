package network.messages;

import Model.Game;
import Model.NormalSquare;

public class MoveMessage extends Message {
    private  NormalSquare newSquare;

    public MoveMessage(Integer token,NormalSquare newSquare){
        actionType=ActionType.MOVE;
        this.token=token;
        this.newSquare=newSquare;
    }

    public  NormalSquare getNewSquare() {
        return newSquare;
    }

}
