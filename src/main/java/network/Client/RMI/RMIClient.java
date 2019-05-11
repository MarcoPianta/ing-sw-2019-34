package network.Client.RMI;

import network.Server.RMI.RMIServerInterface;
import network.messages.Message;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * This class is used from the client to communicate with RMIServer when a RMI connection is used.
 * */
public class RMIClient {
    private int port;
    //private Client client;
    private RMIServerInterface server;

    /**
     * The constructor only initialize the host and port attribute, it doesn't establish connection. Attributes will be
     * used from init() to establish a new connection to the server
     * */
    public RMIClient(int port){
        this.port = port;
    }

    /**
     * This method initialize a new RMI connection and set the output and input stream used to send and receive
     * message from the server.
     * */
    public void init() {
        try {
            Registry registry = LocateRegistry.getRegistry(port);
            server = (RMIServerInterface) registry.lookup("Server");

        }catch (Exception e){
            System.out.println("Client Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This method send a message to the server
     * @param message is the message that must be sent to the server
     * */
    public void send(Message message) throws RemoteException {
            server.send(message);
    }

    /**
     * This method is used to close connection between the client and the server.
     * */
    public void close(){
        System.exit(1);
    }

}
