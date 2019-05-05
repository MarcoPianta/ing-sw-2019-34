package network.Server.Socket;

import network.Server.Server;
import network.messages.ActionType;
import network.messages.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SocketServer {
    private ServerSocket serverSocket;
    private int port;
    private Server server; //This variable is used to contain the main server
    private ArrayList<Socket> clients;

    public SocketServer(Server server, int port) {
        this.server = server;
        this.port = port;
        clients = new ArrayList<>();
    }

    //TODO catch exception
    private void init() throws IOException{
        serverSocket = new ServerSocket(port);
    }

    //TODO catch exception
    public Socket acceptConnection() throws IOException{
        Socket socket = serverSocket.accept();;
        clients.add(socket);
        return socket;
    }

    //TODO catch exception
    public void handleConnection(Socket client) throws IOException{

        try {
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));

            Message m = (Message) in.readObject();


        }catch (IOException e){
            //TODO handle exception, maybe with a logger
        }catch (ClassNotFoundException e){
            //TODO handle exception, maybe with a logger
        }
    }

    public void readMessage(Message message){
        if (message.getActionType().getAbbreviation().equals(ActionType.CONNECTIONREQUEST.getAbbreviation()) ){

        }
    }

    public void run() throws IOException{
        init();

        acceptConnection();
    }
}
