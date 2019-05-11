package network.Server.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class RemoteObjectImplementation extends UnicastRemoteObject implements RemoteObjectInterface, Remote {

    protected RemoteObjectImplementation() throws RemoteException {
    }

    protected RemoteObjectImplementation(int port) throws RemoteException {
        super(port);
    }

    protected RemoteObjectImplementation(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    @Override
    public String sayHello() throws RemoteException {
        return "Hello World";
    }
}
