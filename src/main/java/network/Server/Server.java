package network.Server;

import network.Server.RMI.RMIServer;
import network.Server.Socket.SocketServer;
import network.messages.Message;

import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    private Queue playersQueue;
    private SocketServer socketServer;
    private ArrayList<Integer> tokens;
    private HashMap<Integer, Boolean> clients;
    private RMIServer rmiServer;

    public Server(){
        playersQueue = new Queue();
        socketServer = new SocketServer(this, 10000);
        tokens = new ArrayList<>();
        clients = new HashMap<>();
    }

    public void addToQueue(Socket client){
        playersQueue.addPlayer(client);
    }

    public void onReceive(Message message){

    }

    /**
     * This method is empty because is used for inheritance
     * */
    public void send(Message message, Integer token){
        if (clients.get(token)) {
            //TODO rmiServer.send(message)
        }
        else
            socketServer.send(message);
    }

    public Integer generateToken(boolean rmi){
        Integer integer;
        do{
            integer = new SecureRandom().nextInt(1147483647) + 1000000000;
        }while (tokens.contains(integer));
        tokens.add(integer);
        clients.put(integer, rmi);
        return integer;
    }
}
