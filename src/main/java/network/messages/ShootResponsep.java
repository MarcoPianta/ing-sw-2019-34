package network.messages;


import java.util.List;

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
