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

    public void payment(List<Integer> cost, List<Integer> powerUp){
        for(Integer i:powerUp){
            if(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getColor().getAbbreviation().equals(Colors.RED.getAbbreviation()))
                cost.set(0,cost.get(0) -1);
            else if(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getColor().getAbbreviation().equals(Colors.YELLOW.getAbbreviation()))
                cost.set(1,cost.get(1) -1);
            else if(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getColor().getAbbreviation().equals(Colors.BLUE.getAbbreviation()))
                cost.set(2,cost.get(2) -1);
            gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().removePowerUp(i);
        }
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().decrementAmmo(cost.get(0),cost.get(1),cost.get(2));
    }

    /**
     * this method serves to pay the effect o reload,
     * @param cost is the cost to pay
     */
    public void payment(List<Integer> cost){
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().decrementAmmo(cost.get(0),cost.get(1),cost.get(2));
    }

    /**
     * this methos serves to pay the powerUp with an other powerUp
     * @param powerUp power with which to pay
     * @return true if the action is possible
     */
    public boolean paymentPowerUp(int powerUp){
        boolean valueReturn;
        if(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(powerUp)!=null){
            gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().removePowerUp(powerUp);
            valueReturn=true;
        }
        else
            valueReturn=false;
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

