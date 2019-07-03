package network.Server.RMI;

import network.Client.RMI.RMIClientInterface;
import network.Server.Server;
import network.messages.ActionType;
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
        if (message.getActionType().getAbbreviation().equals(ActionType.DISCONNECT))
            rmiServer.removeClient(message.getToken());
       server.onReceive(message);
    }

    public void acceptConnection(RMIClientInterface client, Integer token) throws RemoteException, NotBoundException {
        rmiServer.acceptConnection(client, token);
    }

    public Integer generateToken() throws RemoteException {
        return server.generateToken(0, true);
    }
}
