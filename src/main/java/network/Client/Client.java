package network.Client;

import Model.*;
import network.messages.*;
import view.View;

import java.util.ArrayList;

/**
 * This Class represents the client, it is extended from the Socket or RMI client.
 * The class contains attributes and methods common to Socket and RMI
 */
public abstract class Client {
    protected boolean rmi;
    protected Integer token;
    protected View view;
    protected Colors playerColor;

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
        else if (message.getActionType().getAbbreviation().equals(ActionType.PAYMENT.getAbbreviation())) {
            view.payment((Payment) message);
        }
        else if (message.getActionType().getAbbreviation().equals(ActionType.PAYMENT.getAbbreviation())){
            view.payment((Payment) message);
        }
        else if(message.getActionType().getAbbreviation().equals(ActionType.SHOOTREQUESTP.getAbbreviation())){
            ShootRequestp shootRequestp = (ShootRequestp) message;
            //view.showPossibleTarget(shootRequestp.getTargetablePlayer(), shootRequestp.getTargetNumber());
        }
        else if(message.getActionType().getAbbreviation().equals(ActionType.SHOOTREQUESTR.getAbbreviation())){
            ShootRequestr shootRequestr = (ShootRequestr) message;
            view.showPossibleRooms(shootRequestr.getRoomTargetable());
        }
        else if(message.getActionType().getAbbreviation().equals(ActionType.SHOOTREQUESTS.getAbbreviation())){
            ShootRequests shootRequests = (ShootRequests) message;
            //view.showPossibleSquares(shootRequests.getTargetableSquare(), shootRequests.getSquareNumber());
        }
        else if(message.getActionType().getAbbreviation().equals(ActionType.TARGETMOVEREQUEST.getAbbreviation())){
            TargetMoveRequest targetMoveRequest = (TargetMoveRequest) message;
            view.showTargetMove(targetMoveRequest.getTargetableSquare());
        }
        else if (message.getActionType().getAbbreviation().equals(ActionType.GRABWEAPONRESPONSE.getAbbreviation())){
            GrabWeaponRequest grabWeaponResponse = (GrabWeaponRequest) message;
            //view.addWeapon();
        }
        else if (message.getActionType().getAbbreviation().equals(ActionType.GRABWEAPONREQUEST.getAbbreviation())){
            GrabWeaponRequest grabWeaponResponse = (GrabWeaponRequest) message;
            //view.addWeapon();
        }
        else if (message.getActionType().getAbbreviation().equals(ActionType.CANUSEVENOM.getAbbreviation())){
            CanUseTagBack canUseTagBack = (CanUseTagBack) message;
            //view.showVenomRequest(canUseTagBack.getPlayerShooter());
        }
        else if (message.getActionType().getAbbreviation().equals(ActionType.START.getAbbreviation())){
            StartMessage startMessage = (StartMessage) message;
            if (startMessage.getType().equals("game")) {
                this.playerColor = startMessage.getMyColor();
                view.startGame(startMessage.getMap());
            }
            else if (startMessage.getType().equals("turn")){
                view.startTurn();
                view.setNumberAction(1);
            }
        }
        else if (message.getActionType().getAbbreviation().equals(ActionType.END.getAbbreviation())){
            view.setMyTurn(false);
        }
        else if (message.getActionType().getAbbreviation().equals(ActionType.MESSAGE.getAbbreviation())){
            ChatMessage chatMessage = (ChatMessage) message;
            view.chatMessage(chatMessage.getMessage());
        }
        else if(message.getActionType().getAbbreviation().equals(ActionType.GAMESETTINGSREQUEST.getAbbreviation())){
            view.showGameSettingsRequest();
        }
        else if (message.getActionType().getAbbreviation().equals(ActionType.TIMEOUT.getAbbreviation())){
            view.setMyTurn(false);
        }
        else if (message.getActionType().getAbbreviation().equals(ActionType.WINNER.getAbbreviation())){
            WinnerMessage winnerMessage = (WinnerMessage) message;
            view.endGame(winnerMessage.isWinner());
        }
        else if(message.getActionType().getAbbreviation().equals(ActionType.FINALACTION.getAbbreviation())){
            if(view.getNumberAction()==1)
                view.setNumberAction(2);
            else
                view.setNumberAction(3);
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
        if (message.getUpdateType().equals(UpdateClient.DAMAGEBARUPDATE)){
            view.setDamageBar(message.getDamageBar());
            view.setMarks(message.getMarks());
        }

        else if (message.getUpdateType().equals(UpdateClient.POSITION))
            view.setMyPositionID(message.getSquareID());

        else if (message.getUpdateType().equals(UpdateClient.POSSIBLESQUARES))
            view.showReachableSquares(message.getReachableSquares());

        else if (message.getUpdateType().equals(UpdateClient.POSSIBLETARGET)){
            //view.showPossibleTarget(message.getReachableTarget());
        }

        else if (message.getUpdateType().equals(UpdateClient.RESPAWN)) {
            System.out.println("Respawn");
            view.addPowerup(message.getPowerUp());
            view.showPowerUpChooseRespawn();
        }
        else if (message.getUpdateType().equals(UpdateClient.OTHERPOSITION))
            view.setOtherPosition(message.getOtherColor(), message.getSquareID());
        else if(message.getUpdateType().equals(UpdateClient.HANDPLAYER)){
            System.out.println("handupdate");
            view.setBlueAmmo(message.getHandPlayer().getAmmoRYB()[2]);
            view.setRedAmmo(message.getHandPlayer().getAmmoRYB()[0]);
            view.setYellowAmmo(message.getHandPlayer().getAmmoRYB()[1]);
            view.setPowerUps((ArrayList<CardPowerUp>) message.getHandPlayer().getPlayerPowerUps());
            view.setWeapons((ArrayList<CardWeapon>) message.getHandPlayer().getPlayerWeapons());
            System.out.println((ArrayList<CardPowerUp>) message.getHandPlayer().getPlayerPowerUps());
        }
        else if (message.getUpdateType().equals(UpdateClient.MESSAGE))
            view.showMessage(message.getMessage());
        else if(message.getUpdateType().equals(UpdateClient.FILLSPAWN))
            view.fillSpawn(message.getSquareID(), message.getPosWeapon(), message.getWeapon().getName());

        else if(message.getUpdateType().equals(UpdateClient.FILLSQUARE))
            view.fillSquare(message.getSquareID(),message.getAmmo());
    }

    public Integer getToken() {
        return token;
    }

    public Colors getPlayerColor() {
        return playerColor;
    }
}