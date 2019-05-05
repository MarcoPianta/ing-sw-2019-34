package network.Server.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteObjectInterface extends Remote {
    String sayHello() throws RemoteException;
}
