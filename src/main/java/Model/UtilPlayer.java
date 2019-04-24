package Model;

import java.util.ArrayList;

public class UtilPlayer {
    private Player player;
    private Colors color;
    private int counter;

    public  UtilPlayer(Player player, Colors color, int counter){
        this.player=player;
        this.color=color;
        this.counter=counter;
    }
    public UtilPlayer(){}

    public Colors getColor() {
        return color;
    }
    public int getCounter() {
        return counter;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }



}
