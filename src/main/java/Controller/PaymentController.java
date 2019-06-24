package Controller;

import Model.AmmoColors;
import Model.CardPowerUp;
import Model.Colors;

import java.util.ArrayList;
import java.util.List;

public class PaymentController {
    private GameHandler gameHandler;

    public PaymentController(GameHandler gameHandler){
        this.gameHandler=gameHandler;
    }

    public boolean payment(int[] cost, List<CardPowerUp> powerUps){
        if(isValidPayment(cost, powerUps))
            return false;
        boolean x;
        while(!powerUps.isEmpty()){
            x = false;
            if(powerUps.get(0).getColor().getAbbreviation().equals(AmmoColors.RED.getAbbreviation()) && cost[0] > 0) {
                cost[0]--;
                x = true;
            }
            else if(powerUps.get(0).getColor().getAbbreviation().equals(AmmoColors.YELLOW.getAbbreviation()) && cost[1] > 0) {
                cost[1]--;
                x = true;
            }
            else if(powerUps.get(0).getColor().getAbbreviation().equals(AmmoColors.BLUE.getAbbreviation()) && cost[2] > 0){
                cost[2]--;
                x = true;
            }
            if(x)
                gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().removePowerUp(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().indexOf(powerUps.get(0)));
            powerUps.remove(0);
        }
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().decrementAmmo(cost[0], cost[1], cost[2]);
        return true;
    }

    /**
     * this method serves to pay the effect o reload,
     * @param cost is the cost to pay
     */
    public boolean payment(int[] cost){
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().decrementAmmo(cost[0], cost[1], cost[2]);
        return true;
    }

    /**
     * this methos serves to pay the powerUp with an other powerUp
     * @param powerUp power with which to pay
     * @return true if the action is possible
     */
    public boolean paymentPowerUp(int powerUp){
        boolean valueReturn;
        if(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(powerUp)!= null){
            gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().removePowerUp(powerUp);
            valueReturn = true;
        }
        else
            valueReturn = false;
        return  valueReturn;
    }

    /**
     * this methos serves to pay the powerUp
     * @param ammo indicates the color of ammo to be removed
     * @return true if the action is possible
     */
    public boolean paymentPowerUp(String ammo){
        //r,y,b
        boolean valueReturn=false;
        if(ammo.equals("r") && gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0]!=0 ){
            gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().decrementAmmo(1,0,0);
            valueReturn=true;
        }
        else if(ammo.equals("y") && gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1]!=0 ){
            gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().decrementAmmo(0,1,0);
            valueReturn=true;
        }
        else if(ammo.equals("b") && gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2]!=0 ){
            gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().decrementAmmo(0,0,1);
            valueReturn=true;
        }
        return valueReturn;
    }

    public boolean isValidPayment(int[] cost, List<CardPowerUp> powerUps){
        int i = 0;
        while(i < powerUps.size()) {
            if (powerUps.get(0).getColor().getAbbreviation().equals(AmmoColors.RED.getAbbreviation()) && cost[0] > 0) {
                cost[0]--;
            } else if (powerUps.get(0).getColor().getAbbreviation().equals(AmmoColors.YELLOW.getAbbreviation()) && cost[1] > 0) {
                cost[1]--;
            } else if (powerUps.get(0).getColor().getAbbreviation().equals(AmmoColors.BLUE.getAbbreviation()) && cost[2] > 0) {
                cost[2]--;
            }
            else
                return false;
            i++;
        }
        return true;
    }
}

