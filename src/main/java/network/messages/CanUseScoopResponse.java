package network.messages;

public class CanUseScoopResponse extends Message {
    private boolean use;
    private int position;

    public CanUseScoopResponse(Integer token, boolean use, int position){
        this.token = token;
        this.actionType = ActionType.CANUSESCOOPRESPONSE;
        this.use = use;
        this.position = position;
    }

    public boolean isUse() {
        return use;
    }

    public int getPosition() {
        return position;
    }
}
