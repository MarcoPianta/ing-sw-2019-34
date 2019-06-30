package Model;

import java.io.Serializable;

/**
 * This class implements Action
 */
public class Reload implements Action, Serializable {
    private CardWeapon selectedWeapon;
    private Player actorPlayer;
    private int r;
    private int b;
    private int y;

/**
 * @param reloaderPlayer    The actorPlayer who use the Grab Action
 * @param grabbedWeapon     The card that is grabbed by the actorPlayer
 */
    public Reload(Player reloaderPlayer, CardWeapon grabbedWeapon){
        this.actorPlayer = reloaderPlayer;
        this.selectedWeapon = grabbedWeapon;
        this.r = selectedWeapon.getRedCost();
        this.y = selectedWeapon.getYellowCost();
        this.b = selectedWeapon.getBlueCost();
    }


    public Reload(Player reloaderPlayer, CardWeapon grabbedWeapon, int r, int y, int b){
        this.actorPlayer = reloaderPlayer;
        this.selectedWeapon = grabbedWeapon;
        this.r = r;
        this.y = y;
        this.b = b;
    }

/**
 * If selectedWeapon is already charged throws an exception
 * Otherwise invoke the isValid method that control the Pre-condition of the action
 * This method execute the Reload Action
 *
 * @return true if the action has been executed, false otherwise
 */
    public boolean execute() {
        actorPlayer.getPlayerBoard().getHandPlayer().chargeWeapon(selectedWeapon, r, y, b);
        return true;
    }

/**
 * Control the Pre-condition of the Reload Action
 *
 * @return true if the action invocation respect the condition, false otherwise
 */
    public boolean isValid(){
        return true;
    }
}
