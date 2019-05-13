package network.messages;

import Model.Game;

public class Pass extends  Message{
    public void Pass(){
        actionType=ActionType.PASS;
    }
}
