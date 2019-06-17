package Model;

import java.util.ArrayList;
import java.util.List;

public class Shoot implements Action{
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
            this.gamePlayers = shooterPlayer.getGameId().getPlayers();
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
        this.gamePlayers = shooterPlayer.getGameId().getPlayers();
        gamePlayers.remove(shooterPlayer);
    }

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
     * Control the Pre-condition of the Shoot Action
     * This method invoke reachableSquare and targetablePlayer methods
     *
     * @return true if the action invocation respect the condition, false otherwise
     */

    public boolean isValid(){
        switch (actionType){
            case ('p'):
                List<Player> visibleTarget = targetablePlayer();
                ArrayList<NormalSquare> targetListSquare = new ArrayList<>();
                for (Player target : targets) {
                    if(!conditionControll(target, visibleTarget, targetListSquare)){
                        return false;
                    }
                }
                if(shootEffect.getPreCondition().isCardinal()) {
                    targetListSquare = new ArrayList<>();
                    targetListSquare.add(shooterPlayer.getPosition());
                    for (Player target : targets) {
                        targetListSquare.add(target.getPosition());
                    }
                    return cardinalControl(targetListSquare);
                }
                return true;

            case ('s'):
                List<NormalSquare> a = reachableSquare();
                for (NormalSquare square: targetSquare) {
                    if(!(a.contains(square)))
                        return false;
                }
                if(shootEffect.getPreCondition().isCardinal()) {
                    targetListSquare = new ArrayList<>();
                    targetListSquare.add(shooterPlayer.getPosition());
                    for (NormalSquare square: targetSquare)
                        targetListSquare.add(square);
                    return cardinalControl(targetListSquare);
                }

                return true;

            case ('r'):
                    for (NormalSquare normalSquare : reachableSquare()){
                        if(normalSquare.getColor() == roomColor)
                            return true;
                    }
                return false;
            default:
                return false;
        }
    }

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

    private boolean conditionControll(Player target, List<Player> visibleTarget, ArrayList<NormalSquare> targetListSquare){
        if (shootEffect.getPreCondition().isMelee() && shooterPlayer.getPosition() != target.getPosition())
            return false;
        if ((shootEffect.getPreCondition().isBlind() && visibleTarget.contains(target)) || (!shootEffect.getPreCondition().isBlind() && !(visibleTarget.contains(target))))
            return false;
        if (shootEffect.getPreCondition().isEnemiesDifferentSquare()){
            if (targetListSquare.contains(target.getPosition()))
                return false;
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
            if (shooterPlayer.getPosition().getN().getColor() != shooterPlayer.getPosition().getColor())
                colors.add(shooterPlayer.getPosition().getN().getColor());
            if (shooterPlayer.getPosition().getE().getColor() != shooterPlayer.getPosition().getColor())
                colors.add(shooterPlayer.getPosition().getE().getColor());
            if (shooterPlayer.getPosition().getS().getColor() != shooterPlayer.getPosition().getColor())
                colors.add(shooterPlayer.getPosition().getS().getColor());
            if (shooterPlayer.getPosition().getW().getColor() != shooterPlayer.getPosition().getColor())
                colors.add(shooterPlayer.getPosition().getW().getColor());
        }
        if (0 == preCondition.getMinRange()) {
            reachableSquares.add(shooterPlayer.getPosition());
        }
        while((preCondition.getMaxRange() == 0 && !thisStepSquare.isEmpty()) || (range <= preCondition.getMaxRange())){
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
            if (!reachableSquares.contains(thisSquare) && preCondition.isVision() && colors.contains((thisSquare.getColor())) && (!preCondition.isCardinal() || (thisSquare.getId().charAt(0) == shooterPlayer.getPosition().getId().charAt(0) || thisSquare.getId().charAt(2) == shooterPlayer.getPosition().getId().charAt(2))))
                reachableSquares.add(reachableSquares.size(), thisSquare);
        }
    }

    /**
     * @return the list of Player that can be targeted by the actorPlayer
     */
    public List<Player> targetablePlayer(){
        ArrayList<Player> reachablePlayer = new ArrayList<>(shooterPlayer.getGameId().getPlayers());
        reachablePlayer.remove(shooterPlayer);
        int i = 0;
        while(i < reachablePlayer.size()){
            if((!reachableSquare().contains(reachablePlayer.get(i).getPosition()) || (reachablePlayer.get(i) == shooterPlayer)))
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
        targetPlayer.getPlayerBoard().getHealthPlayer().addMark(shooterPlayer, damage);
    }

    private void execP(){
        for (int i = 0; i < targets.size(); i++) {
            injureTarget(targets.get(i), shootEffect.getpDamage().get(i));
            markTarget(targets.get(i), shootEffect.getpMark().get(i));
        }
    }

    private void execS(){
        for (int i = 0; i < targetSquare.size(); i++) {
            for (Player target : gamePlayers) {
                if (target.getPosition() == targetSquare.get(i)) {
                    injureTarget(target, shootEffect.getsDamage().get(i));
                    markTarget(target, shootEffect.getsMark().get(i));
                }
            }
        }
    }

    private void execR(){
        for (Player target: gamePlayers) {
            if (target.getPosition().getColor() == roomColor){
                injureTarget(target, shootEffect.getpDamage().get(0));
                markTarget(target, shootEffect.getpMark().get(0));
            }
        }
    }
}
