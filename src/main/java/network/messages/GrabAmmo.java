package network.messages;

/**
 * This message is used to send a grab ammo request
 */
public class GrabAmmo extends Message{

    public GrabAmmo(Integer token) {
        this.token = token;
        actionType = ActionType.GRABAMMO;
    }
}
