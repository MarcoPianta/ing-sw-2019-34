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
    private final Object lock = new Object();

    public Queue(Server server){
        playersID = new LinkedHashMap<>();
        this.server = server;

        new Thread(() -> { //This thread check if the player in the queue are more then three
            while(true){
                //TODO start new Thread to check multiple times if there are enough players to start multiple Games
                if (playersID.size() >= 3) {
                    synchronized (this) {
                        try {
                            System.out.println("Wait");
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            //TODO logger
                        }
                        System.out.println("Booo");
                        ArrayList<Integer> players = getPlayers();
                        notifyServer(getPlayers());
                        System.out.println("Booo1");
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
    public ArrayList<Integer> getPlayers(){
        ArrayList<Integer> ret;
        synchronized (this) {
            ArrayList<Integer> newPlayers = new ArrayList<>(playersID.keySet());
            ret = newPlayers
                    .stream()
                    .filter(x -> newPlayers.indexOf(x) < 5)
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
