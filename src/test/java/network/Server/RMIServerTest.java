package network.Server;

import network.Client.RMI.RMIClient;
import network.Server.RMI.RMIServer;
import network.messages.Message;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static org.junit.jupiter.api.Assertions.*;

public class RMIServerTest {
    private static final int PORT = 10001;
    private static Server server;

    @BeforeAll
    public static void beforeAll(){
        server = new Server();
    }


    @Test
    public void runTest() throws RemoteException, NotBoundException {
        RMIServer rmiServer = new RMIServer(server, PORT);
        new Thread(() -> {
            try{
                rmiServer.run();
            }catch(Exception e){
                //nothing to do
            }
        }).start();
        Registry registry = LocateRegistry.getRegistry(PORT);

        assertNotEquals(null, registry.lookup("Server"));
    }

    @Test
    public void onReceiveTest() throws RemoteException{
        RMIServer rmiServer = new RMIServer(server, PORT);
        new Thread(() -> {
            try{
                rmiServer.run();
            }catch(Exception e){
                //nothing to do
            }
        }).start();
        RMIClient rmiClient = new RMIClient(PORT);
        rmiClient.init();
        rmiClient.send(new Message());
    }

}
