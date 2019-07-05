package network.messages;

import Model.Colors;

/**
 * This message is used to send to the client a message that it can use tag back power up
 */
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
