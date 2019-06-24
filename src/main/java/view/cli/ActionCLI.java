package view.cli;

import network.messages.UsePowerUp;

import java.io.PrintWriter;
import java.util.Scanner;

public class ActionCLI extends ViewCLI {
    private static PrintWriter out=new PrintWriter(System.out,true);
    private static Scanner in=new Scanner(System.in);



    /*public void actionUsePowerUp(){
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
            out.println("\n choose a number from 1 to" + powerUps.size() + "\n");
            boolean corrected = false;
            while (!corrected) {
                i = in.nextInt();
                if (i >= 1 && i <= powerUps.size()) {
                    for ( i = 1; i <= players.size(); i++)
                        out.println(i + ":" +players.get(i-1)+ "\t");
                    out.println("\n choose a number from 1 to" + player.size() + "\n");
                    while (!corrected) {
                        int player= in.nextInt();
                        if (i >= 1 && i <= players.size()) {

                            client.send(new UsePowerUp(client.getToken(),i-1,client,players.get(player-1),));
                            corrected = true;
                        } else {
                            out.println("it's not difficult, you can do it \n");
                            out.println("choose a number from 1 to " + players.size() + "\n");
                        }
                    }
                    client.send(new UsePowerUp(client.getToken(),i-1,e,3));
                    corrected = true;
                } else {
                    out.println("it's not difficult, you can do it \n");
                    out.println("choose a number from 1 to " + powerUps.size() + "\n");
                }
            }

        }

    }
    */
}
