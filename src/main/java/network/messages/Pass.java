package network.messages;

import Model.Game;

public class Pass extends  Message{
    public void Pass(Integer token){
        actionType=ActionType.PASS;
        this.token=token;
    }
}
