package view.cli;

import Model.*;
import network.Client.RMI.RMIClient;
import network.Client.Socket.SocketClient;
import network.messages.*;
import view.View;

import java.io.PrintWriter;
import java.util.*;

public class ViewCLI extends View {
    private static PrintWriter out=new PrintWriter(System.out,true);
    private static Scanner in=new Scanner(System.in);
    private int myPoint;
    private HashMap<String, CardWeapon[]> spawnSquareWeapon;
    private boolean finalTurn;
    private boolean update=false;
    private boolean grab=false;
    private boolean move=false;
    private boolean waitUpdate=false;
    private boolean shot=false;




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
        finalTurn=false;
    }

    public boolean isFinalTurn() {
        return finalTurn;
    }

    public static void main(String[] args) {
            new ViewCLI();
    }

    @Override
    public void chatMessage(String message) {

    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn=myTurn;
    }



    @Override
    public void finalTurn() {
        out.println("finally is the final turn");
        finalTurn=true;
    }


    @Override
    public void grabWeaponRequest() {
        out.println("you can choose one of this weapon to grab");
        int i=1;

        for(CardWeapon weapon :spawnSquareWeapon.get(myPositionID)){
            out.println(i+ "="+weapon.getName() +"\t");
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
                if(move && !grab){
                    numberAction++;
                    //move=false;
                    client.send(new MoveResponse(client.getToken(),squares.get(i-1)));
                }
                else if(grab && (squares.get(i-1).equals("0,2")||squares.get(i-1).equals("1,0")||squares.get(i-1).equals("2,3"))) {
                    client.send(new MoveResponse(client.getToken(),squares.get(i-1)));
                }
                else {
                    numberAction++;
                    client.send(new MoveResponse(client.getToken(),squares.get(i-1)));
                }

                corrected=true;
            }
            else{
                out.println("it's not difficult, you can do it ");
                out.println("choose a number from 1 to "+ squares.size() +"" );
            }
        }
        if(move){
            if(numberAction>=3 && !finalTurn)
                finalActions();
            else if(finalTurn)
                finalTurnAction();
            else
                startActions();
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
    public void setNumberAction(int numberAction) {
        this.numberAction=numberAction;
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
        out.println(" choose a number from 0 to "+ targets.size());
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
        out.println("you must pay: red= "+message.getCost()[0]+"  yellow= "+message.getCost()[1]+"  blue= "+message.getCost()[2]);
        out.println(" PRESS 1 if you want to pay the effect with powerUp, or another number if you wan't");
        ArrayList<Integer> powerUp= new ArrayList<>();
        boolean corrected=false;
        while(!corrected) {
            int i=in.nextInt();
            if(i==1){
                boolean esc=false;
                int count=1;
                for ( CardPowerUp card: powerUps){
                    out.println(count + ":" +card.getName() +"  "+ card.getColor() + "\t");
                    count++;
                }
                out.println("\n choose a number from 1 to" + count+ "or 9 to esc(you can use more powerUp)\n");
                while(!esc) {
                    i=in.nextInt();
                    if(i>=1 && i<=powerUps.size()){
                        out.println("you have choose a powerUp to pay");
                        powerUp.add(i-1);

                    }
                    else if(i==9){
                        esc=true;
                    }
                    else{
                        out.println("it's not difficult, you can do it ");
                        out.println("\n choose a number from 1 to" + count + "or 9 to esc(you can use more powerUp)\n");
                    }
                    if(!esc)
                        System.out.println("choose another powerUp");

                }
                corrected=true;
            }
            else if(i>1)
                corrected=true;
            else{
                out.println("it's not difficult, you can do it ");
                out.println(" PRESS 1 if you want to pay the effect with powerUp, or another number if you wan't");
            }
        }
        corrected=false;
        if (message.getPowerUp()!=-1){
            out.println(" PRESS 1 if you want to pay the scope with powerUp, 2 if you don't want\n");
            while(!corrected) {
                int i=in.nextInt();
                if(i==1){
                    paymentWithScope(message.getPowerUp(),powerUp,message.getCost());
                    corrected=true;
                }
                else if(i==2){
                    paymentWithScope2(message.getPowerUp(),powerUp,message.getCost());
                    corrected=true;
                }

                else{
                    out.println("it's not difficult, you can do it ");
                    out.println(" PRESS 1 if you want to pay the scope with powerUp, 2 if you don't want\n");
                }
            }
        }
        else {
            numberAction++;
            if(powerUp.isEmpty())
                client.send(new PaymentResponse(client.getToken(), powerUp, false, message.getCost(), false));
            else
                client.send(new PaymentResponse(client.getToken(), powerUp, true, message.getCost(), false));
        }

    }
    private void paymentWithScope(Integer posPowerUp,ArrayList<Integer> paymentPowerUp,Integer[] cost){
        int count=1;
        for ( CardPowerUp p:powerUps){
            if((powerUps.indexOf(p)!=posPowerUp && !paymentPowerUp.contains(powerUps.indexOf(p)))) {
                out.print(count + ":" + p.getName()+" " + p.getColor() + "\t");
            }
            else
                out.print(count + ": NOPOWERUP");
            count++;

        }
        if(count!=1) {
            out.println(" choose a number from 1 to" + count);
            boolean corrected=false;
            while (!corrected) {
                int powerUpScope = in.nextInt();
                if (powerUpScope >= 1 && powerUpScope <= count) {
                    if(paymentPowerUp.isEmpty()) {
                        numberAction++;
                        client.send(new PaymentResponse(client.getToken(), paymentPowerUp, false, cost, true, "", powerUpScope));

                    }
                    else {
                        numberAction++;
                        client.send(new PaymentResponse(client.getToken(), paymentPowerUp, true, cost, true, "", powerUpScope));

                    }
                    corrected = true;
                } else {
                    out.println("it's not difficult, you can do it ");
                    out.println(" choose a number from 1 to" + count);
                }
            }

        }
        else
            paymentWithScope2(posPowerUp,paymentPowerUp,cost);

    }
    private void paymentWithScope2(Integer posPowerUp,ArrayList<Integer> paymentPowerUp,Integer[] cost){
        out.println("You must pay scope with ammo, press r for red, y for yellow, b for blue");
        boolean corrected=false;
        while (!corrected) {
            String ammo = in.next();
            if (ammo.equals("r")||ammo.equals("y")||ammo.equals("b")) {
                if(paymentPowerUp.isEmpty())
                    client.send(new PaymentResponse(client.getToken(), paymentPowerUp,false, cost, true,ammo,-1));
                else
                    client.send(new PaymentResponse(client.getToken(), paymentPowerUp,true, cost, true,ammo,-1 ));
                corrected = true;
            } else {
                out.println("it's not difficult, you can do it ");
                out.println("You must pay scope with ammo, press r for red, y for yellow, b for blue");
            }
        }
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
        out.println("\nchoose the power up to discard to spawn in that color:");
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
                waitUpdate=true;
                client.send(new RespawnMessage(client.getToken(),i-1));
                corrected=true;
            }
            else{
                out.println("it's not difficult, you can do it ");
                out.println("choose a number from 1 to "+ powerUps.size() +"" );
            }
        }

    }

    @Override
    public void showMessage(String message) {
        out.println("\n"+message);
        if(message.equals("Action not done") && myPositionID!=null && !(grab && (myPositionID.equals("0,2")||myPositionID.equals("1,0")||myPositionID.equals("2,3")))){
            if(numberAction>=3 && !finalTurn)
                finalActions();
            else if(finalTurn)
                finalTurnAction();
            else
                startActions();

        }
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
    public void startGame(String map) {
        out.println("The game started, GOOD LUCK!!");
    }

    @Override
    public void startTurn() {
        out.println("it's officially your turn ");
        myTurn=true;
        numberAction=1;
        if(myPositionID!=null){
            if(finalTurn)
                finalTurnAction();
            else
                startActions();
        }
    }

    @Override
    public void showToken() {
        out.println(client.getToken());
    }

    public void startActions(){
        if(myTurn){
            update=false;
            grab=false;
            move=false;
            shot=false;
            out.println("\nyou can choose:");
            out.println("1:Move, 2:Shoot, 3:Grab, 4:Use power up 5:Reload and pass, 6:Pass  type a number from 1 to 6 ");
            boolean corrected=false;
            while(!corrected){
                int i=in.nextInt();
                if(i==1){
                    move=true;
                    client.send(new ReceiveTargetSquare(client.getToken(),"move"));
                    corrected=true;
                }
                else if(i==2){
                    shot=true;
                    actionCLI.actionShot();
                    corrected=true;
                }
                else if(i==3){
                    grab=true;
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
                    setMyTurn(false);
                    corrected=true;
                }
                else{
                    out.println("it's not difficult, you can do it ");
                    out.println("1:Move, 2:Shoot, 3:Grab, 4:Use power up 5:Reload and pass, 6:Pass  type a number from 1 to 6 ");
                }
            }
        }
    }
    public void finalActions(){
        if (myTurn){
            update=false;
            grab=false;
            move=false;
            shot=false;
            out.println("\nyou have finished your turn you can recharge using some powerUp or pass");
            out.println("1:Use power up 2:Reload, 3:Pass  type a number from 1 to 3 ");
            boolean corrected=false;
            while(!corrected) {
                int i = in.nextInt();
                if (i == 1) {
                    actionCLI.actionPowerUp();
                    corrected = true;
                } else if (i == 2) {
                    actionCLI.actionReload();
                    corrected = true;
                } else if (i == 3) {
                    client.send(new Pass(client.getToken()));
                    setMyTurn(false);
                    corrected = true;
                } else {
                    out.println("it's not difficult, you can do it ");
                    out.println("1:Use power up 2:Reload, 3:Pass  type a number from 1 to 3 ");
                }
            }
        }
    }

    public void finalTurnAction(){
        if(myTurn){
            grab=false;
            update=false;
            move=false;
            shot=false;
            out.println("\nyou can choose: ");
            out.println("1:Move(don't press if you play after first player), 2:Shoot, 3:Grab, 4:Use power up  5:Pass  type a number from 1 to 5 ");
            boolean corrected=false;
            while(!corrected){
                int i=in.nextInt();
                if(i==1){
                    move=true;
                    client.send(new ReceiveTargetSquare(client.getToken(),"move"));
                    corrected=true;
                }
                else if(i==2){
                    shot=true;
                    out.println("you can reload this weapon for shot");
                    int posWeapon=actionCLI.actionReload();
                    if(posWeapon==0)
                        actionCLI.actionShot();
                    else
                        actionCLI.shotFinalTurn(posWeapon);
                    corrected=true;
                }
                else if(i==3){
                    grab=true;
                    client.send(new ReceiveTargetSquare(client.getToken(),"grab"));
                    corrected=true;
                }
                else if(i==4){
                    actionCLI.actionPowerUp();
                    corrected=true;
                }

                else if(i==5){
                    client.send(new Pass(client.getToken()));
                    setMyTurn(false);
                    corrected=true;
                }
                else{
                    out.println("it's not difficult, you can do it ");
                    out.println("1:Move(don't press if you play after first player), 2:Shoot, 3:Grab, 4:Use power up  5:Pass  type a number from 1 to 5 ");
                }

            }
        }
        update=false;
    }


    @Override
    public void updateEnemyPosition(Colors player, String position) {
        out.println(player +"has moved in"+position +"" );
    }


    @Override
    public void fillSquare(String squareID, CardAmmo ammo){
        out.print(squareID+":"+ammo.getName()+"\t");
    }
    @Override
    public void fillSpawn(String squareID, int position, CardWeapon weapon){
        out.print(squareID+":"+"position " +position+ " "+weapon.getName()+"\t" );
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
    @Override
    public void setMyPositionID(String myPositionID) {
        this.myPositionID=myPositionID;

        out.println("your new Position is " +myPositionID);

        if(!update){
            update=true;
            setWeapons(weapons);
        }


    }
    @Override
    public void setBlueAmmo(int blueAmmo) {
        this.blueAmmo =blueAmmo;
        out.print("\n You have ammo blue="+blueAmmo);
        waitUpdate=false;
        grab=false;
        shot=false;
    }

    @Override
    public void setRedAmmo(int redAmmo) {
        this.redAmmo=redAmmo;
        out.print("\n You have ammo red="+redAmmo);
    }

    @Override
    public void setPowerUps(ArrayList<CardPowerUp> powerUps) {
        this.powerUps=powerUps;
        out.print("\n Yours powerUp are: \t");
        for(CardPowerUp powerUp:powerUps)
            out.print(powerUp.getName()+"\t");
    }

    @Override
    public void setWeapons(ArrayList<CardWeapon> weapons) {
        if(!update){
            update=true;
            setMyPositionID(myPositionID);
        }
        this.weapons=weapons;
        out.print("\n Yours weapons are: \t");
        for(CardWeapon weapon :weapons)
            out.print(weapon.getName() +"\n");

        if(myPositionID!=null && !grab && !shot && !move &&!waitUpdate){
            if(numberAction>=3 && !finalTurn)
                finalActions();
            else if(finalTurn)
                finalTurnAction();
            else
                startActions();
        }

    }

    @Override
    public void setYellowAmmo(int yellowAmmo) {
        this.yellowAmmo=yellowAmmo;
        out.print("\n You have ammo yellow="+yellowAmmo);
    }


}
