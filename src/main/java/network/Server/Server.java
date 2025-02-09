package network.Server;

import network.Server.RMI.RMIServer;
import network.Server.Socket.SocketServer;
import network.messages.*;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * This class represents the server which handle connection with the clients.
 * It contains the SocketServer and the RMIServer, when a message is received those server forward the message to this
 * main server which handle it; if a message need to be sent to a client it use the proper type of server to send it.
 * The server contains an hash map which use the unique token of the client as key and a Boolean type that indicates if
 * the client is an RMI client or not.
 */
public class Server {
    private QueueChunk playersQueue;
    private SocketServer socketServer;
    private ArrayList<Integer> tokens;
    private HashMap<Integer, Boolean> clients;
    private RMIServer rmiServer;
    private HashMap<Integer, GameLobby> lobbyHashMap;
    private static Logger logger = Logger.getLogger("Server");


    /**
     * The constructor creates a new queue which contains the client that are waiting for a new Game to start, it also
     * creates two new server, one is for socket and the other for RMI connection and start them.
     * It creates an ArrayList to contain the tokens generated for each client and an hash map used to distinguish RMI
     * and socket clients.
     */
    public Server(String ip){
        playersQueue = new QueueChunk(this);
        socketServer = new SocketServer(this, 10000);
        rmiServer = new RMIServer(this, 10001, ip);
        tokens = new ArrayList<>();
        clients = new HashMap<>();
        lobbyHashMap = new HashMap<>();
        startServers();
    }

    /**
     * This method is used to start both Socket and RMI server
     */
    private void startServers(){
        try {
            socketServer.run();
        }catch (IOException e){
            logger.severe("Socket server not started" + e.getMessage());
        }
        try {
            rmiServer.run();
        }catch (IOException e){
            logger.severe("RMI server not started" + e.getMessage());
        }
    }

    /**
     * This method add a new client, that connect to the server, to the queue of clients waiting for a new Game.
     * @param client the token of the client that had to be added to the queue
     */
    public void addToQueue(Integer client){
        playersQueue.addPlayer(client);
    }

    public void onReceive(Message message){
        System.out.println("Ho RICEVUTO un messaggio di " + message.getActionType() + " da " + clients.get(message.getToken()));
        try {
            if(message.getActionType().getAbbreviation().equals(ActionType.GAMESETTINGSRESPONSE.getAbbreviation())) {
                GameSettingsResponse m = (GameSettingsResponse) message;
                this.send(new UpdateClient(message.getToken(), "Waiting for others players to connect"));
                playersQueue.setPreferences(m);
            }
            else if(message.getActionType().getAbbreviation().equals(ActionType.RECONNECTIONRESPONSE.getAbbreviation())) {

            }
            else if (message.getActionType().getAbbreviation().equals(ActionType.DISCONNECT.getAbbreviation())) {
                //TODO delete token from clients
            }
            else {
                lobbyHashMap.get(message.getToken()).receiveMessage(message);
            }
        }catch (Exception e){
            e.printStackTrace();//TODO logger
        }
    }

    /**
     * This method is used to send a message to a client.
     * The method, using the hash map, recognize if the client is a Socket or RMI client and use the proper server to
     * forward the message that must be sent.
     * @param message the message that must be sent
     * */
    public void send(Message message){
        if (clients.get(message.getToken())) {
            rmiServer.send(message);
        }
        else
            socketServer.send(message);
    }

    /**
     * This method generates a new unique token, it extracts a random number and check if it has already been extracted,
     * if not the token is added to the hash map and is returned.
     * The method also send a GameSettingsRequest message to the client.
     * @param rmi a boolean that indicates if the client is an RMI client or not
     * @return the generated token to be used from servers to identify the clients
     */
    public Integer generateToken(Integer token, boolean rmi){
        if (token == 0) {
            return createToken(rmi);
        }
        else {
            if (clients.containsKey(token)){
                if (lobbyHashMap.containsKey(token)) {
                    return token;
                }
                else
                    return createToken(rmi);
            }
            else
                return createToken(rmi);
        }
    }

    private Integer createToken(boolean rmi){
        Integer integer;
        do {
            integer = new SecureRandom().nextInt(1147483647) + 1000000000;
        } while (tokens.contains(integer));
        tokens.add(integer);
        clients.put(integer, rmi);
        addToQueue(integer);
        return integer;
    }

    /**
     * This method remove the client form the hash map, it must be called when a client disconnects from one of the
     * servers.
     * @param token the token of the client that must be removed
     */
    public void removeClient(Integer token){
        if (lobbyHashMap.containsKey(token))
            lobbyHashMap.get(token).remove(token);
        else
            clients.remove(token);
    }

    void notifyFromQueue(ArrayList<Integer> players, int skullNumber, int map){
        GameLobby gameLobby = new GameLobby(players, skullNumber, "map"+map, this);
        for (Integer i: players){
            lobbyHashMap.put(i, gameLobby);
        }

    }

    public static void main(String[] args) {
        new Server(args[0]);
    }
}
