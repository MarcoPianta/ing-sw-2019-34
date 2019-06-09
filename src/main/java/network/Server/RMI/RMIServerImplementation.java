package network.Server.RMI;

import network.Client.RMI.RMIClientInterface;
import network.Server.Server;
import network.messages.Message;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServerImplementation extends UnicastRemoteObject implements RMIServerInterface {
    private RMIServer rmiServer;
    private Server server;

    public RMIServerImplementation(Server server, RMIServer rmiServer) throws RemoteException {
        this.server = server;
        this.rmiServer = rmiServer;
    }

    public void onReceive(Message message) {
       server.onReceive(message);
    }

    public void acceptConnection(RMIClientInterface client, Integer token) throws RemoteException, NotBoundException {
        rmiServer.acceptConnection(client, token);
    }

    public Integer generateToken() throws RemoteException {
        return server.generateToken(true);
    }
}
