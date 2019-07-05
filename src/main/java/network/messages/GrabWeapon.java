package network.messages;

/**
 * This message is used to send to the server the weapon he want to grab
 */
public class GrabWeapon extends Message {

    private int positionWeapon;

    /**
     * @param positionWeapon position of the weapon in arraylist
     */
    public GrabWeapon(Integer token, int positionWeapon){
        this.token = token;
        actionType=ActionType.GRABWEAPON;
        this.positionWeapon=positionWeapon;
    }

    public int getPositionWeapon() {
        return positionWeapon;
    }

}
