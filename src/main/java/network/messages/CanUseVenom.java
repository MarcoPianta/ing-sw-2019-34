package network.messages;

import Model.Player;

public class CanUseVenom extends Message {
    private Player users;

    public CanUseVenom(Integer token){
        actionType=ActionType.CANUSEVENOM;
        this.token=token;
    }
}
