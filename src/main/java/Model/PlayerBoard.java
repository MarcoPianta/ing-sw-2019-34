package Model;

import java.util.ArrayList;

public class PlayerBoard {

    private Colors color;
    private String playerName;
    private int maxReward;// indicates the maximum reward obtainable
    private int points;
    private Health healthPlayer;
    private Hand handPlayer;
    private Player player;

    public PlayerBoard(Colors color,String playerName,Player player){
        this.color=color;
        this.playerName = playerName;
        this.maxReward=8;
        this.points = 0;
        this.healthPlayer= new Health(this);
        this.handPlayer=new Hand(this);
        this.player=player;
    }

    public Colors getColor() {
        return color;
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

    public String getPlayerName() {
        return playerName;
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