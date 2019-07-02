package network.messages;

public class CanUseScoopResponse extends Message {
    private boolean use;

    public CanUseScoopResponse(Integer token, boolean use){
        this.token = token;
        this.actionType = ActionType.CANUSESCOOPRESPONSE;
        this.use = use;
    }

    public boolean isUse() {
        return use;
    }
}
