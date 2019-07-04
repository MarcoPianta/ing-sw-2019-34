package network.messages;

import Model.*;

import java.util.ArrayList;
import java.util.List;

public class Shot extends Message {
    private List<Player> targets;
    private Integer posEffect;
    private ArrayList<NormalSquare> squares;
    private Room room;
    private int weapon;
    private Integer powerUp;
    private Player targetPowerUp;

    public Shot(List<Player> targets, Integer posEffect, int weapon, Integer powerUp, Player targetPowerUp){
        actionType=ActionType.SHOT;
        this.posEffect =posEffect;
        this.room=null;
        this.squares=null;
        this.targets =targets;
        this.weapon=weapon;
        this.powerUp=powerUp;
        this.targetPowerUp=targetPowerUp;
    }
    public Shot(ArrayList<NormalSquare> square, Integer posEffect, int weapon, Integer powerUp, Player targetPowerUp){
        actionType=ActionType.SHOT;
        this.posEffect =posEffect;
        this.room=null;
        this.squares=squares;
        this.targets =null;
        this.weapon=weapon;
        this.powerUp=powerUp;
        this.targetPowerUp=targetPowerUp;
    }
    public Shot(Room room, Integer posEffect, int weapon, Integer powerUp, Player targetPowerUp){
        actionType=ActionType.SHOT;
        this.posEffect =posEffect;
        this.room=room;
        this.squares=null;
        this.targets =null;
        this.weapon=weapon;
        this.powerUp=powerUp;
        this.targetPowerUp=targetPowerUp;
    }

    public Shot(Integer token, List<Player> targets, Integer posEffect, int weapon){
        this.token = token;
        actionType=ActionType.SHOT;
        this.posEffect =posEffect;
        this.room=null;
        this.squares=null;
        this.targets =targets;
        this.powerUp=-1;
        this.weapon=weapon;

    }
    public Shot(Integer token, ArrayList<NormalSquare> square, Integer posEffect, int weapon){
        this.token = token;
        actionType=ActionType.SHOT;
        this.posEffect =posEffect;
        this.room=null;
        this.squares=square;
        this.targets =null;
        this.powerUp=-1;
        this.weapon=weapon;
    }
    public Shot(Integer token, Room room, Integer posposEffect, int weapon){
        this.token = token;
        actionType=ActionType.SHOT;
        this.posEffect =posEffect;
        this.room=room;
        this.squares=null;
        this.targets =null;
        this.powerUp=-1;
        this.weapon=weapon;

    }

    public List<NormalSquare> getSquare() {
        return squares;
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

    public Integer getPosEffect() {
        return posEffect;
    }

    public void setPowerUp(Integer powerUp) {
        this.powerUp = powerUp;
    }
}

