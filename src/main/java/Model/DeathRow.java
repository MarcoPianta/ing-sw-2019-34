package Model;

import java.util.ArrayList;

public class DeathRow {
    private int skulls;
    private ArrayList<Colors> murders;
    private Game gameId;

    public DeathRow(int n,Game gameId){
        skulls=n;
        murders=new ArrayList<>();
        this.gameId=gameId;
    }

    public ArrayList<Colors> getMurders() {
        return murders;
    }


}
