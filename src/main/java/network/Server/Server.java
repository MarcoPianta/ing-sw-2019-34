package network.Server;

import network.Server.Socket.SocketServer;
import network.messages.ActionType;
import network.messages.Message;

import java.net.Socket;

public class Server {
    private Queue playersQueue;
    private SocketServer socketServer;
    //TODO RmiServer rmiServer

    public Server(){
        playersQueue = new Queue();
        socketServer = new SocketServer(this, 10000);
    }

    public void addToQueue(Socket client){
        playersQueue.addPlayer(client);
    }

    public void onReceive(Message message){
        if ((message.getActionType().getAbbreviation().equals(ActionType.POSSIBLETARGETSHOT)) || (message.getActionType().getAbbreviation().equals(ActionType.POSSIBLEMOVE))){
            //TODO
        }
        else if ((message.getActionType().getAbbreviation().equals(ActionType.SHOT)) || (message.getActionType().getAbbreviation().equals(ActionType.MOVE)) || (message.getActionType().getAbbreviation().equals(ActionType.RELOAD))  || (message.getActionType().getAbbreviation().equals(ActionType.PASS)) || (message.getActionType().getAbbreviation().equals(ActionType.GRABWEAPON))){

        }
    }

    public void send(){} //TODO handle inheritance of this method for socket and rmi
}
