package network.Server;

import java.util.ArrayList;

public class Queue {
    private ArrayList<String> playersID;
    private int playersNumber;

    public Queue(){
        playersID = new ArrayList<>();
        playersNumber = 0;
    }

    public void addPlayer(String player){
        playersID.add(player);
        playersNumber = playersID.size();
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public String getPlayer(){
        String ret;
        if (!playersID.isEmpty()) {
            ret = playersID.get(0);
            playersID.remove(0);
            playersNumber = playersID.size();
        }
        else
            ret = "";
        return ret;
    }
}
