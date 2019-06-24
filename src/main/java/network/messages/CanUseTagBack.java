package network.messages;

public class CanUseTagBack extends Message{
    private Integer index;

    public CanUseTagBack(Integer token, Integer index){
        this.token = token;
        this.actionType = ActionType.CANUSEVENOM;
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }
}
