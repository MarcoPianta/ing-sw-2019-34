package Model;

import java.util.ArrayList;
import java.util.List;

public class Shoot implements Action{
    private Effect shootEffect;
    private Player shooterPlayer;
    private List<Player> targets;
    private NormalSquare targetSquare;
    private Colors roomColor;
    private char actionType;
    private List<Player> gamePlayers;

    public Shoot(Effect effect, Player shooter, List<Player> target){
        this.shootEffect = effect;
        this.shooterPlayer = shooter;
        this.targets = target;
        this.actionType = 'p';
    }

    public Shoot(Effect effect, Player shooter, NormalSquare square){
        this.shootEffect = effect;
        this.shooterPlayer = shooter;
        this.gamePlayers = shooterPlayer.getGameId().getPlayers();
        this.targetSquare = square;
        this.actionType = 's';
    }

    public Shoot(Effect effect, Player shooter, Colors color){
        this.shootEffect = effect;
        this.shooterPlayer = shooter;
        this.gamePlayers = shooterPlayer.getGameId().getPlayers();
        this.roomColor = color;
        this.actionType = 'r';
    }

    public boolean execute(){
        if (isValid()){
            switch(actionType) {
                case ('p'):
                    caseP();
                    return true;

                case ('s'):
                    caseS();
                    return true;

                case ('r'):
                    caseR();
                    return true;

                default:
                    return false;
            }
        }
        else    return false;
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
                    return true;

            case ('s'):
                return reachableSquare().contains(targetSquare);

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

    private boolean conditionControll(Player target, List<Player> visibleTarget, ArrayList<NormalSquare> targetListSquare){
        if ((shootEffect.getPreCondition().isBlind() && visibleTarget.contains(target)) || (!shootEffect.getPreCondition().isBlind() && !(visibleTarget.contains(target)))) {
            return false;
        }
        if (shootEffect.getPreCondition().isEnemiesDifferentSquare()){
            if (targetListSquare.contains(target.getPosition()))
                return false;
            else
                targetListSquare.add(target.getPosition());
        }
        return true;
    }

    /**
     * This method control the isVision PreCondition
     * @return the list of Square reachable from the startSquare with at least movePass step
     */
    public List<NormalSquare> reachableSquare() {
        Effect.PreCondition preCondition = shootEffect.getPreCondition();
        ArrayList<NormalSquare> reachableSquare = new ArrayList<>();
        ArrayList<NormalSquare> thisStepSquare = new ArrayList<>();
        ArrayList<NormalSquare> allStepSquare = new ArrayList<>();
        ArrayList<Colors> colors = new ArrayList<>();
        thisStepSquare.add(shooterPlayer.getPosition());
        allStepSquare.add(shooterPlayer.getPosition());
        int thisStep;
        int i;
        int j;
        if (0 == preCondition.getMinRange())
            reachableSquare.add(shooterPlayer.getPosition());
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
        for (i = 0; i < preCondition.getMaxRange(); i++) {
            thisStep = thisStepSquare.size();
            j = 0;
            while (j < thisStep) {
                isAlreadyReachable(allStepSquare, thisStepSquare, reachableSquare, thisStepSquare.get(0).getN(), colors, preCondition, i);
                isAlreadyReachable(allStepSquare, thisStepSquare, reachableSquare, thisStepSquare.get(0).getE(), colors, preCondition, i);
                isAlreadyReachable(allStepSquare, thisStepSquare, reachableSquare, thisStepSquare.get(0).getS(), colors, preCondition, i);
                isAlreadyReachable(allStepSquare, thisStepSquare, reachableSquare, thisStepSquare.get(0).getW(), colors, preCondition, i);
                thisStepSquare.remove(0);
                j++;
            }
        }
        return reachableSquare;
    }

    /**
     * This method is invoked by reachableSquare method
     */
    private void isAlreadyReachable(ArrayList<NormalSquare> allStepSquare, ArrayList<NormalSquare> thisStepSquare, ArrayList<NormalSquare> reachableSquare, NormalSquare thisSquare, ArrayList<Colors> colors, Effect.PreCondition preCondition, int i){
        if (!allStepSquare.contains(thisSquare)) {
            thisStepSquare.add(thisSquare);
            allStepSquare.add(thisSquare);
            if ((!preCondition.isVision() || colors.contains(thisSquare.getColor())) && i > preCondition.getMinRange() && !reachableSquare.contains(thisSquare))
                reachableSquare.add(reachableSquare.size(), thisSquare);
        }
    }

    /**
     * @return the list of Player that can be targeted by the actorPlayer
     */
    public List<Player> targetablePlayer(){
        ArrayList<Player> reachablePlayer = new ArrayList<>(shooterPlayer.getGameId().getPlayers());
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

    private void caseP(){
        for (int i = 0; i < targets.size(); i++) {
            injureTarget(targets.get(i), shootEffect.getDamage().get(i));
            markTarget(targets.get(i), shootEffect.getMark().get(i));
        }
    }

    private void caseS(){
        for (Player target: gamePlayers) {
            if(target.getPosition() == targetSquare) {
                injureTarget(target, shootEffect.getDamage().get(0));
                markTarget(target, shootEffect.getMark().get(0));
            }
        }
    }

    private void caseR(){
        for (Player target: gamePlayers) {
            if (target.getPosition().getColor() == roomColor){
                injureTarget(target, shootEffect.getDamage().get(0));
                markTarget(target, shootEffect.getMark().get(0));
            }
        }
    }
}
