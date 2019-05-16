package network.Server;

import Model.Player;
import network.messages.ActionType;
import network.messages.ConnectionResponse;
import network.messages.Message;

/**
 * This Class represents the client, it is extended from the Socket or RMI client.
 * The class contains attributes and methods common to Socket and RMI
 */
public abstract class Client {
    protected Player player;
    protected boolean rmi;
    protected Integer token;

    public Client(){
        this.player = null;
    }

    /**
     * This method receive a message from the server and handle it.
     * The method is called from when a new message is received on input stream it is read and passed as
     * parameter.
     * @param message is the message received from the server
     * */
    public void onReceive(Message message) {
        if (message.getActionType().getAbbreviation().equals(ActionType.CONNECTIONRESPONSE.getAbbreviation())) {
            //If the message is a ConnectionResponse it contains the token, so it is saved
            ConnectionResponse response = (ConnectionResponse) message;
            this.token = response.getToken();
        }
        else if (message.getActionType().getAbbreviation().equals(ActionType.UPDATECLIENTS.getAbbreviation())) {
        }
        else if(message.getActionType().getAbbreviation().equals(ActionType.GAMESETTINGSREQUEST.getAbbreviation())){
            //TODO update view asking client which configuration want to use to play, then send GAMESETTINGSRESPONSE to the server
        }
    }

    private void setPlayer(Player player){
        this.player = player;
    }

    /**
     * This method is used to know if the client communicate with the server using Socket or RMI
     * @return a boolean, if true the client communicate with RMI, otherwise the client use Socket
     */
    public boolean isRmi() {
        return rmi;
    }

    /**
     * This method is abstract because the implementation will be different for Socket and RMI client which will inherit
     * this method signature.
     * @param message the message that must be sent
     * */
    public abstract void send(Message message);
}
