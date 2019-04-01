package Model;

import java.util.ArrayList;

public abstract class Action {
    private ArrayList<Integer> damage;
    private ArrayList<Player> targetPlayer;

    public Action() {

    }
    public void runAction(int effect, int player)() {
        return null;
    }

    public ArrayList<Integer> getDamage() {
        return damage;
    }

    public ArrayList<Player> getTargetPlayer() {
        return targetPlayer;
    }

}
