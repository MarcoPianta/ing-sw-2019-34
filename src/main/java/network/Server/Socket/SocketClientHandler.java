package network.Server.Socket;

import network.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class is used to handle connection between client and server.
 * It is used to read object sent from client and send object from the server to the client.
 * The class implements Runnable interface because it must be run through threads created from server every time a new
 * connection with a client is established.
 * */
public class SocketClientHandler implements Runnable{
    private Socket socket;
    private Integer token;
    private SocketServer server;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private boolean stop;

    /**
     * The constructor initialize the attribute of the class with the inputStream coming from the client and the
     * outputStream outgoing from the server.
     *
     * @param socket this is the socket of which the connection must be handled
     * */
    public SocketClientHandler(Socket socket, Integer token, SocketServer server) {
        this.socket = socket;
        this.token = token;
        this.server = server;
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        }catch (IOException e){
            //TODO logger
        }
        this.stop = false;
    }

    /**
     * This method is used to send message to the server.
     * @param message
     */
    public void respond(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            //TODO catch exception, logger
        }
    }

    /**
     * This is the method run when a new instance of this class is started.
     * It read the input stream to get the messages sent from the client, when a message is received is forwarded to the
     * main server.
     * */
    @Override
    public void run(){
        while (!stop){
            if (socket.isConnected()) {
                try {
                    Message message = (Message) in.readObject();
                    if (message != null)
                        server.onReceive(message);
                } catch (IOException | ClassNotFoundException e) {
                    //TODO logger
                }
            }
            else if (socket.isClosed()){
                server.removeClient(token);
            }
        }
    }
}
