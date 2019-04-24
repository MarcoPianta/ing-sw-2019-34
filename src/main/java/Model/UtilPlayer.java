package Model;

import java.util.ArrayList;

public class UtilPlayer {
    private Player player;
    private int counter;

    public  UtilPlayer(Player player, int counter){
        this.player=player;
        this.counter=counter;
    }
    public UtilPlayer(){}
    public int getCounter() {
        return counter;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }



}
