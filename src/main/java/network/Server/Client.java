package network.Server;

import Model.Player;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private Player player;
    private Socket connection;
    //TODO RmiServer server;

    public Client(Player player){
        this.player = player;
    }

    public Socket getSocket() {
        return connection;
    }

    public void setConnection(String host) throws IOException {
        connection = new Socket(host, 10000);
    }

    public void init(){

    }
}
