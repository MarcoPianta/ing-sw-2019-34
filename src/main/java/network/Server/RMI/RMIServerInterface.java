package network.Server.RMI;

import network.Client.RMI.RMIClient;
import network.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote {

    void acceptConnection() throws RemoteException;
    void send(Message message) throws RemoteException;

    void register(String username, RMIClient client);
}
