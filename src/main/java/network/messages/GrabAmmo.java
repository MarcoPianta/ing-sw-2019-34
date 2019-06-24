package network.messages;

public class GrabAmmo extends Message{

    public GrabAmmo(Integer token) {
        this.token = token;
        actionType = ActionType.GRABAMMO;
    }
}
