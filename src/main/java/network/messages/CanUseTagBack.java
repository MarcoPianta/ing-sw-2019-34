package network.messages;

import Model.Colors;

public class CanUseTagBack extends Message{
    private Colors playerShooter;


    public CanUseTagBack(Integer token, Colors playerShooter){
        this.token = token;
        this.actionType = ActionType.CANUSEVENOM;
        this.playerShooter = playerShooter;
    }

    public Colors getPlayerShooter() {
        return playerShooter;
    }
}
