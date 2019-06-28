package network.messages;

import java.util.List;

/**
 * This message is sended by GameLobby to the Client when a Shoot Action contain a 'm' element in its actionSequence
 * with this message send the List of ID related to the square in which the target should be moved
 */
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
