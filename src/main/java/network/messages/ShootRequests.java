package network.messages;

import java.util.List;

/**
 * This message is sended by GameLobby to the Client when a Shoot Action contain a 's' element in its actionSequence
 * with this message send the List of ID related to the square that should be selected as target
 */
public class ShootRequests extends Message{

    private Integer squareNumber;
    private List<String> targetableSquare;

    public ShootRequests(Integer token, Integer squareNumber, List<String> targetableSquare){
        this.token = token;
        this.actionType = ActionType.SHOOTREQUESTS;
        this.squareNumber = squareNumber;
        this.targetableSquare = targetableSquare;
    }

    public Integer getSquareNumber() {
        return squareNumber;
    }

    public List<String> getTargetableSquare() {
        return targetableSquare;
    }

}
