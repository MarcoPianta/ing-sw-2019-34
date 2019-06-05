package network.Server.RMI;

import network.messages.Message;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote {

    void onReceive(Message message)throws RemoteException;
    Integer generateToken() throws RemoteException;
    void acceptConnection(Integer token) throws RemoteException, NotBoundException;

}
