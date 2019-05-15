package network.Server;

import network.Server.RMI.RMIServer;
import network.Server.Socket.SocketServer;
import network.messages.Message;

import java.io.IOException;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents the server which handle connection with the clients.
 * It contains the SocketServer and the RMIServer, when a message is received those server forward the message to this
 * main server which handle it; if a message need to be sent to a client it use the proper type of server to send it.
 * The server contains an hash map which use the unique token of the client as key and a Boolean type that indicates if
 * the client is an RMI client or not.
 */
public class Server {
    private Queue playersQueue;
    private SocketServer socketServer;
    private ArrayList<Integer> tokens;
    private HashMap<Integer, Boolean> clients;
    private RMIServer rmiServer;

    /**
     * The constructor creates a new queue which contains the client that are waiting for a new Game to start, it also
     * creates two new server, one is for socket and the other for RMI connection and start them.
     * It creates an ArrayList to contain the tokens generated for each client and an hash map used to distinguish RMI
     * and socket clients.
     */
    public Server(){
        playersQueue = new Queue();
        socketServer = new SocketServer(this, 10000);
        rmiServer = new RMIServer(this, 10001);
        tokens = new ArrayList<>();
        clients = new HashMap<>();
        startServers();
    }

    /**
     * This method is used to start both Socket and RMI server
     */
    private void startServers(){
        try {
            socketServer.run();
        }catch (IOException e){
            //TODO log that Socket server has not been started
        }
        try {
            rmiServer.run();
        }catch (IOException e){
            //TODO log that RMI server has not been started
        }
    }

    /**
     * This method add a new client that connected to the server to the queue of clients waiting for a new Game.
     * @param client
     */
    public void addToQueue(Socket client){
        playersQueue.addPlayer(client);
    }

    public void onReceive(Message message){

    }

    /**
     * This method is used to send a message to a client.
     * The method, using the hash map, recognize if the client is a Socket or RMI client and use the proper server to
     * forward the message that must be sent.
     * @param message the message that must be sent
     * @param token the token of the client to which the message must be sent
     * */
    public void send(Message message, Integer token){
        if (clients.get(token)) {
            //TODO rmiServer.send(message)
        }
        else
            socketServer.send(message);
    }

    /**
     * This method generates a new unique token, it extracts a random number and check if it has already been extracted,
     * if not the token is added to the hash map and is returned
     * @param rmi a boolean that indicates if the client is an RMI client or not
     * @return the generated token to be used from servers to identify the clients
     */
    public Integer generateToken(boolean rmi){
        Integer integer;
        do{
            integer = new SecureRandom().nextInt(1147483647) + 1000000000;
        }while (tokens.contains(integer));
        tokens.add(integer);
        clients.put(integer, rmi);
        return integer;
    }

    /**
     * This method remove the client form the hash map, it must be called when a client disconnects from one of the
     * servers.
     * @param token the token of the client that must be removed
     */
    public void removeClient(Integer token){
        clients.remove(token);
    }
}
