package network.messages;

import Model.*;

import java.util.ArrayList;
import java.util.List;

public class Shot extends Message {
    private ArrayList<Player> targets;
    private Effect effect;
    private int maxMove;
    private NormalSquare newSquare;
    private CardWeapon weapon;
    private  boolean reload;

    public Shot(ArrayList<Player> targets, Effect effect, int maxMove, NormalSquare newSquare, CardWeapon weapon,boolean reload){
        actionType=ActionType.SHOT;
        this.effect =effect;
        this.targets =targets;
        this.maxMove=maxMove;
        this.newSquare=newSquare;
        this.weapon=weapon;
        this.reload=reload;
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

