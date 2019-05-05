package network.Server;

import java.net.Socket;
import java.util.ArrayList;

/**
 * This class is used to create and handle a queue for client which request to enter in a new game.
 * Server will get from this class clients waiting in the queue to start a new game.
 * */
public class Queue {
    private ArrayList<Socket> playersID;
    private int playersNumber;

    public Queue(){
        playersID = new ArrayList<>();
        playersNumber = 0;
    }

    /**
     * This method adds a new client to the queue and update the current clients number waiting in the queue
     * */
    public void addPlayer(Socket player){
        playersID.add(player);
        playersNumber = playersID.size();
    }

    /**
     * This method is used to retrieve the current number of client that are waiting in the queue
     * */
    public int getPlayersNumber() {
        return playersNumber;
    }

    /**
     * This method returns the client that is waiting from more time in the queue (which is the first of the arrayList),
     * and update the current number of clients waiting in the queue.
     * If queue is empty null is returned.
     * */
    public Socket getPlayer(){
        Socket ret;
        if (!playersID.isEmpty()) {
            ret = playersID.get(0);
            playersID.remove(0);
            playersNumber = playersID.size();
        }
        else
            ret = null;
        return ret;
    }
}
