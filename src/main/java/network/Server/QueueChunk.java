package network.Server;

import network.messages.GameSettingsResponse;

import java.util.*;

/**
 * This class is used to create and handle a queue for client which request to enter in a new game.
 * Server will get from this class clients waiting in the queue to start a new game.
 * */
//TODO check for disconnection
public class QueueChunk {
    private HashMap<Integer, GameSettingsResponse> playersID; //Contains the players' token and the game settings he choose ordered3 by insertion
    private Server server;
    private ArrayList<ArrayList<Integer>> chunks;
    private int maxChunkIndex;
    private HashMap<Integer, Boolean> startedCheck; //This variable is used to know if a Thread to create a new Game is yet started
    public static final int QUEUEWAITTIME = 30000; //Time to wait when the player number is reached in millis

    public QueueChunk(Server server){
        playersID = new LinkedHashMap<>();
        this.server = server;
        chunks = new ArrayList<>();
        chunks.add(new ArrayList<>());
        maxChunkIndex = 0;
        startedCheck = new HashMap<>();
        startedCheck.put(maxChunkIndex, false);
    }

    /**
     * This method add the player to the Queue, the player is added in the current chunk if there are less then five
     * player in it, otherwise a new chuck is created.
     *
     * @param player the player's token to be added
     */
    public void addPlayer(Integer player){
        Timer timer = new Timer();
        synchronized (this) {
            if (chunks.get(maxChunkIndex).size() < 5) {
                chunks.get(maxChunkIndex).add(player);
                if (chunks.get(maxChunkIndex).size() >= 3 && !startedCheck.get(maxChunkIndex)) {
                    startedCheck.put(maxChunkIndex, true);

                    timer.schedule((new TimerTask() { //Start a timer and Wait for 30 seconds for other players to connect, then create new Game
                                @Override
                                public void run() {
                                    notifyServer(chunks.get(maxChunkIndex));
                                }
                            })
                            , QUEUEWAITTIME);
                }
            } else {
                maxChunkIndex++;
                chunks.add(new ArrayList<>());
                chunks.get(maxChunkIndex).add(player);
                startedCheck.put(maxChunkIndex, false);
            }
        }

        synchronized (this) {
            playersID.put(player, null);
        }
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
        synchronized (this) {
            for (Integer i : players) {
                if (playersID.get(i) != null) {
                    skullNumber = playersID.get(i).getSkullNumber();
                    map = playersID.get(i).getMapNumber();
                    break;
                }
            }
        }
        if (skullNumber == 0 || map == 0){ //If none of the player choose his game preference those are randomly extracted
            skullNumber = (Arrays.asList(5, 8).get(new Random().nextInt(1))); //Extract skull number (can be 5 or 8)
            map = (Arrays.asList(1, 2, 3, 4).get(new Random().nextInt(3))); //Extract map number (can be 1, 2, 3 or 4)
        }
        server.notifyFromQueue(players,skullNumber, map);
        synchronized (this){
            chunks.remove(players);
            for (Integer i: players)
                playersID.remove(i);
            if (maxChunkIndex > 0)
                maxChunkIndex--;
            else
                chunks.add(new ArrayList<>());
        }
    }

    public void setPreferences(GameSettingsResponse message){
        playersID.replace(message.getToken(), message);
    }

    public int getQueueSize(){
        return playersID.size();
    }
}
