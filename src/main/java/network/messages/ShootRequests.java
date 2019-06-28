package network.messages;

import Model.NormalSquare;
import Model.Player;

import java.util.List;

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
