package network.messages;

import Model.Player;

import java.util.List;

public class ShootRequestp extends Message{

    private Integer targetNumber;
    private List<Integer> targetablePlayer;

    public ShootRequestp(Integer token, Integer targetNumber, List<Integer> targetablePlayer){
        this.token = token;
        this.actionType = ActionType.SHOOTREQUESTP;
        this.targetNumber = targetNumber;
        this.targetablePlayer = targetablePlayer;
    }

    public Integer getTargetNumber() {
        return targetNumber;
    }

    public List<Integer> getTargetalePlayer() {
        return targetablePlayer;
    }

}
