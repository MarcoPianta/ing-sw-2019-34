package view.cli;

import Model.*;
import network.Client.RMI.RMIClient;
import network.Client.Socket.SocketClient;
import network.messages.*;
import view.View;

import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ViewCLI extends View {
    private static PrintWriter out=new PrintWriter(System.out,true);
    private static Scanner in=new Scanner(System.in);
    private int myPoint;
    private HashMap<String, CardWeapon[]> spawnSquareWeapon;




    private ActionCLI actionCLI;

    public ViewCLI(){
        actionCLI=new ActionCLI(this);
        out.println("what you want to use?? 1=Socket 2=RMI");
        boolean corrected=false;
        while(!corrected) {
            int i=in.nextInt();
            if(i==1){
                client= new SocketClient("localhost",10000,this);
                corrected=true;
            }
            else if(i==2){
                client= new RMIClient("localhost",10000,this);
                corrected=true;

            }
            else{
                out.println("it's not difficult, you can do it ");
                out.println("choose a number from 1 to "+ 3);
            }
        }
        myPoint=0;
        spawnSquareWeapon=new HashMap<>();
        spawnSquareWeapon.put("0,2",new CardWeapon[3]);
        spawnSquareWeapon.put("1,0",new CardWeapon[3]);
        spawnSquareWeapon.put("2,3",new CardWeapon[3]);
    }

    public static void main(String[] args) {
        ViewCLI viewCLI=new ViewCLI();

    }

    @Override
    public void chatMessage(String message) {

    }

    public void setMyTurn(boolean myTurn) {

    }



    @Override
    public void finalTurn() {

    }


    @Override
    public void grabWeaponRequest() {
        out.println("you can choose one of this weapon to grab");
        int i=1;

        for(CardWeapon weapon :spawnSquareWeapon.get(myPositionID)){
            out.println(1+ "="+weapon.getName() +"\t");
            i++;
        }
        out.println("choose a number from 1 to "+ 3 +"");
        boolean corrected=false;
        while(!corrected) {
            i=in.nextInt();
            if(i>=1 && i<=3){
                client.send(new GrabWeapon(client.getToken(),i-1));
                corrected=true;
            }
            else{
                out.println("it's not difficult, you can do it ");
                out.println("choose a number from 1 to "+ 3);
            }
        }
    }

    @Override
    public void substituteWeaponRequest() {
        out.println("you have a four weapon, choose the weapon that you want to eliminate");
        int c=1;
        for(CardWeapon weapon:weapons){
            out.println(c+ "="+weapon.getName() +"\t");
            c++;
        }
        out.println("choose a number from 1 to "+ weapons.size() +"");
        boolean corrected=false;
        while(!corrected) {
            int i=in.nextInt();
            if(i>=1 && i<=weapons.size()){
                client.send(new SubstituteWeaponResponse(client.getToken(),i-1));
                corrected=true;
            }
            else{
                out.println("it's not difficult, you can do it ");
                out.println("choose a number from 1 to "+ weapons.size() +"" );
            }
        }

    }

    @Override
    public void showReachableSquares(List<String> squares) {
        out.println("you can reach these squares: ");
        int i=1;
        for(String  s:squares){
            out.println(i+ "="+s +"\t");
            i++;
        }
        out.println("choose a number from 1 to "+ squares.size() +"");
        boolean corrected=false;
        while(!corrected) {
            i=in.nextInt();
            if(i>=1 && i<=squares.size()){
                client.send(new MoveResponse(client.getToken(),squares.get(i-1)));
                corrected=true;
            }
            else{
                out.println("it's not difficult, you can do it ");
                out.println("choose a number from 1 to "+ squares.size() +"" );
            }
        }

    }

    @Override
    public void showPossibleRooms(List<String> ids) {
            out.println("you can a shoot a one room, choose a square with color of room: ");
            int i=1;
            for(String  id:ids){
                out.println(i+ "="+id +"\t");
                i++;
            }
            out.println("choose a number from 1 to "+ ids.size() +"");
            boolean corrected=false;
            while(!corrected) {
                i=in.nextInt();
                if(i>=1 && i<=ids.size()){
                    client.send(new ShootResponser(client.getToken(),ids.get(i-1)));
                    corrected=true;
                }
                else{
                    out.println("it's not difficult, you can do it ");
                    out.println("choose a number from 1 to "+ ids.size() +"" );
                }
            }
    }


    @Override
    public void showPossibleSquares(List<String> targets) {
        out.println("you can a shoot a one of this square: ");
        int i=1;
        for(String  s:targets){
            out.println(i+ "="+s+"\t");
            i++;
        }
        out.println("choose a number from 1 to "+ targets.size() +"");
        boolean corrected=false;
        while(!corrected) {
            i=in.nextInt();
            if(i>=1 && i<=targets.size()){
                client.send(new ShootResponses(client.getToken(),targets.get(i-1)));
                corrected=true;
            }
            else{
                out.println("it's not difficult, you can do it ");
                out.println("choose a number from 1 to "+ targets.size() +"" );
            }
        }

    }

    @Override
    public void showPossibleTarget(List<Colors> targets,int i) {
        boolean esc=false;
        out.println("you can shot these targets: ");
        for(Colors c:targets){
            out.println(c +"\t");
        }
        out.println(" choose a number from 0 to "+ targets.size() +"or z to cancel ");
        out.println("choose max"+i+"target and press 9 when terminate");
        int count=0;
        List<Colors> targetChoose=new ArrayList();
        while(count<i || !esc) {
            i=in.nextInt();
            if(i>=1 && i<=targets.size()){
                targetChoose.add(targets.get(i-1));
                count++;
            }
            else if(i==9){
                esc=true;
            }
            else{
                out.println("it's not difficult, you can do it ");
                out.println("choose a number from 1 to "+ targets.size() +"" );
                out.println("choose max"+i+"target and press 9 when terminate");
            }
        }
        client.send(new ShootResponsep(client.getToken(),targetChoose));
    }

    @Override
    public void payment(Payment message) {

    }

    @Override
    public void showTargetMove(List<String> targets) {
        out.println("you can move the the target in a one of this square: ");
        int i=1;
        for(String  s:targets){
            out.println(i+ "="+s+"\t");
            i++;
        }
        out.println("choose a number from 1 to "+ targets.size() +"");
        boolean corrected=false;
        while(!corrected) {
            i=in.nextInt();
            if(i>=1 && i<=targets.size()){
                client.send(new TargetMoveResponse(client.getToken(),targets.get(i-1)));
                corrected=true;
            }
            else{
                out.println("it's not difficult, you can do it ");
                out.println("choose a number from 1 to "+ targets.size() +"" );
            }
        }
    }

    @Override
    public void updateEnemiesDamageBar(List<Colors> damageBar,List<Colors> marks, Colors player) {
        out.println(player+"was attacked and his damagedBar was changed:" );
        for(Colors color:damageBar)
            out.println(color+",");
        out.println("");
    }

    @Override
    public void addMarks(ArrayList<Colors> marks, Colors player) {
        out.println(player+"was attacked and his mark list was changed:" );
        for(Colors color:damageBar)
            out.println(color+",");
        out.println("");
    }

    @Override
    public void showPowerUpChooseRespawn() {
        out.println("choose the power up to discard to spawn in that color:");
        int c=1;
        for(CardPowerUp p:powerUps){
            out.println(c +":"+p.getName()+", "+p.getColor()+ "\t");
            c++;
        }
        out.println(" choose a number from 1 to "+ powerUps.size() +"" );
        boolean corrected=false;
        while(!corrected) {
            int i=in.nextInt();
            if(i>=1 && i<=powerUps.size()){
                client.send(new RespawnMessage(client.getToken(),i-1));
                corrected=true;
            }
            else{
                out.println("it's not difficult, you can do it ");
                out.println("choose a number from 1 to "+ powerUps.size() +"" );
            }
        }
        startActions();
    }

    @Override
    public void showMessage(String message) {
        out.println(message);
    }


    @Override
    public void showScoopRequest() {
        out.println("You can use Scope's powerUp press 1 to use scope or any key to cancel.");
        if(in.nextInt()==1){
            int i;
            for (i = 1; i <= powerUps.size(); i++)
                out.println(i + ":" + powerUps.get(i - 1) + "\t");
            out.println(" choose a number from 1 to" + powerUps.size() + "");
            boolean corrected = false;
            while (!corrected) {
                i = in.nextInt();
                if (i >= 1 && i <= powerUps.size()) {
                    client.send(new CanUseScoopResponse(client.getToken(), true,i-1));
                    corrected = true;
                } else {
                    out.println("it's not difficult, you can do it ");
                    out.println("choose a number from 1 to " + powerUps.size() + "");
                }
            }
        }
        else{
            client.send(new CanUseScoopResponse(client.getToken(), false,-1));
        }

    }

    @Override
    public void chooseScopeTarget(ArrayList<Colors> targets){
        out.println("choose a target for scope");
        int i;
        for (i = 1; i <= targets.size(); i++)
            out.println(i + ":" + targets.get(i - 1) + "\t");
        out.println(" choose a number from 1 to" + targets.size() + "");
        boolean corrected = false;
        while (!corrected) {
            i = in.nextInt();
            if (i >= 1 && i <= targets.size()) {
                client.send(new ScopeTargetResponse(client.getToken(), targets.get(i-1)));
                corrected = true;
            } else {
                out.println("it's not difficult, you can do it ");
                out.println("choose a number from 1 to " + targets.size() + "");
            }
        }

    }

    @Override
    public void showVenomRequest(Colors player){
        ArrayList<Integer> powerUp=new ArrayList<>();
        out.println(""+ player+"attacked you, do you want revenge ?? press 1 to use grenade or any key to cancel.");
        if (in.nextInt()==1){
            int i;
            for(i=0; i<powerUps.size();i++){
                if(powerUps.get(i).getWhen().equals("deal")) {
                    powerUp.add(i);
                }
            }
            if(powerUp.size()==1){
                client.send(new UsePowerUpResponse(client.getToken(),powerUp.get(0),client.getToken(),player,null));
                }
            else{
                for(i=1;i<=powerUp.size();i++){
                    out.println(i+"="+powerUps.get(powerUp.get(i-1))+"\t");
                }
                out.println(" choose a number or 9 to cancel");
                boolean corrected=false;
                i=in.nextInt();
                while(!corrected){
                    if(i==1 || (i==2 && powerUp.size()==2)||(i==3 && powerUp.size()==3)){
                        client.send(new UsePowerUpResponse(client.getToken(),powerUp.get(i-1),client.getToken(),player,null));
                        corrected=true;
                    }
                    else if(i==9){
                        in.close();
                        corrected=true;
                    }
                    else
                        out.println(" choose a  correct number or 9 to cancel");
                }
            }
        }
        else
            in.close();
    }

    @Override
    public void showGameSettingsRequest() {
    }

    @Override
    public void endGame(boolean winner) {
        if(winner)
            out.println("The game is over.Congratulation, you won!");
        else
            out.println("The game is over.You didn't win!");
    }


    @Override
    public void setBlueAmmo(int blueAmmo) {
        super.setBlueAmmo(blueAmmo);
        out.print("\n You have ammo blue="+blueAmmo);
    }

    @Override
    public void setRedAmmo(int redAmmo) {
        super.setRedAmmo(redAmmo);
        out.print("\n You have ammo red="+redAmmo);
    }

    @Override
    public void setPowerUps(ArrayList<CardPowerUp> powerUps) {
        this.setPowerUps(powerUps);
        out.print("\n You grab a new POWER UP="+powerUps.get(powerUps.size()-1));
        out.print("\n Yours powerUp are: \t");
        for(CardPowerUp powerUp:powerUps)
            out.print(powerUp +"\n");
    }

    @Override
    public void setWeapons(ArrayList<CardWeapon> weapons) {
        this.setWeapons(weapons);
        out.print("\n You grab a new POWER UP="+weapons.get(weapons.size()-1));
        out.print("\n Yours weapons are: \t");
        for(CardWeapon weapon :weapons)
            out.print(weapon +"\n");
    }

    @Override
    public void setYellowAmmo(int yellowAmmo) {
        super.setYellowAmmo(yellowAmmo);
        out.print("\n You have ammo yellow="+yellowAmmo);
    }

    @Override
    public void startGame(String map) {
        out.println("The game started, GOOD LUCK!!");
    }

    @Override
    public void startTurn() {
        out.println("it's officially your turn ");
        startActions();
    }

    @Override
    public void showToken() {
        out.println(client.getToken());
    }

    public void startActions(){
        out.println("Is the action number" +getNumberAction()+" you can choose:");
        out.println("1:Move, 2:Shoot, 3:Grab, 4:Use power up 5:Reload and pass, 6:Pass  type a number from 1 to 6 ");
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
                actionCLI.actionReload();
                corrected=true;
            }
            else if(i==6){
                client.send(new Pass(client.getToken()));
                corrected=true;
            }
            else{
                out.println("it's not difficult, you can do it ");
                out.println("1:Move, 2:Shoot, 3:Grab, 4:Use power up 5:Reload and pass, 6:Pass  type a number from 1 to 6 ");
            }

        }
    }
    public void finalActions(){
        out.println("you have finished your turn you can recharge using some powerUp or pass");
        out.println("1:Use power up 2:Reload, 3:Pass  type a number from 1 to 3 ");
        boolean corrected=false;
        while(!corrected){
            int i=in.nextInt();
            if(i==1){
                actionCLI.actionPowerUp();
                corrected=true;
            }
            else if(i==2){
                actionCLI.actionReload();
                corrected=true;
            }
            else if(i==3){
                client.send(new Pass(client.getToken()));
                corrected=true;
            }
            else{
                out.println("it's not difficult, you can do it ");
                out.println("1:Use power up 2:Reload, 3:Pass  type a number from 1 to 3 ");
            }
        }
    }


    @Override
    public void setMyPositionID(String myPositionID) {
        this.myPositionID=myPositionID;
        out.println("your new Position is" +myPositionID);
    }

    @Override
    public void updateEnemyPosition(Colors player, String position) {
        out.println(player +"has moved in"+position +"" );
    }


    @Override
    public void fillSquare(String squareID, CardAmmo ammo){
        out.println(squareID+":"+ammo.getName()+"");
    }
    @Override
    public void fillSpawn(String squareID, int position, CardWeapon weapon){
        out.println(squareID+":"+"position" +position+weapon.getName()+"\n" );
        spawnSquareWeapon.get(squareID)[position] = weapon;

    }
    @Override
    public void setOtherPosition(Colors player, String position) {
        out.println(player+"position:" +position+"" );
    }
    @Override
    public void setPoints(int points) {
        myPoint+=points;
        out.println("you have gained  "+points+" points han you have "+myPoint+"points");
    }
}
