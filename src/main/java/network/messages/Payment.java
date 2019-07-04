package network.messages;

import java.util.ArrayList;

public class Payment extends Message {
    private Integer[] cost;
    private Integer powerUp;//-1 if not used , other used

    public  Payment(Integer token,Integer[] cost, Integer powerUp){
        actionType=ActionType.PAYMENT;
        this.token=token;
        this.cost=cost;
        this.powerUp=powerUp;
    }

    public Integer[] getCost() {
        return cost;
    }

    public Integer getPowerUp() {
        return powerUp;
    }
}
