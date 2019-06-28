package network.messages;

import java.util.List;

public class ShootResponses extends Message{

    private List<String> targetSquare;

    public ShootResponses(Integer token, List<String> targetSquare){
        this.token = token;
        this.actionType = ActionType.SHOOTRESPONSES;
        this.targetSquare = targetSquare;
    }


    public List<String> getTargetSquare() {
        return targetSquare;
    }

}
