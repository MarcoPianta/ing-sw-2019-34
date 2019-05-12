package network.Server.Socket;

import network.Server.Server;
import network.messages.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class represent the socket server; it is used to send and receive message from the client using socket
 * connection.
 * The default port on which the server socket is created is 10000, although is possible to change the default port.
 * */
public class SocketServer {
    private ServerSocket serverSocket;
    private final int PORT;
    private Server server; //This variable is used to contain the main server
    private ArrayList<Socket> clients;

    /**
     * This constructor creates a new server socket listening on the default port 10000
     * @param server the main server
     * */
    public SocketServer(Server server) {
        this.server = server;
        this.PORT = 10000;
        clients = new ArrayList<>();
    }

    /**
     * This constructor creates a new server socket listening on the port indicated as parameter
     * @param server the main server
     * @param port the port on which the server will listen for new connection
     * */
    public SocketServer(Server server, int port) {
        this.server = server;
        this.PORT = port;
        clients = new ArrayList<>();
    }

    /**
     * This method initialize the server creating a new ServerSocket ready to listen for new connection on the
     * indicated port.
     * */
    //TODO catch exception
    private void init() throws IOException{
        serverSocket = new ServerSocket(PORT);
    }

    /**
     * This method accept a new connection from a client.
     * When a new connection is accepted a new Thread running SocketClientHandler's method run is started, so that more
     * than one client will be able to communicate with the server.
     * */
    //TODO catch exception
    public Socket acceptConnection() throws IOException{
        Socket socket = serverSocket.accept();

        new Thread(() -> {
            new SocketClientHandler(socket, this);
        }).start();
        clients.add(socket);
        return socket;
    }

    /**
     * This method forward the message received from the client directly to the main server
     * @param message the message to be forwarded to the server
     * */
    public void onReceive(Message message){
        server.onReceive(message);
    }

    /**
     * This is the main method of the server.
     * This method is called when the server need to be started.
     * */
    public void run() throws IOException{
            init();
            acceptConnection();
    }
}
