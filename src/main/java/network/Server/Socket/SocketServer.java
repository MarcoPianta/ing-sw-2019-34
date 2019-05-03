package network.Server.Socket;

import network.Server.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class SocketServer {
    private int port;
    private ServerSocket sc;
    private ArrayList<Client> clients;

    public SocketServer() throws IOException {
        sc = new ServerSocket(10000);
        clients = new ArrayList<>();
    }

    public void run() throws IOException{
        clients.add(new Client(sc.accept()));

    }
}
