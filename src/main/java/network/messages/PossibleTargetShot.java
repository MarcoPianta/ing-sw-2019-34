package network.messages;

import Model.Effect;
import Model.Game;

public class PossibleTargetShot extends  Message{
    private Effect effect;

    public PossibleTargetShot(Integer token,Effect effect){
        actionType= ActionType.POSSIBLETARGETSHOT;
        this.token=token;
        this.effect=effect;
    }

    public Effect getEffect() {
        return effect;
    }
}
