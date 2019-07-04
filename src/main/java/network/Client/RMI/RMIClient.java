package network.Client.RMI;

import network.Client.Client;
import network.Server.RMI.RMIServerInterface;
import network.messages.*;
import view.View;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

/**
 * This class is used from the client to communicate with RMIServer when a RMI connection is used.
 * */
public class RMIClient extends Client{
    private String hostname;
    private int PORT;
    private RMIServerInterface server;
    private static Logger logger = Logger.getLogger("rmiClient");

    /**
     * The constructor only initialize the host and PORT attribute, it doesn't establish connection. Attributes will be
     * used from init() to establish a new connection to the server
     * */
    public RMIClient(String hostname, int port, View view){
        super(view);
        this.rmi = true;
        this.PORT = port;
        this.hostname = hostname;
        init();
    }

    /**
     * This method initialize a new RMI connection
     * get the registry by the port PORT, lookup for "Server"
     * rebind "Client" for the server that can lookup for it
     * */
    public void init() {
        try {
            Registry registry = LocateRegistry.getRegistry(hostname, PORT);
            server = (RMIServerInterface) registry.lookup("Server");

            this.token = server.generateToken();
            server.acceptConnection(new RMIClientImplementation(this), token);
        }catch (Exception e){
            logger.severe(e.getMessage());
        }
        new Thread(() ->{
                onReceive(new ConnectionResponse(token));
                onReceive(new GameSettingsRequest(token));
        }).start();
    }

    /**
     * This method send a message to the server
     * @param message is the message that must be sent to the server
     * */
    public void send(Message message){
        try{

            server.onReceive(message);
        }catch (RemoteException e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * This method is used to close connection between the client and the server.
     * */
    public void close(){
        System.exit(1);
    }
}
