package network.messages;

import Model.CardPowerUp;
import Model.NormalSquare;
import Model.Player;

public class UsePowerUp extends Message{
    private Integer powerUp;
    private Player target;
    private Player user;
    private NormalSquare square;
    private int maxMove;


    public  UsePowerUp(Integer token,Integer cardPowerUp,Player user,Player target,NormalSquare square,int  maxMove){
        actionType=ActionType.USEPOWERUP;
        this.token=token;
        this.powerUp=cardPowerUp;
        this.target=target;
        this.square=square;
        this.maxMove=maxMove;
        this.user=user;
    }

    public Integer getPowerUp() {
        return powerUp;
    }

    public int getMaxMove() {
        return maxMove;
    }

    public NormalSquare getSquare() {
        return square;
    }

    public Player getTarget() {
        return target;
    }

    public Player getUser() {
        return user;
    }
}
