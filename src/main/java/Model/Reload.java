package Model;

public class Reload implements Action {
    private CardWeapon selectedWeapon;
    private Player actorPlayer;
    private int r;
    private int b;
    private int y;

    public Reload(Player player, CardWeapon weapon){
        actorPlayer = player;
        selectedWeapon = weapon;
        r = selectedWeapon.getRedCost();
        y = selectedWeapon.getYellowCost();
        b = selectedWeapon.getBlueCost();
    }

    public Reload(Player player, CardWeapon weapon, CardPowerUp powerUp){
        actorPlayer = player;
        selectedWeapon = weapon;
        r = selectedWeapon.getRedCost();
        y = selectedWeapon.getYellowCost();
        b = selectedWeapon.getBlueCost();
        if(powerUp.getColor().getAbbreviation().equals("r"))
            if((r - 1) < 0){
                //TODO throw exception uselessPowerUp
            }
            r -= 1;
        if(powerUp.getColor().getAbbreviation().equals("y"))
            if((y - 1) < 0){
                //TODO throw exception uselessPowerUp
            }
            y -= 1;
        if(powerUp.getColor().getAbbreviation().equals("b"))
            if((b - 1) < 0){
                //TODO throw exception uselessPowerUp
            }
            b -= 1;
    }

    public boolean execute() {
        if(selectedWeapon.isCharge()){
            //TODO throws exception weaponAlreadyCharge
            return true;
        }
        else {
            if (isValid()) {
                actorPlayer.getPlayerBoard().getHandPlayer().chargeWeapon(selectedWeapon, r, y, b);
                return true;
            }
            return false;
        }
    }
/**
 * isValid method return true only if the player has enough Ammo
 * */
    public boolean isValid(){
        int[] a = actorPlayer.getPlayerBoard().getHandPlayer().getAmmoRYB();
        if(a[0] < r || a[1] < y || a[2] < b)
            return false;
        return true;
    }
}
