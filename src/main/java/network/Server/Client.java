package network.Server;

import Model.Colors;
import Model.NormalSquare;
import Model.Player;
import network.messages.*;
import view.View;

/**
 * This Class represents the client, it is extended from the Socket or RMI client.
 * The class contains attributes and methods common to Socket and RMI
 */
public abstract class Client {
    protected boolean rmi;
    protected Integer token;
    protected View view;
    protected Colors playerColor = Colors.GREEN; //TODO delete assignment used only for testing

    public Client(View view){
        this.view = view;
    }

    public abstract void close();

    /**
     * This method receive a message from the server and handle it.
     * The method is called when a new message is received on input stream, it is read and passed as parameter, then
     * the proper action is executed.
     * @param message is the message received from the server
     * */
    public void onReceive(Message message) {
        if (message.getActionType().getAbbreviation().equals(ActionType.CONNECTIONRESPONSE.getAbbreviation())) {
            //If the message is a ConnectionResponse it contains the token, so it is saved
            ConnectionResponse response = (ConnectionResponse) message;
            this.token = response.getToken();
            view.showToken();
        }
        else if (message.getActionType().getAbbreviation().equals(ActionType.UPDATECLIENTS.getAbbreviation())) {
            UpdateClient update = (UpdateClient) message;
            handleUpdate(update);
        }
        else if (message.getActionType().getAbbreviation().equals(ActionType.RELOADED.getAbbreviation())) {
            ReloadedMessage reloadedMessage = (ReloadedMessage) message;
            if (reloadedMessage.getStatus()){}

        }
        else if (message.getActionType().getAbbreviation().equals(ActionType.GRABWEAPONRESPONSE.getAbbreviation())){
            GrabWeaponResponse grabWeaponResponse = (GrabWeaponResponse) message;
            //view.addWeapon();
        }
        else if (message.getActionType().getAbbreviation().equals(ActionType.CANUSEVENOM.getAbbreviation())){
            view.showVenomRequest();
        }
        else if (message.getActionType().getAbbreviation().equals(ActionType.START.getAbbreviation())){
            StartMessage startMessage = (StartMessage) message;
            if (startMessage.getType().equals("game"))
                view.startGame();
            else if (startMessage.getType().equals("turn"))
                view.startTurn();
        }
        else if(message.getActionType().getAbbreviation().equals(ActionType.GAMESETTINGSREQUEST.getAbbreviation())){
            view.showGameSettingsRequest();
        }
        else if (message.getActionType().getAbbreviation().equals(ActionType.WINNER.getAbbreviation())){
            WinnerMessage winnerMessage = (WinnerMessage) message;
            view.endGame(winnerMessage.isWinner());
        }
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

    public void handleUpdate(UpdateClient message){
        if (message.getUpdateType().equals(UpdateClient.DAMAGEBARUPDATE))
            view.setDamageBar(message.getDamageBar());

        else if (message.getUpdateType().equals(UpdateClient.POSITION))
            view.setMyPositionID(message.getSquareID());

        else if (message.getUpdateType().equals(UpdateClient.POSSIBLESQUARES))
            view.showReachableSquares(message.getReachableSquares());

        else if (message.getUpdateType().equals(UpdateClient.POSSIBLETARGET))
            view.showPossibleTarget(message.getReachableTarget());

        else if (message.getUpdateType().equals(UpdateClient.RESPAWN)) {
            view.addPowerup(message.getPowerUp());
            view.showPowerUpChooseRespawn();
        }

        else if (message.getUpdateType().equals(UpdateClient.MESSAGE))
            view.showMessage(message.getMessage());
    }

    public Integer getToken() {
        return token;
    }

    public Colors getPlayerColor() {
        return playerColor;
    }
}
