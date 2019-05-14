package network.Client.Socket;

import network.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class is used to handle the connection between socket client and server on the client side.
 * It is used to read messages received from the server and send messages to the client using socket connection.
 * The class implements Runnable interface as it will be run as Thread from client.
 * */
public class NetworkHandler implements Runnable{
    private SocketClient client;
    private Socket connection;
    private ObjectInputStream in; //Used to send message to server
    private ObjectOutputStream out; //Used to receive message from server
    private boolean loop;

    public NetworkHandler(SocketClient client, Socket connection){
        try {
            out = new ObjectOutputStream(this.connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            this.client = client;
            this.connection = connection;
            loop = true;
        }catch (IOException e){
            loop = false;
        }
    }

    @Override
    public void run() {
        while (loop){
            try {
                Message message = (Message) in.readObject();
                if (message == null)
                    loop = false;
                else
                    client.onReceive(message);
            }catch (IOException|ClassNotFoundException e){
                loop = false;
            }
        }
    }
}
