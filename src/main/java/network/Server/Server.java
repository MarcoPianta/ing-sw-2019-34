package network.Server;

import network.Server.Socket.SocketServer;

public class Server {
    private Queue playersQueue;
    private SocketServer socketServer;
    //TODO RmiServer rmiServer

    public Server(){
        playersQueue = new Queue();
    }
}
