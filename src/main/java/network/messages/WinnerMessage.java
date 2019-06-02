package network.messages;

public class WinnerMessage extends Message{
    private boolean winner;

    public WinnerMessage(Integer token, boolean winner){
        this.token = token;
        this.actionType = ActionType.WINNER;
        this.winner = winner;
    }

    public boolean isWinner() {
        return winner;
    }
}
