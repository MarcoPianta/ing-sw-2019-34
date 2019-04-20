package Model;

import java.util.ArrayList;

public class DeadRoute {
    private int skulls;
    private ArrayList<Colors> murders;
    private Game gameId;

    public DeadRoute(int n, Game gameId){
        skulls=n;
        murders=new ArrayList<>();
        this.gameId=gameId;
    }

    public ArrayList<Colors> getMurders() {
        return murders;
    }


}
