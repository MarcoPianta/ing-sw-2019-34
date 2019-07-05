package network.messages;

/**
 * This message tell the client final frenzy has started
 */
public class FinalTurnMessage extends Message {
    boolean firstPlayer;

    public FinalTurnMessage(Integer token, boolean firstPlayer){
        this.token = token;
        this.actionType = ActionType.FINALTURN;
        this.firstPlayer = firstPlayer;
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }
}
