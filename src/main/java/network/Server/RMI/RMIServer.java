package network.Server.RMI;

import network.Client.RMI.RMIClientInterface;
import network.Server.Server;
import network.messages.ConnectionResponse;
import network.messages.GameSettingsRequest;
import network.messages.Message;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class RMIServer {

    private final int PORT;
    private Server server;
    private HashMap<Integer , RMIClientInterface> rmiHashMap;
    private String myip;
    private static Logger logger = Logger.getLogger("rmiServer");

    /**
     * This constructor creates a new RMI server listening on the default port 10001
     * @param server the main server
     * */
    public RMIServer(Server server) {
        this.server = server;
        this.PORT = 10001;
        rmiHashMap = new HashMap<>();
    }

    /**
     * This constructor creates a new RMI server listening on the port indicated as parameter
     * @param server the main server
     * @param port the port on which the server will listen for new connection
     * */
    public RMIServer(Server server, int port, String myip) {
        this.server = server;
        this.PORT = port;
        rmiHashMap = new HashMap<>();
        this.myip = myip;
    }

    /**
     * This method execute the initialization of RMI server
     */
    private void init() throws RemoteException{
        System.setProperty("java.rmi.server.hostname", myip);
        Registry registry = LocateRegistry.createRegistry(PORT);
        try {
            registry.rebind("Server", new RMIServerImplementation(server, this));
            logger.info("RMIServer ready");
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * This method is used to send messages to the client
     */
    public void send(Message message) {
        try {
            try {
                rmiHashMap.get(message.getToken()).onReceive(message);
            }catch (ConnectException e){
                removeClient(message.getToken());
            }
        } catch (RemoteException e) {
            logger.severe(e.getMessage());
        }
    }

    public void run() throws RemoteException {
        init();

    }

    void acceptConnection(RMIClientInterface client, Integer token) throws RemoteException, NotBoundException {
        this.rmiHashMap.put(token, client);
    }

    /**
     * When a rmi connection is closed the client is remove from the hash map containing the token of every
     * connected client. The client is also removed from the main server
     * @param token the identifier of the client to be removed.
     */
    void removeClient(Integer token){
        server.removeClient(token);
        rmiHashMap.remove(token);
    }
}
