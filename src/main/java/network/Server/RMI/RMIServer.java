package network.Server.RMI;

import network.Client.RMI.RMIClientInterface;
import network.Server.Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Logger;

public class RMIServer {

    private Server server;
    private int port ;
    private static Logger logger = Logger.getLogger("rmiServer");
    private ArrayList<RMIClientInterface> clients;

    public RMIServer(Server server, int port) {
        this.server = server;
        this.port = port;
        clients = new ArrayList<>();
    }

    private void init() throws RemoteException{
        Registry registry = LocateRegistry.createRegistry(port);
        try {
            registry.rebind("Server", new RMIServerImplementation(server));
            System.err.println("RMIServer ready");
        } catch (RemoteException e) {
            System.err.println("Server Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void run() throws RemoteException {
        init();
    }


}
