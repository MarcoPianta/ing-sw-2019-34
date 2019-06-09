package network.Client.RMI;

import network.Server.Client;
import network.Server.RMI.RMIServerInterface;
import network.messages.Message;
import view.View;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

/**
 * This class is used from the client to communicate with RMIServer when a RMI connection is used.
 * */
public class RMIClient extends Client{
    private int PORT;
    private RMIServerInterface server;
    private static Logger logger = Logger.getLogger("rmiClient");

    /**
     * The constructor only initialize the host and PORT attribute, it doesn't establish connection. Attributes will be
     * used from init() to establish a new connection to the server
     * */
    public RMIClient(int port, View view){
        super(view);
        this.rmi = true;
        this.PORT = port;
    }

    /**
     * This method initialize a new RMI connection
     * get the registry by the port PORT, lookup for "Server"
     * rebind "Client" for the server that can lookup for it
     * */
    public void init() throws RemoteException{
        try {
            Registry registry = LocateRegistry.getRegistry(PORT);
            server = (RMIServerInterface) registry.lookup("Server");

            token = server.generateToken();
            server.acceptConnection(new RMIClientImplementation(this), token);
        }catch (Exception e){
            System.out.println("Client Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This method send a message to the server
     * @param message is the message that must be sent to the server
     * */
    public void send(Message message){
        try{

            server.onReceive(message);
        }catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to close connection between the client and the server.
     * */
    public void close(){
        System.exit(1);
    }


    public void onReceive(Message message){
        System.out.println("Sono il Client RMI: ho ricevuto questo messaggio --> " + message.getActionType() + " da " + message.getToken());
    }
}
