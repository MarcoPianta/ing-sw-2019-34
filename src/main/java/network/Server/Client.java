package network.Server;

import Model.Player;
import network.messages.ActionType;
import network.messages.Message;

public class Client {
    protected Player player;
    protected boolean rmi;

    public Client(){
        this.player = null;
    }

    /**
     * This method receive a message from the server and handle it.
     * The method is called from NetworkHandler, when a new message is received on input stream it is read and passed as
     * parameter.
     * @param message is the message received from the server
     * */
    public void onReceive(Message message) {
        if (message.getActionType().getAbbreviation().equals(ActionType.CONNECTIONRESPONSE.getAbbreviation())) {
            //TODO call setPlayer(); set new player in the client
        }
        else if (message.getActionType().getAbbreviation().equals(ActionType.UPDATECLIENTS.getAbbreviation())) {
            //TODO call method
        }
    }

    private void setPlayer(Player player){
        this.player = player;
    }

    public boolean isRmi() {
        return rmi;
    }
}
