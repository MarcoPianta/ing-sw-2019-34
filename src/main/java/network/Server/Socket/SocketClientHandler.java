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
 * connection with a client is established
 * */
public class SocketClientHandler implements Runnable{
    private Socket socket;
    private SocketServer server;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private boolean stop;

    /**
     * The constructor initialize the attribute of the class with the inputStream coming from the client and the
     * outputStream outgoing from the client.
     *
     * @param socket this is the socket of which the connection must be handled
     * */
    public SocketClientHandler(Socket socket, SocketServer server) {
        this.socket = socket;
        this.server = server;
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        }catch (IOException e){
            //TODO logger
        }
        this.stop = false;
    }

    public void respond(Message message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            //TODO catch exception, logger
        }
    }

    /**
     * This is the method run when a new instance of this class is started
     * */
    @Override
    public void run(){
        while (!stop){
            try {
                Message message = (Message) in.readObject();
                if (message != null)
                    server.onReceive(message);
            }catch (IOException|ClassNotFoundException e){
                //TODO logger
            }
        }
    }
}
