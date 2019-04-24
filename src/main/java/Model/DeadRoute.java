package Model;

import java.util.ArrayList;

public class DeadRoute {
    private int skulls;
    private ArrayList<UtilPlayer> murders;
    private Game gameId;

    public DeadRoute(int n, Game gameId){
        skulls=n;
        murders=new ArrayList<>();
        this.gameId=gameId;
    }

    public ArrayList<UtilPlayer> getMurders() {
        return murders;
    }

    public void addMurders(UtilPlayer player){
        getMurders().add(player);
        skulls--;
        if(skulls==0){
            //TODO ultimate adrenaline round
        }
    }


}
