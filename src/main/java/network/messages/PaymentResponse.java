package network.messages;

import java.util.ArrayList;
import java.util.List;

public class PaymentResponse extends Message{
    private boolean usePowerUp;
    private boolean scoop;
    private List<Integer> powerUp;
    private int [] cost;
    private Integer powerUpScoop;
    private String  colorScoop;

    public  PaymentResponse(Integer token,List<Integer> powerUp,boolean usePowerUp, int[] cost,boolean scoop){
        actionType=ActionType.PAYMENTRESPONSE;
        this.token=token;
        this.powerUp=powerUp;
        this.usePowerUp=usePowerUp;
        this.cost=cost;
        this.scoop=scoop;
        this.powerUpScoop=null;
        this.colorScoop=null;
    }
    public  PaymentResponse(Integer token,List<Integer> powerUp,boolean usePowerUp, int[] cost,boolean scoop,String  colorScoop){
        actionType=ActionType.PAYMENTRESPONSE;
        this.token=token;
        this.powerUp=powerUp;
        this.usePowerUp=usePowerUp;
        this.cost=cost;
        this.scoop=scoop;
        this.powerUpScoop=null;
        this.colorScoop=colorScoop;
    }
    public  PaymentResponse(Integer token, List<Integer> powerUp, boolean usePowerUp, int[] cost, boolean scoop, Integer powerUpScoop){
        actionType=ActionType.PAYMENTRESPONSE;
        this.token=token;
        this.powerUp=powerUp;
        this.usePowerUp=usePowerUp;
        this.cost=cost;
        this.scoop=scoop;
        this.powerUpScoop=powerUpScoop;
        this.colorScoop=null;
    }

    public int[] getCost() {
        return cost;
    }

    public List<Integer> getPowerUp() {
        return powerUp;
    }

    public boolean isUsePowerUp() {
        return usePowerUp;
    }

    public boolean isScoop() {
        return scoop;
    }

    public Integer getPowerUpScoop() {
        return powerUpScoop;
    }

    public String getColorScoop() {
        return colorScoop;
    }
}
