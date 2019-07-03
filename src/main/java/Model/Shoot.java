package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Shoot implements Action, Serializable {
    private Effect shootEffect;
    private Player shooterPlayer;
    private List<Player> targets;
    private List<NormalSquare> targetSquare;
    private Colors roomColor;
    private char actionType;
    private List<Player> gamePlayers;

    public Shoot(Effect effect, Player shooter, List<Player> target, List<NormalSquare> square, Boolean isSquare){
        this.shootEffect = effect;
        this.shooterPlayer = shooter;
        this.targets = target;
        this.targetSquare = square;
        if (isSquare) {
            this.actionType = 's';
            this.gamePlayers = new ArrayList<>(shooterPlayer.getGameId().getPlayers());
            gamePlayers.remove(shooterPlayer);
        }
        else
            this.actionType = 'p';
    }

    public Shoot(Effect effect, Player shooter, Colors color){
        this.shootEffect = effect;
        this.shooterPlayer = shooter;
        this.roomColor = color;
        this.actionType = 'r';
        this.gamePlayers = new ArrayList<>(shooterPlayer.getGameId().getPlayers());
        gamePlayers.remove(shooterPlayer);
    }

    /**
     * This method execute the Shoot Action
     *
     * @return true if the action has been executed, false otherwise
     */
    public boolean execute(){
            switch(actionType) {
                case ('p'):
                    execP();
                    return true;

                case ('s'):
                    execS();
                    return true;

                case ('r'):
                    execR();
                    return true;

                default:
                    return false;
            }
    }


    /**
     * This method is invoked by isValid
     * Control the pre-condition of case P
     * */
    private boolean isValidP(){
        List<Player> visibleTarget = targetablePlayer();
        ArrayList<NormalSquare> targetListSquare = new ArrayList<>();
        for (Player target : targets) {
            if(!conditionControll(target, visibleTarget, targetListSquare)){
                System.out.println("falso1");
                return false;
            }
        }
        if(shootEffect.getPreCondition().isCardinal()) {
            targetListSquare = new ArrayList<>();
            targetListSquare.add(shooterPlayer.getPosition());
            for (Player target : targets) {
                targetListSquare.add(target.getPosition());
            }
            System.out.println(cardinalControl(targetListSquare));
            return cardinalControl(targetListSquare);
        }
        return true;
    }

    /**
     * This method is invoked by isValid
     * Control the pre-condition of case S
     * */
    private boolean isValidS(){
        List<NormalSquare> a = reachableSquare();
        ArrayList<NormalSquare> targetListSquare;
        for (NormalSquare square: targetSquare) {
            if(!(a.contains(square)))
                return false;
        }
        if(shootEffect.getPreCondition().isCardinal()) {
            targetListSquare = new ArrayList<>();
            targetListSquare.add(shooterPlayer.getPosition());
            targetListSquare.addAll(targetSquare);
            return cardinalControl(targetListSquare);
        }
        return true;
    }


    /**
     * This method is invoked by isValid
     * Control the pre-condition of case R
     * */
    private boolean isValidR(){
        for (NormalSquare normalSquare : reachableSquare()){
            if(normalSquare.getColor() == roomColor && normalSquare.getColor() != shooterPlayer.getPosition().getColor())
                return true;
        }
        return false;
    }

    /**
     * Control the Pre-condition of the Shoot Action
     * This method invoke isValidP, isValidS and isValidR methods
     *
     * @return true if the action invocation respect the condition, false otherwise
     */
    public boolean isValid(){
        switch (actionType){
            case ('p'):
                return isValidP();

            case ('s'):
                return isValidS();

            case ('r'):
                return isValidR();

            default:
                return false;
        }
    }

    /**
     * When it's called by the 'p' case of the Shoot action this method returns True if the players in the List of selected targets belong to cardinals square, otherwise return false
     * When it's called by the 's' case of the Shoot action this method returns True if the squares in the List of selected targets belong to cardinals square, otherwise return false
     */
    private boolean cardinalControl(List<NormalSquare> square) {
        int i;
        int j;
        int x = 0;
        int y = 0;
        for (j = 0,i = 1; i < square.size() && (x == 0 || y == 0); i++) {
            if (square.get(i).getId().charAt(0) != square.get(j).getId().charAt(0))
                x++;
            if (square.get(i).getId().charAt(2) != square.get(j).getId().charAt(2))
                y++;
        }
        return (x == 0 || y == 0);
    }

    /**
     *
     * */
    private boolean conditionControll(Player target, List<Player> visibleTarget, ArrayList<NormalSquare> targetListSquare){
        if (shootEffect.getPreCondition().isMelee() && shooterPlayer.getPosition() != target.getPosition()){
            System.out.println("1");
            return false;
        }
        if (shootEffect.getPreCondition().isBlind() && visibleTarget.contains(target)){
            System.out.println("2");

            return false;
        }

        if (!shootEffect.getPreCondition().isBlind() && !(visibleTarget.contains(target))){
            System.out.println("3");

            return false;
        }
        if (shootEffect.getPreCondition().isEnemiesDifferentSquare()){
            if (targetListSquare.contains(target.getPosition())){
                System.out.println("4");

                return false;
            }
            else
                targetListSquare.add(target.getPosition());
        }
        return true;
    }

    /**
     * This method control the effect's PreCondition
     * @return the list of Square reachable from the startSquare with at least movePass step
     */
    public List<NormalSquare> reachableSquare() {
        Effect.PreCondition preCondition = shootEffect.getPreCondition();
        ArrayList<NormalSquare> reachableSquares = new ArrayList<>();
        ArrayList<NormalSquare> thisStepSquare = new ArrayList<>();
        ArrayList<NormalSquare> allStepSquare = new ArrayList<>();
        ArrayList<Colors> colors = new ArrayList<>();
        thisStepSquare.add(shooterPlayer.getPosition());
        allStepSquare.add(shooterPlayer.getPosition());
        int thisStep;
        int range = 0;
        int j;
        if(preCondition.isMelee()) {
            reachableSquares.add(shooterPlayer.getPosition());
            return reachableSquares;
        }
        if (preCondition.isVision()) {
            colors.add(shooterPlayer.getPosition().getColor());
            if (shooterPlayer.getPosition().getN().getColor() != shooterPlayer.getPosition().getColor()){
                colors.add(shooterPlayer.getPosition().getN().getColor());
            }

            if (shooterPlayer.getPosition().getE().getColor() != shooterPlayer.getPosition().getColor()){
                colors.add(shooterPlayer.getPosition().getE().getColor());
            }
            if (shooterPlayer.getPosition().getS().getColor() != shooterPlayer.getPosition().getColor()){
                colors.add(shooterPlayer.getPosition().getS().getColor());
            }
            if (shooterPlayer.getPosition().getW().getColor() != shooterPlayer.getPosition().getColor()){
                colors.add(shooterPlayer.getPosition().getW().getColor());
            }
        }
        if (0 == preCondition.getMinRange()) {
            reachableSquares.add(shooterPlayer.getPosition());
        }
        while((preCondition.getMaxRange() == 0 && !thisStepSquare.isEmpty()) || range <= preCondition.getMaxRange()){
            thisStep = thisStepSquare.size();
            j = 0;
            while (j < thisStep) {
                isAlreadyReachable(allStepSquare, thisStepSquare, reachableSquares, thisStepSquare.get(0).getN(), colors, preCondition, range);
                isAlreadyReachable(allStepSquare, thisStepSquare, reachableSquares, thisStepSquare.get(0).getE(), colors, preCondition, range);
                isAlreadyReachable(allStepSquare, thisStepSquare, reachableSquares, thisStepSquare.get(0).getS(), colors, preCondition, range);
                isAlreadyReachable(allStepSquare, thisStepSquare, reachableSquares, thisStepSquare.get(0).getW(), colors, preCondition, range);
                thisStepSquare.remove(0);
                j++;
            }
            range++;
        }
        return reachableSquares;
    }

    /**
     * This method is invoked by reachableSquare method
     */
    private void isAlreadyReachable(ArrayList<NormalSquare> allStepSquare, ArrayList<NormalSquare> thisStepSquare, ArrayList<NormalSquare> reachableSquares, NormalSquare thisSquare, ArrayList<Colors> colors, Effect.PreCondition preCondition, int range){
        if (!allStepSquare.contains(thisSquare)) {
            thisStepSquare.add(thisSquare);
            allStepSquare.add(thisSquare);
            if (!reachableSquares.contains(thisSquare)
                    && (preCondition.isVision() && colors.contains((thisSquare.getColor())))
                    || (!preCondition.isVision()
                        && (!preCondition.isCardinal() || (thisSquare.getId().charAt(0) == shooterPlayer.getPosition().getId().charAt(0)
                                                            || thisSquare.getId().charAt(2) == shooterPlayer.getPosition().getId().charAt(2)))))
                reachableSquares.add(reachableSquares.size(), thisSquare);
        }
    }

    /**
     * This method return a List of all the square that compose the rooms reachables with the selected effect
     */
    public List<NormalSquare> reachableRoom() {
        ArrayList<Colors> roomList = new ArrayList<>();
        List<NormalSquare> squareList = new ArrayList<>();
        if(roomList.contains(shooterPlayer.getPosition().getN()) && shooterPlayer.getPosition().getN().getColor() != shooterPlayer.getPosition().getColor()) {
            roomList.add(shooterPlayer.getPosition().getN().getColor());
        }
        if(roomList.contains(shooterPlayer.getPosition().getE()) && shooterPlayer.getPosition().getE().getColor() != shooterPlayer.getPosition().getColor()) {
            roomList.add(shooterPlayer.getPosition().getE().getColor());
        }
        if(roomList.contains(shooterPlayer.getPosition().getS()) && shooterPlayer.getPosition().getS().getColor() != shooterPlayer.getPosition().getColor()) {
            roomList.add(shooterPlayer.getPosition().getS().getColor());
        }
        if(roomList.contains(shooterPlayer.getPosition().getW()) && shooterPlayer.getPosition().getW().getColor() != shooterPlayer.getPosition().getColor()) {
            roomList.add(shooterPlayer.getPosition().getW().getColor());
        }
        for (int i = 0; i < shooterPlayer.getGameId().getMap().getRooms().size(); i++) {
            if(roomList.contains(shooterPlayer.getGameId().getMap().getRooms().get(i).getColor())){
                squareList.addAll(shooterPlayer.getGameId().getMap().getRooms().get(i).getNormalSquares());
            }
        }

        return squareList;
    }


        /**
         * @return the list of Player that can be targeted by the actorPlayer
         */
    public List<Player> targetablePlayer(){
        ArrayList<Player> reachablePlayer = new ArrayList<>(shooterPlayer.getGameId().getPlayers());
        reachablePlayer.remove(shooterPlayer);

        int i = 0;
        while(i < reachablePlayer.size()){
            if(!reachableSquare().contains(reachablePlayer.get(i).getPosition()))
                reachablePlayer.remove(i);
            else
                i++;
        }

        return reachablePlayer;
    }

    /**
     * @param targetPlayer  The Player that is targeted in this step
     * @param mark        The number of mark that must be dealt to the targetPlayer
     * This method mark the target
     */
    private void markTarget(Player targetPlayer, int mark){
        targetPlayer.getPlayerBoard().getHealthPlayer().addMark(shooterPlayer, mark);
    }

    /**
     * @param targetPlayer  The Player that is targeted in this step
     * @param damage        The number of mark that must be dealt to the targetPlayer
     * This method damage the target
     */
    private void injureTarget(Player targetPlayer, int damage){
        targetPlayer.getPlayerBoard().getHealthPlayer().addDamage(shooterPlayer, damage);
    }

    /**
     * This method is called by execute
     * This method execute the Shoot Action only in 'p' case
     */
    private void execP(){
        for (int i = 0; i < targets.size(); i++) {
            injureTarget(targets.get(i), shootEffect.getpDamage().get(i));
            markTarget(targets.get(i), shootEffect.getpMark().get(i));
        }
    }

    /**
     * This method is called by execute
     * This method execute the Shoot Action only in 's' case
     */
    private void execS(){
        if(shootEffect.getSquareNumber() == 4)
            shootShockWave();
        else if(shootEffect.getSquareNumber() == 2){
            shootFlameThrower();
        }
        else{
            for (Player target : gamePlayers) {
                if (target.getPosition() == targetSquare.get(0)) {
                    injureTarget(target, shootEffect.getsDamage().get(0));
                    markTarget(target, shootEffect.getsMark().get(0));
                }
            }
        }
    }

    /**
     * This method is called by execS
     * This method execute the Shoot Action only for FlameThrower Weapon
     */
    private void shootFlameThrower() {
        ArrayList<NormalSquare> flameSquares = new ArrayList<>(targetSquare);
        int x = 0;
        int y = 0;
        int c = 0;
        NormalSquare newTargetSquare = null;
        if (shooterPlayer.getPosition().getId().charAt(0) == flameSquares.get(0).getId().charAt(0)) {
            x = Integer.parseInt(String.valueOf(shooterPlayer.getPosition().getId().charAt(0)));
            y = Integer.parseInt(String.valueOf(flameSquares.get(0).getId().charAt(2))) + Integer.parseInt(String.valueOf(flameSquares.get(0).getId().charAt(2))) - Integer.parseInt(String.valueOf(shooterPlayer.getPosition().getId().charAt(2)));
            c = 1;
            newTargetSquare = shooterPlayer.getGameId().getMap().getSquareFromId(x + "," + y);
        } else if (shooterPlayer.getPosition().getId().charAt(2) == flameSquares.get(0).getId().charAt(2)) {
            x = Integer.parseInt(String.valueOf(flameSquares.get(0).getId().charAt(0))) + Integer.parseInt(String.valueOf(flameSquares.get(0).getId().charAt(0))) - Integer.parseInt(String.valueOf(shooterPlayer.getPosition().getId().charAt(0)));
            y = Integer.parseInt(String.valueOf(shooterPlayer.getPosition().getId().charAt(2)));
            c = 2;
            newTargetSquare = shooterPlayer.getGameId().getMap().getSquareFromId(x + "," + y);
        }
        if((c == 1) && flameSquares.get(0).getId().charAt(0) == newTargetSquare.getId().charAt(0) || (c == 2) && flameSquares.get(0).getId().charAt(2) == newTargetSquare.getId().charAt(2)) {
            flameSquares.add(newTargetSquare);
        }
        for (int i = 0; i < flameSquares.size(); i++) {
            for (Player target : gamePlayers) {
                if (target.getPosition() == flameSquares.get(i)) {
                    injureTarget(target, shootEffect.getsDamage().get(i));
                    markTarget(target, shootEffect.getsMark().get(i));
                }
            }
        }
    }

    /**
     * This method is called by execS
     * This method execute the Shoot Action only for ShockWave Weapon
     */
    private void shootShockWave() {
        ArrayList<NormalSquare> shockSquares = new ArrayList<>();
        if (shooterPlayer.getPosition().getN() != shooterPlayer.getPosition())
            shockSquares.add(shooterPlayer.getPosition().getN());
        if (shooterPlayer.getPosition().getE() != shooterPlayer.getPosition())
            shockSquares.add(shooterPlayer.getPosition().getE());
        if (shooterPlayer.getPosition().getS() != shooterPlayer.getPosition())
            shockSquares.add(shooterPlayer.getPosition().getS());
        if (shooterPlayer.getPosition().getW() != shooterPlayer.getPosition())
            shockSquares.add(shooterPlayer.getPosition().getW());
        for (int i = 0; i < shockSquares.size(); i++) {
            for (Player target : gamePlayers) {
                if (target.getPosition() == shockSquares.get(i)) {
                    injureTarget(target, shootEffect.getsDamage().get(i));
                    markTarget(target, shootEffect.getsMark().get(i));
                }
            }
        }
    }

    /**
     * This method is called by execute
     * This method execute the Shoot Action only in 'r' case
     */
    private void execR(){
        for (Player target: gamePlayers) {
            if (target.getPosition().getColor() == roomColor){
                injureTarget(target, shootEffect.getrDamage());
                markTarget(target, shootEffect.getrMark());
            }
        }
    }
}
