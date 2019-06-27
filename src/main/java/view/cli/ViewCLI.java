package view.cli;

import Model.CardPowerUp;
import Model.Colors;
import Model.NormalSquare;
import Model.Player;
import network.messages.*;
import view.View;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewCLI extends View {
    private static PrintWriter out=new PrintWriter(System.out,true);
    private static Scanner in=new Scanner(System.in);
    private int numberAction;
    private ActionCLI actionCLI;

    public int getNumberAction() {
        return numberAction;
    }
    public ViewCLI(){
        this.numberAction=1;
        actionCLI=new ActionCLI();
    }

    public static void main(String[] args) {
        ViewCLI viewCLI=new ViewCLI();
        viewCLI.showPowerUpChooseRespawn();
    }

    @Override
    public void showReachableSquares(List<String> squares) {
        out.println("you can reach these squares: \n");
        int i=1;
        for(String  s:squares){
            out.println(i+ "="+s +"t");
            i++;
        }
        out.println("choose a number from 1 to "+ squares.size() +"\n");
        boolean corrected=false;
        while(!corrected) {
            i=in.nextInt();
            if(i>=1 && i<=squares.size()){
                client.send(new MoveResponse(client.getToken(),squares.get(i-1)));
                corrected=true;
            }
            else{
                out.println("it's not difficult, you can do it \n");
                out.println("choose a number from 1 to "+ squares.size() +"\n" );
            }
        }

    }
    //aggiunngere show possible square e room
    @Override
    public void showPossibleTarget(List<Colors> targets) {
        out.println("you can shot these targets: \n");
        for(Colors c:targets){
            out.println(c +"\t");
        }
        out.println("\n choose a number from 0 to "+ targets.size() +"or z to cancel \n");
        //modificare perchè possono essere scelti più target
    }



    @Override
    public void showPowerUpChooseRespawn() {
        out.println("choose the power up to discard to spawn in that color:\n");
        int i=1;
        for(CardPowerUp p:powerUps){
            out.println(i +":"+p.getName()+", "+p.getColor()+ "\t");
            i++;
        }
        out.println("\n choose a number from 1 to "+ powerUps.size() +"\n" );
        boolean corrected=false;
        while(!corrected) {
            i=in.nextInt();
            if(i>=1 && i<=powerUps.size()){
                client.send(new RespawnMessage(client.getToken(),i-1));
                corrected=true;
            }
            else{
                out.println("it's not difficult, you can do it \n");
                out.println("choose a number from 1 to "+ powerUps.size() +"\n" );
            }
        }
    }
    //substituteweapons
    @Override
    public void showMessage(String message) {
        out.println(message);
    }

    public void showScopeRequest(ArrayList<Colors> playersId){
        out.println("\nYou can use Scope's powerUp\n press 1 to use scope or any key to cancel.\n");
        if(in.nextInt()==1){
            int i;
            if(playersId.size()==1){

            }
            else {
                for (i = 1; i <= playersId.size(); i++)
                    out.println(i + ":" + playersId.get(i - 1) + "\t");
                out.println("\n choose a number from 1 to" + playersId.size() + "\n");
                boolean corrected = false;
                while (!corrected) {
                    i = in.nextInt();
                    if (i >= 1 && i <= playersId.size()) {

                        corrected = true;
                    } else {
                        out.println("it's not difficult, you can do it \n");
                        out.println("choose a number from 1 to " + playersId.size() + "\n");
                    }
                }
            }
        }
    }

    @Override
    public void showVenomRequest(Colors player){
        ArrayList<Integer> powerUp=new ArrayList<>();
        out.println("\n"+ player+"attacked you, do you want revenge ??\n press 1 to use grenade or any key to cancel.\n");
        if (in.nextInt()==1){
            int i;
            for(i=0; i<powerUps.size();i++){
                if(powerUps.get(i).getWhen().equals("deal")) {
                    powerUp.add(i);
                }
            }
            if(powerUp.size()==1){
                //client.send(new UsePowerUp(client.getToken(),powerUp.get(0),,player));
                }
            else{
                for(i=1;i<=powerUp.size();i++){
                    out.println(i+"="+powerUps.get(powerUp.get(i-1))+"\t");
                }
                out.println("\n choose a number or 9 to cancel");
                boolean corrected=false;
                i=in.nextInt();
                while(!corrected){
                    if(i==1){
                        //client.send(new UsePowerUp(client.getToken(),powerUp.get(0),,player));
                        corrected=true;
                    }
                    else if(i==2){
                        //client.send(new UsePowerUp(client.getToken(),powerUp.get(1),,player));
                        corrected=true;
                    }
                    else if(i==3 && powerUp.size()==3){
                        //client.send(new UsePowerUp(client.getToken(),powerUp.get(1),,player));
                        corrected=true;
                    }
                    else if(i==9){
                        in.close();
                        corrected=true;
                    }
                    else
                        out.println("\n choose a  correct number or 9 to cancel");
                }
            }
        }
        else
            in.close();
    }

    @Override
    public void showGameSettingsRequest() {
        //TODO implement method
    }

    @Override
    public void endGame(boolean winner) {
        if(winner)
            out.println("The game is over.\nCongratulation, you won!");
        else
            out.println("The game is over.\nCongratulation, you didn't win!");
    }

    @Override
    public void startGame() {
        out.println("The game started, GOOD LUCK!!\n");
    }

    @Override
    public void startTurn() {
        numberAction=1;
        out.println("it's officially your turn \n");
        startActions();
    }

    @Override
    public void showToken() {
        out.println(client.getToken());
    }

    public void startActions(){
        out.println("Is the action number" +numberAction+"\n you can choose:\n");
        out.println("1:Move, 2:Shoot, 3:Grab, 4:Use power up 5:Reload and pass, 6:Pass \n type a number from 1 to 6 \n");
        boolean corrected=false;
        while(!corrected){
            int i=in.nextInt();
            if(i==1){
                client.send(new ReceiveTargetSquare(client.getToken(),"move"));
                corrected=true;
            }
            else if(i==2){
                actionCLI.actionShot();
                corrected=true;
            }
            else if(i==3){
                client.send(new ReceiveTargetSquare(client.getToken(),"grab"));
                corrected=true;
            }
            else if(i==4){
                actionCLI.actionPowerUp();
                corrected=true;
            }
            else if(i==5){
                //actionCLI.actionReload();
                corrected=true;
            }
            else if(i==6){
                client.send(new Pass(client.getToken()));
                corrected=true;
            }
            else{
                out.println("it's not difficult, you can do it \n");
                out.println("1:Move, 2:Shoot, 3:Grab, 4:Use power up 5:Reload and pass, 6:Pass \n type a number from 1 to 6 \n");
            }

        }
    }
    public void finalActions(){
        out.println("you have finished your turn you can recharge using some powerUp or pass\n");
        out.println("1:Use power up 2:Reload, 3:Pass \n type a number from 1 to 3 \n");
        boolean corrected=false;
        while(!corrected){
            int i=in.nextInt();
            if(i==1){
                actionCLI.actionPowerUp();
                corrected=true;
            }
            else if(i==2){
                //actionReload();
                corrected=true;
            }
            else if(i==3){
                client.send(new Pass(client.getToken()));
                corrected=true;
            }
            else{
                out.println("it's not difficult, you can do it \n");
                out.println("1:Use power up 2:Reload, 3:Pass \n type a number from 1 to 3 \n");
            }
        }
    }

    /*public  void endAction(Boolean executed){
        if(executed){
            out.println("the action was executed, the game was updated\n");
            numberAction++;
            if(numberAction==3)
                finalActions();
            else
                startActions();
        }
        else
            out.println("There was an error, please try again\n");
    }*/
}
