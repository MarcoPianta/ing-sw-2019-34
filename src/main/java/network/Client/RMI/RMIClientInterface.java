package network.Client.RMI;

import network.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClientInterface extends Remote {

    void send(Message message) throws RemoteException;

}
