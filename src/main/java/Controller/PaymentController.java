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

    public void payment(int[] cost, List<Integer> powerUps){
        int x;
        ArrayList<CardPowerUp> realPowerUp = new ArrayList<>();

        for(Integer powerUp: powerUps)
            realPowerUp.add(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(powerUp));

        while(!realPowerUp.isEmpty()){
            x = 0;
            if(realPowerUp.get(0).getColor().getAbbreviation().equals(AmmoColors.RED.getAbbreviation()) && cost[0] > 0) {
                cost[0]--;
                x = 1;
            }
            else if(realPowerUp.get(0).getColor().getAbbreviation().equals(AmmoColors.YELLOW.getAbbreviation()) && cost[1] > 0) {
                cost[1]--;
                x = 1;
            }
            else if(realPowerUp.get(0).getColor().getAbbreviation().equals(AmmoColors.BLUE.getAbbreviation()) && cost[2] > 0){
                cost[2]--;
                x = 1;
            }
            if(x == 1)
                gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().removePowerUp(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().indexOf(realPowerUp.get(0)));
            realPowerUp.remove(0);
        }
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().decrementAmmo(cost[0], cost[1], cost[2]);
    }

    /**
     * this method serves to pay the effect o reload,
     * @param cost is the cost to pay
     */
    public void payment(int[] cost){
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().decrementAmmo(cost[0], cost[1], cost[2]);
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
}

