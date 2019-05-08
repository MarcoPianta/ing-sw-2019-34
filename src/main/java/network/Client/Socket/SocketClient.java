package network.Client.Socket;

import network.Server.Socket.SocketServer;
import network.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class is used from the client to communicate with SocketServer when a Socket connection is used.
 * */
public class SocketClient {
    private String host;
    private final int port;
    private Socket connection;
    private SocketServer server;
    private ObjectInputStream in; //Used to send message to server
    private ObjectOutputStream out; ////Used to receive message from server

    /**
     * The constructor only initialize the host and port attribute, it doesn't establish connection. Attributes will be
     * used from init() to establish a new connection to the server
     * */
    public SocketClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    /**
     * This method initialize a new socket connection and set the output and input stream used to send and receive
     * message from the server.
     * */
    public void init() throws IOException {
        connection = new Socket(host, port);
        out = new ObjectOutputStream(connection.getOutputStream());
        in = new ObjectInputStream(connection.getInputStream());
    }

    /**
     * This method send a message to the server
     * @param message is the message that must be sent to the server
     * */
    //TODO handle exception, maybe with a logger
    public void send(Message message) throws IOException{
        out.writeObject(message);
    }

    //TODO receive response message

    /**
     * This method is used to close connection between the client and the server. Before closing connection input and
     * output streams are closed.
     * */
    public void close() throws IOException {
        in.close();
        out.close();
        connection.close();
    }
}
