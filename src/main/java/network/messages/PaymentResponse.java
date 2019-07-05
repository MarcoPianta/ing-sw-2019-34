package network.messages;

import java.util.ArrayList;
import java.util.List;

public class PaymentResponse extends Message{
    private boolean usePowerUp;
    private boolean scoop;
    private List<Integer> powerUp;
    private Integer [] cost;
    private Integer powerUpScoop;
    private String  colorScoop;

    /**
     6     * constructor Payment response
     * @param token
     * @param powerUp list of powerUp to pay cost
     * @param usePowerUp true indicate the payment with powerUp
     * @param scoop true indicate the use of scoop powerup
     */
    public  PaymentResponse(Integer token,List<Integer> powerUp,boolean usePowerUp, Integer[] cost,boolean scoop){
        actionType=ActionType.PAYMENTRESPONSE;
        this.token=token;
        this.powerUp=powerUp;
        this.usePowerUp=usePowerUp;
        this.cost=cost;
        this.scoop=scoop;
        this.powerUpScoop=null;
        this.colorScoop=null;
    }
    public  PaymentResponse(Integer token,List<Integer> powerUp,boolean usePowerUp, Integer[] cost,boolean scoop,String  colorScoop, Integer powerUpScoop){
        actionType=ActionType.PAYMENTRESPONSE;
        this.token=token;
        this.powerUp=powerUp;
        this.usePowerUp=usePowerUp;
        this.cost=cost;
        this.scoop=scoop;
        this.powerUpScoop=powerUpScoop;
        this.colorScoop=colorScoop;
    }

    public Integer[] getCost() {
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
