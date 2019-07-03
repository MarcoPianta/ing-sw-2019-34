package network.messages;

import Model.Colors;

import java.util.ArrayList;

public class ScopeTargetRequest extends Message{
    ArrayList<Colors> targets;

    public ScopeTargetRequest(Integer token, ArrayList<Colors> targets){
        this.token = token;
        this.actionType = ActionType.SCOPETARGETREQUEST;
        this.targets = targets;
    }

    public ArrayList<Colors> getTargets() {
        return targets;
    }
}
