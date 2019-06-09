package network.messages;

import Model.Effect;

public class ReceiveTargetSquare extends Message {
    private String type; //"grab","move","shoot"
    private Effect effect;

    public ReceiveTargetSquare(Integer token,String string, Effect effect){
        actionType=ActionType.RECEIVETARGETSQUARE;
        this.token=token;
        this.type=string;
        this.effect=effect;
    }

    public String getType() {
        return type;
    }

    public Effect getEffect() {
        return effect;
    }
}
