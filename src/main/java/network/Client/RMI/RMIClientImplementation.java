package network.Client.RMI;

import network.messages.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class represent the client on rmi server
 */
public class RMIClientImplementation extends UnicastRemoteObject implements RMIClientInterface {
    private RMIClient rmiClient;

    public RMIClientImplementation(RMIClient rmiClient) throws RemoteException {
        super();
        this.rmiClient = rmiClient;
    }

    @Override
    public void onReceive(Message message) throws RemoteException {
        rmiClient.onReceive(message);
    }
}
