package network.Server.RMI;

import network.Client.RMI.RMIClient;
import network.Server.Server;
import network.messages.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServerImplementation extends UnicastRemoteObject implements RMIServerInterface {

    private Server server;

    public RMIServerImplementation(Server server) throws RemoteException {
        this.server = server;
    }

    public void acceptConnection() throws RemoteException {
/*
*  TODO (String username, ClientInterface client) {
        server.addClient(username, client);
    }
* */
    }

    public void send(Message message) {
       //TODO server.onRecieve(message);
        System.out.println("SEND HA FUNZIONATO");
    }

}
