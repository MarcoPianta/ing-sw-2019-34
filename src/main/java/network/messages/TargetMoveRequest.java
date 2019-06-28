package network.messages;

import Model.Player;

import java.util.List;

public class TargetMoveRequest extends Message{

    private List<String> targetableSquare;

    public TargetMoveRequest(Integer token, List<String> targetableSquare){
        this.token = token;
        this.actionType = ActionType.TARGETMOVEREQUEST;
        this.targetableSquare = targetableSquare;
    }

    public List<String> getTargetableSquare() {
        return targetableSquare;
    }

}
