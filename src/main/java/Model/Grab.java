package Model;
/**
 * This class implements Action
 */
public class Grab implements Action {
    private Player actorPlayer;
    private NormalSquare playerSquare;
    private Card grabbedItem;
    private int execution;

    /**
     * @param grabberPlayer
     * @param grabbedCard
     */
    public Grab(Player grabberPlayer, CardWeapon grabbedCard){
        actorPlayer = grabberPlayer;
        playerSquare = grabberPlayer.getPosition();
        grabbedItem = grabbedCard;
        execution = 0;
    }

    /**
     * @param grabberPlayer
     * @param grabbedCard
     */
    public Grab(Player grabberPlayer, CardOnlyAmmo grabbedCard){
        actorPlayer = grabberPlayer;
        playerSquare = grabberPlayer.getPosition();
        grabbedItem = grabbedCard;
        execution = 1;
    }

    /**
     * @param grabberPlayer
     * @param grabbedCard
     */
    public Grab(Player grabberPlayer, CardNotOnlyAmmo grabbedCard){
        actorPlayer = grabberPlayer;
        playerSquare = grabberPlayer.getPosition();
        grabbedItem = grabbedCard;
        execution = 2;
    }

    public boolean execute(){
        if(isValid()){
            if( execution == 0 ){
                SpawnSquare actorSquare = (SpawnSquare) actorPlayer.getPosition();
                actorPlayer.getPlayerBoard().getHandPlayer().addWeapon((CardWeapon) grabbedItem);
                actorSquare.grabItem(actorPlayer.getPosition().getWeapons().indexOf(grabbedItem));
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
        return false;
    }

    public boolean isValid(){
        if(actorPlayer.getPosition().isSpawn()){
            return actorPlayer.getPosition().getWeapons().contains(grabbedItem);
        }
        else{
            return actorPlayer.getPosition().getItem() == grabbedItem;
        }
    }

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

