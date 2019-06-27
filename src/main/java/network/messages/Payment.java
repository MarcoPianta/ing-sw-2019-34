package network.messages;

import java.util.ArrayList;

public class Payment extends Message {
    private Integer[] cost;
    private boolean powerUp;

    public  Payment(Integer token,Integer[] cost, boolean powerUp){
        actionType=ActionType.PAYMENT;
        this.token=token;
        this.cost=cost;
        this.powerUp=powerUp;
    }

    public Integer[] getCost() {
        return cost;
    }

    public boolean isPowerUp() {
        return powerUp;
    }
}
