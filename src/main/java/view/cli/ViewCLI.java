package view.cli;

import view.View;

import java.util.List;

public class ViewCLI extends View {
    private final String horizontalWall = "____________________";
    private final String horizontalDoor = "_______      _______";
    private final String leftWall = "|";
    private final String rightWall      = "                    |";

    @Override
    public void showReachableSquares(List<String> squares) {
        //TODO implement method
    }

    @Override
    public void showPossibleTarget(List<String> targets) {
        //TODO implement method
    }

    @Override
    public void showPowerUpChooseRespawn() {
        //TODO implement method
    }

    @Override
    public void showMessage(String message) {
        //TODO implement method
    }

    @Override
    public void showVenomRequest() {
        //TODO implement method
    }

    @Override
    public void showGameSettingsRequest() {
        //TODO implement method
    }

    @Override
    public void endGame(boolean winner) {
        //TODO implement method
    }

    @Override
    public void startGame() {

    }

    @Override
    public void startTurn() {

    }

    @Override
    public void showToken() {
        //TODO implement method
    }
}
