package view.cli;

import Model.Action;
import Model.CardWeapon;
import network.messages.ReceiveTargetSquare;
import network.messages.ReloadMessage;
import network.messages.UsePowerUp;
import view.View;

import java.io.PrintWriter;
import java.util.Scanner;

public class ActionCLI {
    private ViewCLI viewCLI;
    private static PrintWriter out=new PrintWriter(System.out,true);
    private static Scanner in=new Scanner(System.in);

    public ActionCLI(ViewCLI viewCLI){
        this.viewCLI=viewCLI;
    }



    public void actionPowerUp(){
        if(powerUps.size()==0){
            out.println("\n you have no Power up \n");
            if(getNumberAction()==3)
                finalActions();
            else
                startActions();
        }
        else{
            int i;
            for ( i = 1; i <= powerUps.size(); i++)
                out.println(i + ":" + powerUps.get(i - 1)+ powerUps.get(i - 1).getColor() + "\t");
            out.println("\n choose a number from 1 to" + powerUps.size() + "or 9 to cancel\n");
            boolean corrected = false;
            while (!corrected) {
                i = in.nextInt();
                if (i >= 1 && i <= powerUps.size()) {
                    //if(powerUps.get(i-1).getMyMove().)
                    for ( i = 1; i <= players.size(); i++)
                        out.println(i + ":" +playersColor.get(i-1)+ "\t");

                    out.println("\n choose a number from 1 to" + playersColor.size() + "or 9 to cancel \n");
                    while (!corrected) {
                        int player= in.nextInt();
                        if (player >= 1 && i <= playersColor.size()) {
                            //client.send(new UsePowerUp(client.getToken(),i-1,client,playersColor.get(player-1),));
                            corrected = true;
                        }
                        else if(i==9) {
                            in.close();
                            corrected = true;
                            if (getNumberAction() == 3)
                                finalActions();
                            else
                                startActions();
                        }
                        else {
                            out.println("it's not difficult, you can do it \n");
                            out.println("choose a number from 1 to " + playersColor.size() + "\n");
                        }
                    }
                    //client.send(new UsePowerUp(client.getToken(),i-1,e,3));
                    corrected = true;
                }
                else if(i==9){
                    in.close();
                    corrected=true;
                    if(getNumberAction()==3)
                        finalActions();
                    else
                        startActions();

                }
                else {
                    out.println("it's not difficult, you can do it \n");
                    out.println("choose a number from 1 to " + powerUps.size() +"or 9 to cancel"+ "\n");
                }
            }

        }
    }
    public void actionShot(){
        if(viewCLI.getWeapons().size()==0){
            out.println("\n you have no weapon \n");
            viewCLI.startActions();
        }

        int count=1;
        for (CardWeapon weapon: viewCLI.getWeapons()){
            if(weapon.isCharge()){
                out.println(count + ":" + weapon.getName() + "\t");
                count++;
            }
        }
        out.println("\n choose a number from 1 to" + (count-1) + "or 9 to cancel\n");
        boolean corrected = false;
        while (!corrected) {
            int i;
            int posWeapon= in.nextInt();
            if (posWeapon >= 1 && posWeapon<= (count-1)) {
                for ( i = 1; i <= viewCLI.getWeapons().get(posWeapon-1).getEffects().size(); i++)
                    out.println(i + ":" + viewCLI.getWeapons().get(posWeapon-1).getActionSequences().get(i-1)+ "\t");
                out.println("\n choose a number from 1 to" + viewCLI.getWeapons().get(posWeapon-1).getEffects().size() + "or 9 to cancel\n");
                while (!corrected) {
                    int posEffect= in.nextInt();
                    if (posEffect >= 1 && i <=viewCLI.getWeapons().get(posWeapon-1).getActionSequences().size()) {
                        viewCLI.getClient().send(new ReceiveTargetSquare(viewCLI.getClient().getToken(),"shot",posWeapon,posEffect));
                        corrected = true;

                    }
                    else if(posWeapon==9){
                        corrected=true;
                        viewCLI.startActions();
                        in.close();
                    }
                    else {
                        out.println("it's not difficult, you can do it \n");
                        out.println("choose a number from 1 to " + viewCLI.getWeapons().get(posWeapon-1).getActionSequences().size() + "or 9 to cancel\n");
                    }
                }
            }
            else if(posWeapon==9){
                corrected=true;
                viewCLI.startActions();
                in.close();
            }
            else {
                out.println("it's not difficult, you can do it \n");
                out.println("choose a number from 1 to " + (count -1) + "or 9 to cancel\n");
            }
        }
    }
    public  void actionReload(){
        if(viewCLI.getWeapons().size()==0){
            out.println("\n you have no weapon \n");
            if(viewCLI.getNumberAction()>=3)
                viewCLI.finalActions();
            else
                viewCLI.startActions();
        }

        int count=1;
        for (CardWeapon weapon: viewCLI.getWeapons()){
            if(!weapon.isCharge()){
                out.println(count + ":" + weapon.getName() + "\t");
                count++;
            }
        }
        out.println("\n choose a number from 1 to" + (count-1) + "or 9 to cancel\n");
        boolean corrected=false;
        while(!corrected) {
            int i=in.nextInt();
            if(i>=1 && i<=(count-1)){
                viewCLI.getClient().send(new ReloadMessage(viewCLI.getClient().getToken(),i-1));
                corrected=true;
            }
            else{
                out.println("it's not difficult, you can do it ");
                out.println("choose a number from 1 to "+ (count-1)+"" );
            }
        }
    }

}
