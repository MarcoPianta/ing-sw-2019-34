package network.messages;

import Model.Game;

public class PossibleMove extends Message{
    private int maxMove;

    public PossibleMove(int maxMove){
        actionType=ActionType.POSSIBLEMOVE;
        this.maxMove=maxMove;
    }

    public int getMaxMove() {
        return maxMove;
    }
}