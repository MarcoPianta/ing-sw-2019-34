package network.messages;

import Model.NormalSquare;
import Model.Player;

public class UsePowerUpResponse extends Message {
    private Integer powerUp;
    private Integer target;
    private Integer user;
    private NormalSquare square;

    public  UsePowerUpResponse(Integer token,Integer cardPowerUp,Integer user,Integer target,NormalSquare square){
        actionType=ActionType.USEPOWERUP;
        this.token=token;
        this.powerUp=cardPowerUp;
        this.target=target;
        this.square=square;
        this.user=user;
    }

    public Integer getPowerUp() {
        return powerUp;
    }

    public NormalSquare getSquare() {
        return square;
    }

    public Integer getTarget() {
        return target;
    }

    public Integer getUser() {
        return user;
    }
}
