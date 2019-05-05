package network.Server.RMI;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer implements RemoteObjectInterface {


    protected RMIServer() throws RemoteException {
        super();
    }

    @Override
    public String sayHello(){
        return "FUNZIONOOOOO";
    }

    public static void main(String args[]) {
        try{
            RMIServer serverRMI = new RMIServer();
            RemoteObjectInterface stub = (RemoteObjectInterface) UnicastRemoteObject.exportObject(serverRMI, 10000);
            Registry registry = LocateRegistry.createRegistry(10000);
            registry.bind("STUB", stub);
            System.err.println("RMIServer ready");

        } catch (RemoteException | AlreadyBoundException e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
