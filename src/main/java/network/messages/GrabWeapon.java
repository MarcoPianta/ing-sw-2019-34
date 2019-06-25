package network.messages;

public class GrabWeapon extends Message {

    private int positionWeapon;

    public GrabWeapon(Integer token, int positionWeapon){
        this.token = token;
        actionType=ActionType.GRABWEAPON;
        this.positionWeapon=positionWeapon;
    }

    public int getPositionWeapon() {
        return positionWeapon;
    }

}
