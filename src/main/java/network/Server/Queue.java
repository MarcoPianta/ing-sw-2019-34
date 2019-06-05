package network.Server;

import network.messages.GameSettingsResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * This class is used to create and handle a queue for client which request to enter in a new game.
 * Server will get from this class clients waiting in the queue to start a new game.
 * */
public class Queue {
    private LinkedHashMap<Integer, GameSettingsResponse> playersID; //Contains the players' token and the game settings he choose ordered3 by insertion
    private Server server;
    private final Object lockIndex = new Object();
    private Integer index;
    private ArrayList<Integer[]> chuncks;

    public Queue(Server server){
        playersID = new LinkedHashMap<>();
        this.server = server;
        this.index = 1;
        startCheckThread();
    }

    /**
     * This method starts a Thread to check if there are enough player (at least 3) to starts a new Game.
     * If there are more then three player the Queue wait for some times (a timer is started) in case other player
     * connects, when timer times out a new Game with a maximum of 5 player is created.
     * Meanwhile a new Thread check if there are more then 5 people to start a new Thread with the same functionality as
     * the first, check if there are enough player to start a new Game.
     * TODO possible improvement use chucnk of 5 people to start a new Game with the chunk
     */
    private void startCheckThread(){
        new Thread(() -> { //This thread check if the player in the queue are more then three
            while(true){
                //TODO start new Thread to check multiple times if there are enough players to start multiple Games
                if (playersID.size() >= 3) {
                    checkIfMoreThenFive();

                    checkAndCreate();
                }
            }
        }).start();
    }

    private void checkAndCreate(){
        synchronized (this) {
            try {
                this.wait(300);
            } catch (InterruptedException e) {
                //TODO logger
            }
            notifyServer(getPlayers(index));
            synchronized (lockIndex) {
                if (index > 1) {
                    index--;
                }
            }
        }
    }

    private void checkIfMoreThenFive(){
        new Thread(() -> {//This thread starts to check if there are more then 5 people waiting to crate a new Game
            while (true) {
                if (playersID.size() > 5 * index) {
                    synchronized (lockIndex) {
                        index++;
                    }
                    if (playersID.size() >= 5 * (index - 1) + 3) {
                        checkAndCreate();
                    }
                }
            }
        }).start();
    }

    /**
     * This method adds a new client to the queue. The client is inserted in a LinkedHashMap so the insertion order is
     * guaranteed, this made possible to get the clients which are waiting from more time first.
     * @param player the token of the client that need to be added to the queue
     * */
    public void addPlayer(Integer player){
        synchronized (this) {
            playersID.put(player, null);
        }
    }

    /**
     * This method returns the first five clients that are waiting from more time in the queue, if there are less then
     * five clients it returns all the clients in the queue.
     * @return the first five players inserted in the LinkedHashMap
     * */
    public ArrayList<Integer> getPlayers(int index){
        ArrayList<Integer> ret;
        synchronized (this) {
            ArrayList<Integer> newPlayers = new ArrayList<>(playersID.keySet());
            ret = newPlayers
                    .stream()
                    .filter(x -> newPlayers.indexOf(x) >= (5 * (index - 1)) && newPlayers.indexOf(x) < (5 * index))
                    .collect(Collectors.toCollection(ArrayList::new));
            notifyServer(ret);
            playersID.keySet().removeAll(ret);
        }
        return ret;
    }

    public void setPreferences(GameSettingsResponse message){
        playersID.replace(message.getToken(), message);
    }

    public int getQueueSize(){
        return playersID.size();
    }

    /**
     * This method notify the server when there are enough players to start a new Game.
     * It forward to the server the players which will play and the first game settings available of the players.
     * If none of the player has available game settings those are randomly chosen.
     * @param players the ArrayList of the players that will take part of the new Game
     */
    private void notifyServer(ArrayList<Integer> players){
        int skullNumber = 0;
        int map = 0;
        for(Integer i: players) {
            if (playersID.get(i) != null) {
                skullNumber = playersID.get(i).getSkullNumber();
                map = playersID.get(i).getMapNumber();
                break;
            }
        }
        if (skullNumber == 0 || map == 0){ //If none of the player choose his game preference those are randomly extracted
            skullNumber = (Arrays.asList(5, 8).get(new Random().nextInt(1))); //Extract skull number (can be 5 or 8)
            map = (Arrays.asList(1, 2, 3, 4).get(new Random().nextInt(4))); //Extract map number (can be 1, 2, 3 or 4)
        }
        server.notifyFromQueue(players,skullNumber, map);
    }
}
