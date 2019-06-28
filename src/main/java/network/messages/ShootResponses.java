package network.messages;

import java.util.List;

/**
 * This message is sended by Client to Server as a response of ShootRequests
 * with this message send the List of Token player selected as target
 */
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
