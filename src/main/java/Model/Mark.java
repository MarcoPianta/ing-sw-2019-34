package Model;

public class Mark implements Action {
    private Colors markColor;
    private Player targetPlayer;
    private Effect markerEffect;
    public void Mark(Player markerPlayer, Player target, Effect effect){
        markColor = markerPlayer.getColor(); //devo ricevere la copia
        targetPlayer = target; //devo ricevere l'originale
        markerEffect = effect; //devo ricevere la copia
    }

    public boolean execute() {
        if (isValid()) {
            return true;
        }
        return false;
    }

    public boolean isValid(){
        //TODO control the pre/post condition
        return true;
    }
}
