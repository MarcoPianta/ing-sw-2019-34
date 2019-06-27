package network.messages;

import Model.Game;
import Model.NormalSquare;
import Model.Player;

public class MoveMessage extends Message {
    private  NormalSquare newSquare;
    private Player playerTarget;

    public MoveMessage(Integer token,Player playerTarget,NormalSquare newSquare){
        actionType=ActionType.MOVE;
        this.token=token;
        this.newSquare=newSquare;
        this.playerTarget=playerTarget;
    }

    public  NormalSquare getNewSquare() {
        return newSquare;
    }

    public Player getPlayerTarget() {
        return playerTarget;
    }
}
