package Controller;


import Model.Player;
import network.Server.GameLobby;
import network.Server.Server;
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
        GameHandler gameHandler=new GameHandler(8,players,"map1",null);
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
    }




}
