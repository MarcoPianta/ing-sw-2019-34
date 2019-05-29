package view;

import Model.Colors;

import java.util.List;

/**
 * This class contains the data and method used from the cli or the GUI to show the current state of the game.
 */
public abstract class View {
    public abstract void showReachableSquares();

    public abstract void showMessage(String message);

    public void setDamageBar(List<Colors> damageBar) {}

    public void setMyPositionID(String myPositionID) {}
}
