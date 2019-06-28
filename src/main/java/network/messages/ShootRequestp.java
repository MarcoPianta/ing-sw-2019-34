package network.messages;

import java.util.List;

/**
 * This message is sended by GameLobby to the Client when a Shoot Action contain a 'p' element in its actionSequence
 * with this message send the List of Token related to the player that should be selected as target
 */
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
