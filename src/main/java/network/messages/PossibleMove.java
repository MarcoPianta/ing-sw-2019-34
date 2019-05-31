package network.messages;

import Model.Game;

public class PossibleMove extends Message{
    private int maxMove;

    public PossibleMove(Integer token,int maxMove){
        actionType=ActionType.POSSIBLEMOVE;
        this.token=token;
        this.maxMove=maxMove;
    }

    public int getMaxMove() {
        return maxMove;
    }
}
