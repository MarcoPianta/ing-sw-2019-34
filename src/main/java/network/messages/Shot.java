package network.messages;

import Model.*;

import java.util.List;

public class Shot extends Message {
    private List<Player> targets;
    private Effect effect;
    private NormalSquare square;
    private Room room;
    private int weapon;
    private Integer powerUp;
    private Player targetPowerUp;

    public Shot(List<Player> targets, Effect effect,int weapon,Integer powerUp,Player targetPowerUp){
        actionType=ActionType.SHOT;
        this.effect =effect;
        this.room=null;
        this.square=null;
        this.targets =targets;
        this.weapon=weapon;
        this.powerUp=powerUp;
        this.targetPowerUp=targetPowerUp;
    }
    public Shot(Room room,Effect effect,int weapon,Integer powerUp,Player targetPowerUp){
        actionType=ActionType.SHOT;
        this.effect =effect;
        this.room=room;
        this.square=null;
        this.targets =null;
        this.weapon=weapon;
        this.powerUp=powerUp;
        this.targetPowerUp=targetPowerUp;
    }
    public Shot(NormalSquare square,Effect effect,int weapon,Integer powerUp,Player targetPowerUp){
        actionType=ActionType.SHOT;
        this.effect =effect;
        this.room=null;
        this.square=square;
        this.targets =null;
        this.weapon=weapon;
        this.powerUp=powerUp;
        this.targetPowerUp=targetPowerUp;
    }

    public Shot(List<Player> targets, Effect effect,int weapon){
        actionType=ActionType.SHOT;
        this.effect =effect;
        this.room=null;
        this.square=null;
        this.targets =targets;
        this.powerUp=-1;
        this.weapon=weapon;

    }
    public Shot(Room room,Effect effect,int weapon){
        actionType=ActionType.SHOT;
        this.effect =effect;
        this.room=room;
        this.square=null;
        this.targets =null;
        this.powerUp=-1;
        this.weapon=weapon;

    }
    public Shot(NormalSquare square,Effect effect,int weapon){
        actionType=ActionType.SHOT;
        this.effect =effect;
        this.room=null;
        this.square=square;
        this.targets =null;
        this.powerUp=-1;
        this.weapon=weapon;
    }

    public NormalSquare getSquare() {
        return square;
    }

    public Room getRoom() {
        return room;
    }

    public Player getTargetPowerUp() {
        return targetPowerUp;
    }

    public Integer getPowerUp() {
        return powerUp;
    }

    public int getWeapon() {
        return weapon;
    }

    public List<Player> getTargets() {
        return targets;
    }

    public Effect getEffect() {
        return effect;
    }

}

