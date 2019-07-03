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
    private ArrayList<RMIClientInterface> clients;
    private HashMap<Integer , RMIClientInterface> rmiHashMap;
    private static Logger logger = Logger.getLogger("rmiServer");

    /**
     * This constructor creates a new RMI server listening on the default port 10001
     * @param server the main server
     * */
    public RMIServer(Server server) {
        this.server = server;
        this.PORT = 10001;
        rmiHashMap = new HashMap<>();
        clients = new ArrayList<>();
    }

    /**
     * This constructor creates a new RMI server listening on the port indicated as parameter
     * @param server the main server
     * @param port the port on which the server will listen for new connection
     * */
    public RMIServer(Server server, int port) {
        this.server = server;
        this.PORT = port;
        rmiHashMap = new HashMap<>();
        clients = new ArrayList<>();
    }

    private void init() throws RemoteException{
        System.setProperty("java.rmi.server.hostname", "192.168.0.3");
        Registry registry = LocateRegistry.createRegistry(PORT);
        try {
            registry.rebind("Server", new RMIServerImplementation(server, this));
            System.err.println("RMIServer ready");
        } catch (Exception e) {
            System.err.println("Server Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void send(Message message) {
        try {
            try {
                rmiHashMap.get(message.getToken()).onReceive(message);
            }catch (ConnectException e){
                removeClient(message.getToken());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void run() throws RemoteException {
        init();

    }

    public void acceptConnection(RMIClientInterface client, Integer token) throws RemoteException, NotBoundException {
        this.rmiHashMap.put(token, client);
    }

    /**
     * When a rmi connection is closed the client is remove from the hash map containing the token of every
     * connected client. The client is also removed from the main server
     * @param token the identifier of the client to be removed.
     */
    public void removeClient(Integer token){
        server.removeClient(token);
        rmiHashMap.remove(token);
    }
}
