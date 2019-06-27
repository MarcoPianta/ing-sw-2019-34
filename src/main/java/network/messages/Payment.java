package network.messages;

import java.util.ArrayList;

public class Payment extends Message {
    private int[] cost;
    private boolean powerUp;

    public  Payment(Integer token,int[] cost, boolean powerUp){
        actionType=ActionType.PAYMENT;
        this.token=token;
        this.cost=cost;
        this.powerUp=powerUp;
    }

    public int[] getCost() {
        return cost;
    }

    public boolean isPowerUp() {
        return powerUp;
    }
}
