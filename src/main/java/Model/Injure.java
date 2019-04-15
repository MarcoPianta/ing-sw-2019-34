package Model;

public class Injure implements Action {
    private Colors damageColor;
    private Player targetPlayer;
    private Effect injurerEffect;
    public void Injure(Player shooterPlayer, Player target, Effect effect){
        damageColor = shooterPlayer.getPlayerBoard().getColor(); //devo ricevere la copia
        targetPlayer = target; //devo ricevere l'originale
        injurerEffect = effect; //devo ricevere la copia
    }

    public void execute() {
        targetPlayer.getPlayerBoard().addDamage(damageColor, injurerEffect.getMark());
    }

    public boolean isValid(){
        //TODO control the pre/post condition
        return true;
    }
}
