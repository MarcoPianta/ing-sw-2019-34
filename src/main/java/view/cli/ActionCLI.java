package view.cli;

import Model.*;
import network.messages.ReceiveTargetSquare;
import network.messages.ReloadMessage;
import network.messages.UsePowerUp;
import network.messages.UsePowerUpResponse;
import view.View;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ActionCLI {
    private ViewCLI viewCLI;
    private static PrintWriter out=new PrintWriter(System.out,true);
    private static Scanner in=new Scanner(System.in);

    public ActionCLI(ViewCLI viewCLI){
        this.viewCLI=viewCLI;
    }



    public void actionPowerUp(){
        if(viewCLI.getPowerUps().isEmpty()){
            out.println("\n you have no Power up \n");
            if(viewCLI.getNumberAction()==3 && !viewCLI.isFinalTurn())
                viewCLI.finalActions();
            else if(viewCLI.isFinalTurn())
                viewCLI.finalTurnAction();
            else
                viewCLI.startActions();

        }
        else{
            int i;
            for ( i = 1; i <= viewCLI.getPowerUps().size(); i++)
                out.println(i + ":" +viewCLI.getPowerUps().get(i - 1).getName() +" "+ viewCLI.getPowerUps().get(i - 1).getColor() + "\t");
            out.println("\n choose a number from 1 to" + viewCLI.getPowerUps().size() + "or 9 to cancel\n");
            boolean corrected = false;
            while (!corrected) {
                i = in.nextInt();
                if (i >= 1 && i <= viewCLI.getPowerUps().size()) {
                    if (viewCLI.getPowerUps().get(i-1).getOtherMove()==2)
                        newtonPowerUp(i);
                    else if(viewCLI.getPowerUps().get(i-1).getMyMove()==-1)
                        teletrasporterPowerUp(i);
                    else{
                        out.println("The powerUp selected is tagBack grenade o targetting Scope, the game will tell you when to use them");
                        if(viewCLI.getNumberAction()==3 && !viewCLI.isFinalTurn())
                            viewCLI.finalActions();
                        else if(viewCLI.isFinalTurn())
                            viewCLI.finalTurnAction();
                        else
                            viewCLI.startActions();

                    }
                    corrected = true;
                }
                else if(i==9)
                    corrected=true;
                else {
                    out.println("it's not difficult, you can do it \n");
                    out.println("choose a number from 1 to " + viewCLI.getPowerUps() +"or 9 to cancel"+ "\n");
                }
            }

        }
    }

    private void  newtonPowerUp(int posPowerUp){
        //venom
        ArrayList<Colors> colorsPlayer=new ArrayList<>();
        ArrayList<NormalSquare> squares=new ArrayList<>();
        int count=1;
        out.println("Choose the TARGET for NEWTON");
        for (Colors color:viewCLI.getPlayers().keySet()){
            out.print(count + ":" +color + "\t");
            colorsPlayer.add(color);
            count++;

        }
        out.println(" choose a number from 1 to" + viewCLI.getPlayers().keySet().size());
        boolean corrected = false;
        while (!corrected) {
            int target= in.nextInt();
            if (target >= 1 && target <= viewCLI.getPowerUps().size()) {
                out.println("Choose a square, the square must be at maximum distance 2 from the target \n" +
                        "and in cardinal position");
                out.println(" choose a number a square id, insert string for example 0,0" );
                while (!corrected) {
                    String square= in.next();
                    if (square.equals("0,0") || square.equals("0,1")||square.equals("0,2") || square.equals("1,0") ||square.equals("1,1") || square.equals("1,2")||square.equals("1,3") || square.equals("2,2")||square.equals("2,3") || square.equals("2,1")) {
                        viewCLI.getClient().send(new UsePowerUpResponse(viewCLI.getClient().getToken(),posPowerUp-1,viewCLI.getClient().getToken(),colorsPlayer.get(target-1),square));
                        corrected=true;
                    }
                    else{
                        out.println("it's not difficult, you can do it");
                        out.println("choose a string");
                    }
                }

            }

            else {
                out.println("it's not difficult, you can do it \n");
                out.println("choose a number from 1 to " + viewCLI.getPlayers().keySet().size());
            }
        }
    }
    private void teletrasporterPowerUp(int posPowerUp){
        ArrayList<NormalSquare> squares=new ArrayList<>();
        out.println("choose any square where to transport yourself, have a good trip");

        boolean corrected=false;
        out.println(" choose a number a square id, insert string for example 1,0" );
        while (!corrected) {
            String square= in.next();
            if (square.equals("0,1")||square.equals("0,0") ||square.equals("0,2") || square.equals("1,0") ||square.equals("1,1") || square.equals("1,2")||square.equals("1,3") || square.equals("2,2")||square.equals("2,3") || square.equals("2,1")) {
                viewCLI.getClient().send(new UsePowerUpResponse(viewCLI.getClient().getToken(),posPowerUp-1,viewCLI.getClient().getToken(),Colors.BLUE,square));
                corrected=true;
            }
            else{
                out.println("it's not difficult, you can do it");
                out.println("choose a string");
            }
        }
    }

    public void actionShot(){

        if(viewCLI.getWeapons().isEmpty()){
            out.println("\n you have no weapon \n");
            if(viewCLI.getNumberAction()>=3 && !viewCLI.isFinalTurn())
                viewCLI.finalActions();
            else if(viewCLI.isFinalTurn())
                viewCLI.finalTurnAction();
            else
                viewCLI.startActions();
        }
        int count=1;
        for (CardWeapon weapon: viewCLI.getWeapons()){
                out.println(count + ":" + weapon.getName() + "\t");
                count++;
            }
        out.println("\n choose a number from 1 to" + (count-1) + "or 9 to cancel\n");
        boolean corrected = false;
        while (!corrected) {
            int i;
            int posWeapon= in.nextInt();
            if (posWeapon >= 1 && posWeapon<= (count-1)) {
                for ( i = 1; i <= viewCLI.getWeapons().get(posWeapon-1).getEffects().size(); i++)
                    out.println(i + ":" + viewCLI.getWeapons().get(posWeapon-1).getActionSequences().get(i-1)+ "\t");
                out.println("\n choose a number from 1 to" + viewCLI.getWeapons().get(posWeapon-1).getEffects().size()+"\t");
                while (!corrected) {
                    int posEffect= in.nextInt();
                    if (posEffect >= 1 && i <=viewCLI.getWeapons().get(posWeapon-1).getActionSequences().size()) {
                        viewCLI.getClient().send(new ReceiveTargetSquare(viewCLI.getClient().getToken(),"shot",posWeapon,posEffect));
                        corrected = true;
                    }
                    else {
                        out.println("it's not difficult, you can do it \n");
                        out.println("choose a number from 1 to " + viewCLI.getWeapons().get(posWeapon-1).getActionSequences().size()+"\t");
                    }
                }
            }
            else if(posWeapon==9){
                corrected=true;
                if(viewCLI.getNumberAction()>=3 && !viewCLI.isFinalTurn())
                viewCLI.finalActions();
                        else if(viewCLI.isFinalTurn())
                    viewCLI.finalTurnAction();
                else
                    viewCLI.startActions();

            }
            else {
                out.println("it's not difficult, you can do it \n");
                out.println("choose a number from 1 to " + (count -1) + "or 9 to cancel\n");
            }
        }
    }
    public void shotFinalTurn(int posWeapon){
        int i;
        for ( i = 1; i <= viewCLI.getWeapons().get(posWeapon-1).getEffects().size(); i++)
            out.println(i + ":" + viewCLI.getWeapons().get(posWeapon-1).getActionSequences().get(i-1)+ "\t");
        out.println("\n choose a number from 1 to" + viewCLI.getWeapons().get(posWeapon-1).getEffects().size());
        boolean  corrected=false;
        while (!corrected) {
            int posEffect= in.nextInt();
            if (posEffect >= 1 && i <=viewCLI.getWeapons().get(posWeapon-1).getActionSequences().size()) {
                viewCLI.getClient().send(new ReceiveTargetSquare(viewCLI.getClient().getToken(),"shot",posWeapon,posEffect));
                corrected = true;

            }
            else {
                out.println("it's not difficult, you can do it \n");
                out.println("choose a number from 1 to " + viewCLI.getWeapons().get(posWeapon-1).getActionSequences().size());
            }
        }
    }

    public  int actionReload(){
        if(!viewCLI.isFinalTurn())
            viewCLI.setNumberAction(3);
        if(viewCLI.getWeapons().isEmpty()){
            out.println("\n you have no weapon for reload\n");
            viewCLI.finalActions();
        }

        int count=1;
        for (CardWeapon weapon: viewCLI.getWeapons()){
            if(!weapon.isCharge()){
                out.println(count + ":" + weapon.getName() + "\t");
                count++;
            }
        }
        int i;
        out.println("\n choose a number from 1 to" + (count-1) + "or 9 to cancel\n");
        boolean corrected=false;
        while(!corrected) {
            i=in.nextInt();
            if(i>=1 && i<=(count-1)){
                viewCLI.getClient().send(new ReloadMessage(viewCLI.getClient().getToken(),i-1));
                return i-1;
            }
            else if(i==9)
                    corrected=true;
            else{
                out.println("it's not difficult, you can do it ");
                out.println("choose a number from 1 to "+ (count-1)+"" );
            }
        }
        return 0;
    }

}
