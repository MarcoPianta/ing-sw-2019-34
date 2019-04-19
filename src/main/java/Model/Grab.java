package Model;
/**
 * This class implements Action
 */
public class Grab implements Action {
    private Player actorPlayer;
    private NormalSquare playerSquare;
    private Card grabbedItem;
    private int execution;

    public void Grab(Player grabberPlayer, CardWeapon grabbedCard){
        actorPlayer = grabberPlayer;
        playerSquare = grabberPlayer.getPosition();
        grabbedItem = grabbedCard;
        execution = 0;
    }

    public void Grab(Player grabberPlayer, CardOnlyAmmo grabbedCard){
        actorPlayer = grabberPlayer;
        playerSquare = grabberPlayer.getPosition();
        grabbedItem = grabbedCard;
        execution = 1;
    }

    public void Grab(Player grabberPlayer, CardNotOnlyAmmo grabbedCard){
        actorPlayer = grabberPlayer;
        playerSquare = grabberPlayer.getPosition();
        grabbedItem = grabbedCard;
        execution = 2;
    }

    public void execute(){
        if( execution == 0 ){
            //TODO è il caso in cui raccolgo un CardWeapon
            // se ne ho gia 3 chiamo la substituteWeapon se no la addWeapon
        }
        else if( execution == 1 ){
            //TODO è il caso in cui raccolgo una CardOnlyAmmo
            // chiamo la addAmmo passando i 3 colori
        }
        else if( execution == 2 ){
            //TODO è il caso in cui raccolgo una CardNotOnlyAmmo
            // chiamo la addAmmo passando i 3 colori
            // chiamo anche la addPowerUp se ne ha meno di 3 se no la substitute
        }
    }

    public boolean isValid(){
        //TODO
        return true;
    }
}

