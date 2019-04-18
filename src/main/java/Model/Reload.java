package Model;

public class Reload implements Action {
    private CardWeapon selectedWeapon;
    private Player actorPlayer;

    public void Injure(Player player, CardWeapon weapon){
        selectedWeapon = weapon;
        actorPlayer = player;
    }

    public void execute() {
        if(!selectedWeapon.isCharge()){
            //TODO throws exception weaponAlreadyCharge
        }
        else if(isValid()){
            //TODO actorPlayer must pay the cost of reload
            selectedWeapon.setCharge(true);
        }
    }

    public boolean isValid(){
        int R = selectedWeapon.getRedCost();
        int B = selectedWeapon.getBlueCost();
        int Y = selectedWeapon.getYellowCost();
        //TODO if non ha abbastanza ammo --> return false;
        return true;
    }
}
