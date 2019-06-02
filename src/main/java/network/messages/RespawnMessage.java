package network.messages;

import Model.CardPowerUp;

public class RespawnMessage extends Message{
    private CardPowerUp chosenPowerUp;

    public RespawnMessage(Integer token, CardPowerUp powerUp){
        this.token = token;
        this.actionType = ActionType.RESPAWN;
        this.chosenPowerUp = powerUp;
    }

    public CardPowerUp getPowerUp() {
        return chosenPowerUp;
    }
}
