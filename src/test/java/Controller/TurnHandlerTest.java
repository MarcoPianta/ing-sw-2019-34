package Controller;

import Model.*;
import network.messages.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class TurnHandlerTest {
    @Test
    public void startTest()throws FileNotFoundException{
        ArrayList<Integer> players=new ArrayList<>();
        players.add(4324525);
        players.add(67625);
        GameHandler gameHandler=new GameHandler(5,players,"map1");
        gameHandler.getGame().setCurrentPlayer(gameHandler.getGame().getPlayers().get(0));

        assertEquals(2,gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().size());
        assertEquals(1,gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0]);
        assertEquals(1,gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1]);
        assertEquals(1,gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2]);
        assertEquals(StateMachineEnumerationTurn.START,gameHandler.getGame().getCurrentPlayer().getState());
        assertNull(gameHandler.getGame().getCurrentPlayer().getPosition());

    }
    @Test
    public void actionStateTest() throws FileNotFoundException {
        ArrayList<Integer> players=new ArrayList<>();
        List<NormalSquare> squares;

        players.add(32413);
        players.add(4324525);
        GameHandler gameHandler=new GameHandler(5,players,"map1");
        gameHandler.getGame().setCurrentPlayer(gameHandler.getGame().getPlayers().get(0));
        //test gameHandler receive square
        PossibleMove possibleMove=new PossibleMove(gameHandler.getGame().getPlayers().get(0).getPlayerID(),3);
        //move test
        squares=gameHandler.receiveSquare(possibleMove);
        NormalSquare newSquare= squares.get(0);
        MoveMessage message=new MoveMessage(gameHandler.getGame().getPlayers().get(0).getPlayerID(),newSquare);
        assertTrue(gameHandler.getTurnHandler().actionState(message));
        assertEquals(StateMachineEnumerationTurn.ACTION1,gameHandler.getGame().getCurrentPlayer().getState());
        assertEquals(StateMachineEnumerationTurn.ACTION2,gameHandler.getTurnHandler().getNextState());

        String file= WeaponDictionary.ELECTROSCYTE.getAbbreviation();
        CardWeapon cardWeapon=new CardWeapon(file);
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().addAmmo(3,3,3);
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().addWeapon(cardWeapon);
        //test gameHandler receiveTarget
        //shot test
        PossibleTargetShot shotMessage= new PossibleTargetShot(gameHandler.getGame().getPlayers().get(0).getPlayerID(),gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(0).getEffects().get(0));
        gameHandler.getGame().getPlayers().get(1).newPosition(gameHandler.getGame().getCurrentPlayer().getPosition());

        ArrayList<Player>  playersTarget;
        playersTarget=gameHandler.receiveTarget(shotMessage);//receive target

        Shot shot=new Shot(playersTarget,gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(0).getEffects().get(0),0);
        assertTrue(gameHandler.getTurnHandler().actionState(shot));

        assertEquals(StateMachineEnumerationTurn.ACTION2,gameHandler.getGame().getCurrentPlayer().getState());
        assertEquals(StateMachineEnumerationTurn.RELOAD,gameHandler.getTurnHandler().getNextState());
        assertFalse(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(0).isCharge());

        //grab
        gameHandler.getGame().getCurrentPlayer().setState(StateMachineEnumerationTurn.ACTION1);
        GrabNotOnlyAmmo grabNotOnlyAmmo=new GrabNotOnlyAmmo(gameHandler.getGame().getCurrentPlayer().getPlayerID());
        assertTrue(gameHandler.receiveServerMessage(grabNotOnlyAmmo));
    }
    @Test
    public void actionReloadMessage() throws  FileNotFoundException{
        ArrayList<Integer> players=new ArrayList<>();
        players.add(23532);
        GameHandler gameHandler=new GameHandler(5,players,"map1");
        String file= WeaponDictionary.ELECTROSCYTE.getAbbreviation();
        CardWeapon cardWeapon=new CardWeapon(file);
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().addAmmo(3,3,3);
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().addWeapon(cardWeapon);
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerWeapons().get(0).setCharge(false);

        ReloadMessage reloadMessage=new ReloadMessage(gameHandler.getGame().getCurrentPlayer().getPlayerID(),0,null);
        assertTrue(gameHandler.receiveServerMessage(reloadMessage));
    }

    @Test
    public void usePowerUpTest() throws FileNotFoundException{
        ArrayList<Integer> players=new ArrayList<>();
        players.add(32413);
        players.add(4324525);
        GameHandler gameHandler=new GameHandler(5,players,"map1");

        CardNotOnlyAmmo cardNotOnlyAmmo=new CardNotOnlyAmmo(AmmoEnum.AMMO1.getAbbreviation());
        NormalSquare normalSquare=new NormalSquare();
        normalSquare.setItems(cardNotOnlyAmmo);
        GrabNotOnlyAmmo grabNotOnlyAmmo=new GrabNotOnlyAmmo(gameHandler.getGame().getPlayers().get(0).getPlayerID());
        gameHandler.receiveServerMessage(grabNotOnlyAmmo);

        NormalSquare normalSquare1=new NormalSquare();
        UsePowerUp usePowerUp=new UsePowerUp(gameHandler.getGame().getPlayers().get(0).getPlayerID(),0,gameHandler.getGame().getPlayers().get(0),gameHandler.getGame().getPlayers().get(1),normalSquare1);
        gameHandler.getGame().setCurrentPlayer(gameHandler.getGame().getPlayers().get(0));
        if(gameHandler.getGame().getPlayers().get(0).getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(usePowerUp.getPowerUp()).getWhen().equals("during")&&(usePowerUp.getUser()==gameHandler.getGame().getCurrentPlayer())
                &&(gameHandler.getGame().getCurrentPlayer().getState()!=StateMachineEnumerationTurn.ENDTURN))
            assertTrue(gameHandler.receiveServerMessage(usePowerUp));
        else if(gameHandler.getGame().getPlayers().get(0).getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(usePowerUp.getPowerUp()).getWhen().equals("dealing") &&(usePowerUp.getUser()!=gameHandler.getGame().getCurrentPlayer()))
            assertFalse(gameHandler.receiveServerMessage(usePowerUp));
        else if(gameHandler.getGame().getPlayers().get(0).getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(usePowerUp.getPowerUp()).getWhen().equals("get"))
            assertFalse(gameHandler.receiveServerMessage(usePowerUp));
    }

    @Test
    public void endTurnTest() throws FileNotFoundException{
        ArrayList<Integer> players=new ArrayList<>();
        players.add(32413);
        players.add(4324525);
        GameHandler gameHandler=new GameHandler(5,players,"map1");
        gameHandler.getGame().setCurrentPlayer(gameHandler.getGame().getPlayers().get(0));

        Pass pass=new Pass();
        gameHandler.receiveServerMessage(pass);

        assertEquals(StateMachineEnumerationTurn.WAIT,gameHandler.getGame().getPlayers().get(0).getPosition());
        assertEquals(gameHandler.getGame().getPlayers().get(1),gameHandler.getGame().getCurrentPlayer());

        //start
        assertEquals(StateMachineEnumerationTurn.START,gameHandler.getGame().getPlayers().get(1).getPosition());
    }

    @Test
    public void fillSquareTest() throws  FileNotFoundException{
        ArrayList<Integer> players=new ArrayList<>();
        ArrayList<CardWeapon> weapons=new ArrayList<>();
        players.add(32413);
        players.add(4324525);
        GameHandler gameHandler=new GameHandler(5,players,"map1");
        gameHandler.getGame().setCurrentPlayer(gameHandler.getGame().getPlayers().get(0));


        String file1= WeaponDictionary.FURNACE.getAbbreviation();
        CardWeapon cardWeapon1=new CardWeapon(file1);
        String file2= WeaponDictionary.ELECTROSCYTE.getAbbreviation();
        CardWeapon cardWeapon2=new CardWeapon(file2);
        weapons.add(cardWeapon1);
        weapons.add(cardWeapon2);
        //spawnSquare
        SpawnSquare spawnSquare=new SpawnSquare(null,null,null,null,Colors.BLUE,weapons);
        //NormalSquare
        NormalSquare normalSquare=new NormalSquare(null,null,null,null, Colors.RED,null);

        gameHandler.getTurnHandler().getEndTurnChecks().getEmptySquares().add(normalSquare);
        gameHandler.getTurnHandler().getEndTurnChecks().getEmptySquares().add(spawnSquare);

        gameHandler.getTurnHandler().getEndTurnChecks().fillSquare(gameHandler.getGame());
        assertEquals(3,spawnSquare.getWeapons().size());
        assertNotNull(normalSquare.getItem());


    }

    @Test
    public void playerIsDeadTest()throws FileNotFoundException{
        ArrayList<Integer> players=new ArrayList<>();
        players.add(32413);
        players.add(4324525);
        GameHandler gameHandler=new GameHandler(5,players,"map1");
        gameHandler.getGame().setCurrentPlayer(gameHandler.getGame().getPlayers().get(0));

        while(!gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().isEmpty())
            gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().remove(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().size()-1);
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHealthPlayer().addDamage(gameHandler.getGame().getPlayers().get(1),11);

        gameHandler.getTurnHandler().getEndTurnChecks().playerIsDead(gameHandler.getGame());
        assertEquals(1,gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().size());
    }
    @Test
    public void isFinalTurn()throws FileNotFoundException{
        ArrayList<Integer> players=new ArrayList<>();
        players.add(32413);
        players.add(4324525);
        GameHandler gameHandler=new GameHandler(5,players,"map1");
        gameHandler.getGame().setCurrentPlayer(gameHandler.getGame().getFirstPlayer());

        gameHandler.getGame().getDeadRoute().setFinalTurn(true);

        gameHandler.getTurnHandler().getEndTurnChecks().isFinalTurn(gameHandler.getGame());

        assertEquals(2,gameHandler.getGame().getPlayers().get(0).getPlayerBoard().getMaxReward());
        assertEquals(2,gameHandler.getGame().getPlayers().get(1).getPlayerBoard().getMaxReward());
        assertEquals(gameHandler.getGame().getCurrentPlayer(),gameHandler.getFinalTurnHandler().getFirstFinalTurnPlayer());
        assertTrue(gameHandler.getFinalTurnHandler().isAlreadyFirstPlayer());
    }


}
