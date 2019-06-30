package Model;

import java.io.Serializable;

/**
 * This class implements Action
 */
public class Grab implements Action, Serializable {
    private Player actorPlayer;
    private Card grabbedItem;
    private int execution;

    /**
     * @param grabberPlayer The actorPlayer who use the Grab Action
     * @param grabbedCard   The card that is grabbed by the actorPlayer
     */
    public Grab(Player grabberPlayer, CardWeapon grabbedCard){
        actorPlayer = grabberPlayer;
        grabbedItem = grabbedCard;
        execution = 0;
    }

    /**
     * @param grabberPlayer The actorPlayer who use the Grab Action
     * @param grabbedCard   The card that is grabbed by the actorPlayer
     */
    public Grab(Player grabberPlayer, CardOnlyAmmo grabbedCard){
        actorPlayer = grabberPlayer;
        grabbedItem = grabbedCard;
        execution = 1;
    }

    /**
     * @param grabberPlayer The actorPlayer who use the Grab Action
     * @param grabbedCard   The card that is grabbed by the actorPlayer
     */
    public Grab(Player grabberPlayer, CardNotOnlyAmmo grabbedCard){
        actorPlayer = grabberPlayer;
        grabbedItem = grabbedCard;
        execution = 2;
    }

    /**
     * This method execute the Grab Action
     *
     * @return true if the action has been executed, false otherwise
     */
    public boolean execute(){
            if( execution == 0 ){
                SpawnSquare actorSquare = (SpawnSquare) actorPlayer.getPosition();
                actorPlayer.getPlayerBoard().getHandPlayer().addWeapon((CardWeapon) actorSquare.grabItem(actorPlayer.getPosition().getWeapons().indexOf(grabbedItem)));
            }
            else if( execution == 1 ){
                CardOnlyAmmo grabbedOnlyAmmo = (CardOnlyAmmo) grabbedItem;
                grabAmmo(grabbedOnlyAmmo.getItem1().getAbbreviation());
                grabAmmo(grabbedOnlyAmmo.getItem2().getAbbreviation());
                grabAmmo(grabbedOnlyAmmo.getItem3().getAbbreviation());
                }
            else if( execution == 2 ){
                CardNotOnlyAmmo grabbedNotOnlyAmmo = (CardNotOnlyAmmo) grabbedItem;
                grabAmmo(grabbedNotOnlyAmmo.getItem2().getAbbreviation());
                grabAmmo(grabbedNotOnlyAmmo.getItem3().getAbbreviation());
                if(actorPlayer.getPlayerBoard().getHandPlayer().getPlayerPowerUps().size() != 3)
                    actorPlayer.getPlayerBoard().getHandPlayer().addPowerUp(actorPlayer.getGameId().getDeckCollector().getCardPowerUpDrawer().draw());
                //TODO prompt fullPowerUp in else
            }
            return true;
    }

    /**
     * This method control the Pre-condition of the Grab Action
     *
     * @return true if the action invocation respect the condition, false otherwise
     */
    public boolean isValid(){
        if(actorPlayer.getPosition().isSpawn()){

            return actorPlayer.getPosition().getWeapons().contains(grabbedItem);
        }
        else{
            return actorPlayer.getPosition().getItem() == grabbedItem;
        }
    }

    /**
     * This method is invoked by execute method if the grabbed card is a CardAmmo
     *
     * @param ammoColor The string that represent the color of the grabbed ammo
     * @return 0 if RED, 1 if YELLOW, 2 if BLUE
     */
    private int grabAmmo(String ammoColor) {
        int RYB = 0;
        if(ammoColor.equals("R"))
            RYB = 0;
        else if(ammoColor.equals("Y"))
            RYB = 1;
        else if(ammoColor.equals("B"))
            RYB = 2;
        return RYB;
    }
}
