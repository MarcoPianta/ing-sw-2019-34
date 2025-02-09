package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerBoard implements Serializable {
    private int maxReward;// indicates the maximum reward obtainable
    private int points;
    private Health healthPlayer;
    private Hand handPlayer;
    private Player player;
    private boolean finalTurn;//max reward=2;

    public PlayerBoard(Player player){
        this.maxReward=8;
        this.points = 0;
        this.healthPlayer= new Health(this);
        this.handPlayer=new Hand(this);
        this.player=player;
        finalTurn=false;
    }

    public boolean isFinalTurn() {
        return finalTurn;
    }

    public void setMaxReward(int maxReward) {
        this.maxReward = maxReward;
    }

    public void setFinalTurn(boolean finalTurn) {
        this.finalTurn = finalTurn;
    }

    public Player getPlayer() {
        return player;
    }

    public int getMaxReward() {
        return maxReward;
    }

    public int getPoints() {
        return points;
    }

    public Hand getHandPlayer() {
        return handPlayer;
    }

    public Health getHealthPlayer() {
        return healthPlayer;
    }

    /**
     * decrement maxReward after death
     * */
    public void decrementMaxReward(){
        if(getMaxReward()!=1)
            maxReward-=2;
    }

    /**
     * this method add point after a other player's death
     * */
    public void addPoints(int newPoints){
        points+= newPoints;
    }
}