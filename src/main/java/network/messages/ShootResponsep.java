package network.messages;

import java.util.List;

/**
 * This message is sended by Client to Server as a response of ShootRequestp
 * with this message send the List of Token player selected as target
 */
public class ShootResponsep extends Message{

    private List<Integer> targetPlayer;

    public ShootResponsep(Integer token, List<Integer> targetPlayer){
        this.token = token;
        this.actionType = ActionType.SHOOTRESPONSEP;
        this.targetPlayer = targetPlayer;
    }


    public List<Integer> getTargetPlayer() {
        return targetPlayer;
    }

}
