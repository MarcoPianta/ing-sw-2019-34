package network.messages;

import Model.Effect;

public class ReceiveTargetSquare extends Message {
    private String type; //"grab","move","shoot"
    private Integer posEffect;
    private Integer posWeapon;

    public ReceiveTargetSquare(Integer token,String string, Integer posWeapon, Integer posEffect){
        actionType=ActionType.RECEIVETARGETSQUARE;
        this.token=token;
        this.type=string;
        this.posEffect=posEffect;
        this.posWeapon=posWeapon;
    }
    public ReceiveTargetSquare(Integer token,String string){
        actionType=ActionType.RECEIVETARGETSQUARE;
        this.token=token;
        this.type=string;
        this.posEffect=null;
        this.posWeapon=null;

    }

    public String getType() {
        return type;
    }

    public Integer getPosEffect() {
        return posEffect;
    }

    public Integer getPosWeapon() {
        return posWeapon;
    }
}
