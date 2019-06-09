package network.Client.RMI;

import network.messages.Message;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
