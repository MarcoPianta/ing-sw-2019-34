package network.messages;

import Model.Colors;

public class ScopeTargetResponse extends Message {
    private Colors target;

    public ScopeTargetResponse(Integer token, Colors target){
        this.token = token;
        this.actionType = ActionType.SCOPETARGETRESPONSE;
        this.target = target;
    }

    public Colors getTarget() {
        return target;
    }
}
