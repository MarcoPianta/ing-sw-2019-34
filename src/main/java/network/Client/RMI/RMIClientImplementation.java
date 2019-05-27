package network.Client.RMI;

import network.messages.Message;

import java.rmi.RemoteException;

public class RMIClientImplementation implements RMIClientInterface {
    private RMIClient rmiClient;

    public RMIClientImplementation(RMIClient rmiClient) {
        this.rmiClient = rmiClient;
    }

    @Override
    public void onReceive(Message message) throws RemoteException {
        rmiClient.onReceive(message);
        System.out.println("Il server ha chiamato la onReceive e funziona");
    }
}
