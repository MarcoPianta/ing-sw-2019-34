package network.Server.Socket;

import network.Server.Server;
import network.messages.ActionType;
import network.messages.ConnectionResponse;
import network.messages.GameSettingsRequest;
import network.messages.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * This class represent the socket server; it is used to send and receive message from the client using socket
 * connection.
 * The default port on which the server socket is created is 10000, although is possible to change the default port.
 * */
public class SocketServer {
    private ServerSocket serverSocket;
    private int port;
    private Server server; //This variable is used to contain the main server
    private HashMap<Integer, SocketClientHandler> threadHashMap;

    /**
     * This constructor creates a new server socket listening on the default port 10000
     * @param server the main server
     * */
    public SocketServer(Server server) {
        this.server = server;
        this.port = 10000;
        threadHashMap = new HashMap<>();
    }

    /**
     * This constructor creates a new server socket listening on the port indicated as parameter
     * @param server the main server
     * @param port the port on which the server will listen for new connection
     * */
    public SocketServer(Server server, int port) {
        this.server = server;
        this.port = port;
        threadHashMap = new HashMap<>();
    }

    /**
     * This method initialize the server creating a new ServerSocket ready to listen for new connection on the
     * indicated port.
     * */
    //TODO catch exception
    private void init() throws IOException{
        serverSocket = new ServerSocket(port);
    }

    /**
     * This method accept a new connection from a client.
     * When a new connection is accepted a new Thread running SocketClientHandler's method run is started, so that more
     * than one client will be able to communicate with the server.
     * To every new client is assigned a unique token which is sent to the client using a ConnectionResponse message.
     * Every connected client is also added to an hash map using the token as key
     * */
    //TODO catch exception
    public void acceptConnection() throws IOException{
        while (threadHashMap.size() < 100) {
            Socket socket = serverSocket.accept();

            int token = server.generateToken(0,false);
            SocketClientHandler clientHandler = new SocketClientHandler(socket, token, this);

            Thread thread = new Thread(clientHandler);
            thread.start();

            threadHashMap.put(token, clientHandler);
            clientHandler.respond(new ConnectionResponse(token));
            send(new GameSettingsRequest(token));
        }
    }

    /**
     * This method forward the message received from the client directly to the main server
     * @param message the message to be forwarded to the server
     * */
    public void onReceive(Message message){
        if (message.getActionType().getAbbreviation().equals(ActionType.DISCONNECT.getAbbreviation()))
            removeClient(message.getToken());
        server.onReceive(message);
    }

    public void send(Message message){
        threadHashMap.get(message.getToken()).respond(message);
    }

    /**
     * This is the main method of the server.
     * This method is called when the server need to be started.
     * */
    public void run() throws IOException{
            init();
            new Thread(() -> {
                try {
                    acceptConnection();
                }catch (IOException e){
                    //TODO log exception
                }
            }).start();
    }

    /**
     * When a socket connection is closed the client is remove from the hash map containing the Thread of every
     * connected client. The client is also removed from the main server
     * @param token the identifier of the client to be removed.
     */
    public void removeClient(Integer token){
        server.removeClient(token);
        threadHashMap.remove(token);
    }
}
