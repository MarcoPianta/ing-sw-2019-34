package network.Client.Socket;

import network.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * This class is used to handle the connection between socket client and server on the client side.
 * It is used to read messages received from the server and send messages to the client using socket connection.
 * The class implements Runnable interface as it will be run as Thread from client.
 * */
public class NetworkHandler implements Runnable{
    private SocketClient client;
    private ObjectInputStream in; //Used to send message to server
    private ObjectOutputStream out; //Used to receive message from server
    private boolean loop;
    private static Logger logger = Logger.getLogger("NetworkHandler");


    /**
     * The constructor create input and output stream of the socket connection to communicate with server.
     * @param client the client of which the connection must be handled
     */
    public NetworkHandler(SocketClient client, ObjectInputStream in, ObjectOutputStream out){
        try {
            this.out = out;
            this.out.flush();
            this.in = in;
            this.client = client;
            loop = true;
        }catch (IOException e){
            loop = false;
        }
    }

    /**
     * This method is run when the thread which contains the class is started.
     * It loops to read object on the input stream sent from the server, when a message is received is forwarded to the
     * client.
     * To forward the message is used the onReceive method in SocketClient which is inherited from the Client class
     * without overriding.
     */
    @Override
    public void run() {
        while (loop){
            try {
                Message message = (Message) in.readObject();
                if (message == null) {
                    loop = false;
                }
                else {
                    client.onReceive(message);
                }
            }catch (IOException|ClassNotFoundException e){
                loop = false;
                logger.severe(e.getMessage());
            }
        }
    }
}
