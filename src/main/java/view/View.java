package view;

import Model.*;
import network.Server.Client;
import network.messages.GameSettingsResponse;
import network.messages.RespawnMessage;
import network.messages.WinnerMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class contains the data and method used from the cli or the GUI to show the current state of the game.
 */
public abstract class View {
    protected Client client;

    protected ArrayList<CardWeapon> weapons; /**The weapons owned from hte player*/
    protected int blueAmmo; /**Number of blue ammo*/
    protected int redAmmo; /**Number of red ammo*/
    protected int yellowAmmo; /**Number of yellow ammo*/
    protected ArrayList<Colors> damageBar; /**The player damage bar, is an array*/
    protected ArrayList<Colors> marks;
    protected ArrayList<CardPowerUp> powerUps; /**The powerUps owned from the player*/
    protected GameBoard map;
    protected int maxReward; /**The max reward gained from other players when player die*/
    protected String myPositionID; /**This variable contains the ID of player's current square */
    protected ArrayList<String> otherPlayersPosition; /**This ArrayList contains the IDs of the square where other players are on*/
    protected HashMap<String, Boolean> charge;

    public View(Client client){
        this.weapons = new ArrayList<>(4);
        while (weapons.size() < 4)
            weapons.add(null);

        this.blueAmmo = this.redAmmo = this.yellowAmmo = 1;
        this.damageBar = new ArrayList<>();
        while (damageBar.size()<12)
            damageBar.add(Colors.NULL);

        this.marks = new ArrayList<>();
        while (marks.size()<12)
            marks.add(Colors.NULL);

        this.powerUps = new ArrayList<>();
        this.maxReward = 0;
        this.otherPlayersPosition = new ArrayList<>();

        this.client = client;
    }

    public abstract void showReachableSquares(List<String> squares);

    public abstract void showPossibleTarget(List<String> targets);

    public abstract void showPowerUpChooseRespawn();

    public abstract void showMessage(String message);

    public abstract void showVenomRequest();//TODO send UsePowerUp if player to use it venom

    public abstract void showGameSettingsRequest();

    public abstract void endGame(boolean winner);

    /**
     * This method add a weapon to the player hand
     * @param cardWeapon the weapon
     * @param position the arrayList position
     */
    public void addWeapon(CardWeapon cardWeapon, int position){
        weapons.set(position, cardWeapon);
    }

    /**
     * This method is used to set the new damageBar received from server.
     * @param damageBar an ArrayList conayining the new damageBar
     */
    public void setDamageBar(List<Colors> damageBar) {
        this.damageBar = new ArrayList<>(damageBar);
    }

    public void setMyPositionID(String myPositionID) {
        this.myPositionID = myPositionID;
    }

    public void addPowerup(CardPowerUp powerUp){
        powerUps.add(powerUp);
    }

    public void setBlueAmmo(int blueAmmo) {
        this.blueAmmo = blueAmmo;
    }

    public void setRedAmmo(int redAmmo) {
        this.redAmmo = redAmmo;
    }

    public void setYellowAmmo(int yellowAmmo) {
        this.yellowAmmo = yellowAmmo;
    }

    public void respawnResponse(CardPowerUp powerUp){
        client.send(new RespawnMessage(client.getToken(), powerUp));
    }
}
