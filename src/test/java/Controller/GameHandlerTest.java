package Controller;


import Model.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameHandlerTest {
    @Test
    public void winnerTest() throws FileNotFoundException {
        List<Player> winners;
        ArrayList<Integer> players=new ArrayList<>();

        players.add(1233);
        players.add(3525);
        players.add(235555);
        players.add(235235);
        players.add(53325);
        GameHandler gameHandler=new GameHandler(8,players,"map1");
        gameHandler.getGame().getPlayers().get(0).getPlayerBoard().addPoints(32);
        gameHandler.getGame().getPlayers().get(1).getPlayerBoard().addPoints(32);
        gameHandler.getGame().getPlayers().get(2).getPlayerBoard().addPoints(22);
        gameHandler.getGame().getPlayers().get(3).getPlayerBoard().addPoints(30);
        gameHandler.getGame().getPlayers().get(4).getPlayerBoard().addPoints(17);

        gameHandler.getGame().getDeadRoute().addMurders(gameHandler.getGame().getPlayers().get(0),2);
        gameHandler.getGame().getDeadRoute().addMurders(gameHandler.getGame().getPlayers().get(1),1);
        gameHandler.getGame().getDeadRoute().addMurders(gameHandler.getGame().getPlayers().get(3),2);
        assertEquals(1,gameHandler.countPlayer(gameHandler.getGame().getPlayers().get(1)));
        assertEquals(2,gameHandler.countPlayer(gameHandler.getGame().getPlayers().get(0)));
        winners=gameHandler.winner();
        assertEquals(gameHandler.getGame().getPlayers().get(0),winners.get(0));

        assertEquals(1, winners.size());
        assertEquals(gameHandler.getGame().getPlayers().get(0),winners.get(0));
        gameHandler.getGame().getPlayers().get(3).getPlayerBoard().addPoints(2);

        winners=gameHandler.winner();

        assertEquals(2, winners.size());
        assertEquals(gameHandler.getGame().getPlayers().get(0), winners.get(0));
        assertEquals(gameHandler.getGame().getPlayers().get(3), winners.get(1));
    }

    @Test
    public void endGameTest() throws FileNotFoundException{
        ArrayList<Integer> players=new ArrayList<>();
        players.add(1233);
        players.add(3525);
        players.add(235555);

        GameHandler gameHandler=new GameHandler(1,players,"map1");
        gameHandler.getGame().getDeadRoute().setFinalTurn(true);
        gameHandler.getTurnHandler().getEndTurnChecks().isFinalTurn(gameHandler.getGame());
        gameHandler.getGame().getPlayers().get(2).getPlayerBoard().getHealthPlayer().addDamage(gameHandler.getGame().getPlayers().get(1),3);
        gameHandler.getGame().getPlayers().get(2).getPlayerBoard().getHealthPlayer().addDamage(gameHandler.getGame().getPlayers().get(0),2);
        gameHandler.getGame().getPlayers().get(1).getPlayerBoard().getHealthPlayer().addDamage(gameHandler.getGame().getPlayers().get(2),3);
        gameHandler.getGame().getPlayers().get(0).getPlayerBoard().getHealthPlayer().addDamage(gameHandler.getGame().getPlayers().get(1),3);
        gameHandler.getGame().getPlayers().get(0).getPlayerBoard().getHealthPlayer().addDamage(gameHandler.getGame().getPlayers().get(2),2);
        gameHandler.getGame().getDeadRoute().addMurders(gameHandler.getGame().getPlayers().get(0),1);
        gameHandler.endGame();

        assertEquals(9,gameHandler.getGame().getPlayers().get(0).getPlayerBoard().getPoints());
        assertEquals(4,gameHandler.getGame().getPlayers().get(1).getPlayerBoard().getPoints());
        assertEquals(3,gameHandler.getGame().getPlayers().get(2).getPlayerBoard().getPoints());
    }



}
