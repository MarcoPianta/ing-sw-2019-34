package network.messages;

import Model.CardPowerUp;

public class RespawnMessage extends Message{
    private int chosenPowerUp;

    public RespawnMessage(Integer token, int powerUp){
        this.token = token;
        this.actionType = ActionType.RESPAWN;
        this.chosenPowerUp = powerUp;
    }

    public int getPowerUp() {
        return chosenPowerUp;
    }
}
