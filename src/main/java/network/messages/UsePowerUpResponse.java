package network.messages;

import Model.Card;
import Model.Colors;
import Model.NormalSquare;
import Model.Player;

public class UsePowerUpResponse extends Message {
    private Integer powerUp;
    private Colors target;
    private Integer user;
    private String square;

    public  UsePowerUpResponse(Integer token, Integer cardPowerUp, Integer user, Colors target, String square){
        actionType=ActionType.USEPOWERUPRESPONSE;
        this.token=token;
        this.powerUp=cardPowerUp;
        this.target=target;
        this.square=square;
        this.user=user;
    }

    public Integer getPowerUp() {
        return powerUp;
    }

    public String getSquare() {
        return square;
    }

    public Colors getTarget() {
        return target;
    }

    public Integer getUser() {
        return user;
    }
}
