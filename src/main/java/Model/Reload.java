package Model;

import java.util.List;

/**
 * This class implements Action
 */
public class Reload implements Action {
    private CardWeapon selectedWeapon;
    private Player actorPlayer;
    private List<CardPowerUp> usedPowerUps;
    private int r;
    private int b;
    private int y;

    /**
     * @param reloaderPlayer    The actorPlayer who use the Grab Action
     * @param grabbedWeapon     The card that is grabbed by the actorPlayer
     */
    public Reload(Player reloaderPlayer, CardWeapon grabbedWeapon){
        actorPlayer = reloaderPlayer;
        selectedWeapon = grabbedWeapon;
        r = selectedWeapon.getRedCost();
        y = selectedWeapon.getYellowCost();
        b = selectedWeapon.getBlueCost();
    }

    /**
     * @param reloaderPlayer    The actorPlayer who use the Grab Action
     * @param grabbedWeapon     The card that is grabbed by the actorPlayer
     * @param powerUps           List of powerUp used as a reload payment
     */
    public Reload(Player reloaderPlayer, CardWeapon grabbedWeapon, List<CardPowerUp> powerUps){
        actorPlayer = reloaderPlayer;
        selectedWeapon = grabbedWeapon;
        usedPowerUps = powerUps;
        r = selectedWeapon.getRedCost();
        y = selectedWeapon.getYellowCost();
        b = selectedWeapon.getBlueCost();
    }

    private boolean controlPowerUp(CardPowerUp powerUp) {
        if(powerUp.getColor().getAbbreviation().equals("r"))
            if((r - 1) < 0){
                return false;
            }
        r -= 1;
        if(powerUp.getColor().getAbbreviation().equals("y"))
            if((y - 1) < 0){
                return false;
            }
        y -= 1;
        if(powerUp.getColor().getAbbreviation().equals("b"))
            if((b - 1) < 0){
                return false;
            }
        b -= 1;
        return true;
    }

    /**
     * If selectedWeapon is already charged throws an exception
     * Otherwise invoke the isValid method that control the Pre-condition of the action
     * This method execute the Reload Action
     *
     * @return true if the action has been executed, false otherwise
     */
    public boolean execute() {
        if(selectedWeapon.isCharge()){
            //TODO throws exception weaponAlreadyCharge
            return true;
        }
        else {
            if (isValid()) {
                actorPlayer.getPlayerBoard().getHandPlayer().chargeWeapon(selectedWeapon, r, y, b);
                for (CardPowerUp powerUp: usedPowerUps) {
                    actorPlayer.getPlayerBoard().getHandPlayer().removePowerUp(actorPlayer.getPlayerBoard().getHandPlayer().getPlayerPowerUps().indexOf(powerUp));
                }
             return true;
            }
            return false;
        }
    }

    /**
     * Control the Pre-condition of the Reload Action
     *
     * @return true if the action invocation respect the condition, false otherwise
     */
    public boolean isValid(){
        for (CardPowerUp powerUp: usedPowerUps)
            if(controlPowerUp(powerUp))
                return false;
        int[] a = actorPlayer.getPlayerBoard().getHandPlayer().getAmmoRYB();
        if(a[0] < r || a[1] < y || a[2] < b)
            return false;
        return true;
    }
}
