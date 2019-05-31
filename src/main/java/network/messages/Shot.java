package network.messages;

import Model.*;

import java.util.ArrayList;
import java.util.List;

public class Shot extends Message {
    private List<Player> targets;
    private Effect effect;
    private int maxMove;
    private NormalSquare newSquare;
    private NormalSquare square;
    private Room room;
    private CardWeapon weapon;
    private  boolean reload;
    private CardPowerUp powerUp;
    private Player targetPowerUp;

    public Shot(List<Player> targets, Effect effect, int maxMove, NormalSquare newSquare, CardWeapon weapon,boolean reload,CardPowerUp powerUp,Player targetPowerUp){
        actionType=ActionType.SHOT;
        this.effect =effect;
        this.room=null;
        this.square=null;
        this.targets =targets;
        this.maxMove=maxMove;
        this.newSquare=newSquare;
        this.weapon=weapon;
        this.reload=reload;
        this.powerUp=powerUp;
        this.targetPowerUp=targetPowerUp;
    }
    public Shot(Effect effect,Room room, int maxMove,NormalSquare newSquare,CardWeapon weapon,boolean reload,CardPowerUp powerUp,Player targetPowerUp){
        actionType=ActionType.SHOT;
        this.effect =effect;
        this.room=room;
        this.square=null;
        this.targets =null;
        this.maxMove=maxMove;
        this.newSquare=newSquare;
        this.weapon=weapon;
        this.reload=reload;
        this.powerUp=powerUp;
        this.targetPowerUp=targetPowerUp;
    }
    public Shot(Effect effect,NormalSquare square, int maxMove,NormalSquare newSquare,CardWeapon weapon,boolean reload,CardPowerUp powerUp,Player targetPowerUp){
        actionType=ActionType.SHOT;
        this.effect =effect;
        this.room=null;
        this.square=newSquare;
        this.targets =null;
        this.maxMove=maxMove;
        this.newSquare=square;
        this.weapon=weapon;
        this.reload=reload;
        this.powerUp=powerUp;
        this.targetPowerUp=targetPowerUp;
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

    public CardPowerUp getPowerUp() {
        return powerUp;
    }

    public boolean isReload() {
        return reload;
    }

    public CardWeapon getWeapon() {
        return weapon;
    }

    public List<Player> getTargets() {
        return targets;
    }

    public Effect getEffect() {
        return effect;
    }

    public int getMaxMove() {
        return maxMove;
    }

    public NormalSquare getNewSquare() {
        return newSquare;
    }
}

