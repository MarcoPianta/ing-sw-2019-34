package view;

import Model.*;
import network.Client.Client;
import network.messages.Payment;
import network.messages.RespawnMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class contains the data and method used from the cli or the GUI to show the current state of the game.
 */
public abstract class View {
    protected Client client;

    protected ArrayList<CardWeapon> weapons;
    /**
     * The weapons owned from hte player
     */
    protected int blueAmmo;
    /**
     * Number of blue ammo
     */
    protected int redAmmo;
    /**
     * Number of red ammo
     */
    protected int yellowAmmo;
    /**
     * Number of yellow ammo
     */
    protected ArrayList<Colors> damageBar;
    /**
     * The player damage bar, is an array
     */
    protected ArrayList<Colors> marks;
    protected ArrayList<CardPowerUp> powerUps;
    /**
     * The powerUps owned from the player
     */
    protected GameBoard map;
    protected int maxReward;
    /**
     * The max reward gained from other players when player die
     */
    protected String myPositionID;
    /**
     * This variable contains the ID of player's current square
     */
    protected String[] otherPlayersPosition = new String[4];
    /**
     * This ArrayList contains the IDs of the square where other players are on
     */
    protected HashMap<String, Boolean> charge;
    protected Integer posWeapon;
    protected Integer posEffect;
    protected HashMap<Colors, Integer> players;//bisogna inizializzare
    protected ArrayList<Colors> playersColor;//bisogna inizializzare
    private boolean myTurn;

    public View() {
        this.weapons = new ArrayList<>(4);
        while (weapons.size() < 4)
            weapons.add(null);

        this.blueAmmo = this.redAmmo = this.yellowAmmo = 1;
        this.damageBar = new ArrayList<>();
        while (damageBar.size() < 12)
            damageBar.add(Colors.NULL);

        this.marks = new ArrayList<>();
        while (marks.size() < 12)
            marks.add(Colors.NULL);

        this.powerUps = new ArrayList<>();
        this.maxReward = 0;

    }

    public abstract void showToken();

    public abstract void showReachableSquares(List<String> squares);// for move

    public abstract void showPossibleTarget(List<Colors> targets);

    public abstract void showPossibleRooms(List<String> ids); //For shot action

    public abstract void showPossibleSquares(List<Colors> targets); // For shot action

    public abstract void showTargetMove(List<Colors> targets); //When need to be shown target which have to be moved for a weapon effect

    public abstract void payment(Payment message);

    public abstract void updateEnemiesDamageBar(ArrayList<Colors> damageBar, Colors player);

    public abstract void updateEnemyPosition(Colors player, String position);

    public abstract void showPowerUpChooseRespawn();

    public abstract void showMessage(String message);

    public abstract void showVenomRequest(Colors color);//TODO send UsePowerUp if player to use it venom

    public abstract void showGameSettingsRequest();

    public abstract void startGame();

    public abstract void startTurn();

    public abstract void chatMessage(String message);

    public abstract void endGame(boolean winner);

    /**
     * This method add a weapon to the player hand
     *
     * @param cardWeapon the weapon
     * @param position   the arrayList position
     */
    public void addWeapon(CardWeapon cardWeapon, int position) {
        weapons.set(position, cardWeapon);
    }

    /**
     * This method is used to set the new damageBar received from server.
     *
     * @param damageBar an ArrayList conayining the new damageBar
     */
    public void setDamageBar(List<Colors> damageBar) {
        this.damageBar = new ArrayList<>(damageBar);
    }

    public void setMyPositionID(String myPositionID) {
        this.myPositionID = myPositionID;
    }

    public void addPowerup(CardPowerUp powerUp) {
        powerUps.add(powerUp);
    }

    public void setPowerUps(ArrayList<CardPowerUp> powerUps) {
        this.powerUps = powerUps;
    }

    public void setWeapons(ArrayList<CardWeapon> weapons) {
        this.weapons = weapons;
    }

    public void setMap(GameBoard map) {
        this.map = map;
    }

    public ArrayList<CardPowerUp> getPowerUps() {
        return powerUps;
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

    public void respawnResponse(Integer powerUp) {
        client.send(new RespawnMessage(client.getToken(), powerUp));
    }

    public void setOtherPosition(int player, String position) {
        otherPlayersPosition[player] = position;
    }

    public void setMyTurn(boolean myTurn){
        this.myTurn = myTurn;
    }

}


