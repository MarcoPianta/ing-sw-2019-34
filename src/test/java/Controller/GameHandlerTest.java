package Controller;

import Model.Colors;
import Model.Game;
import Model.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameHandlerTest {
    @Test
    public void winnerTest() throws FileNotFoundException {
        List<Player> winners=new ArrayList<>();
        ArrayList<Player> players=new ArrayList<>();
        Player player5=new Player("playertest2832", Colors.RED, "playerTest");
        Player player1=new Player("playertest2830", Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831", Colors.BLUE, "playerTest2");
        Player player3=new Player("playertest2832", Colors.YELLOW, "playerTest3");
        Player player4=new Player("playertest2832", Colors.VIOLET, "playerTest4");
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        players.add(player5);
        GameHandler gameHandler=new GameHandler(8,players,"map1");
        player1.getPlayerBoard().addPoints(32);
        player2.getPlayerBoard().addPoints(32);
        player3.getPlayerBoard().addPoints(22);
        player4.getPlayerBoard().addPoints(30);
        player5.getPlayerBoard().addPoints(17);
        winners=gameHandler.winner();
        gameHandler.getGame().getDeadRoute().addMurders(player1,2);
        gameHandler.getGame().getDeadRoute().addMurders(player2,1);
        gameHandler.getGame().getDeadRoute().addMurders(player4,2);
        assertEquals(1,gameHandler.countPlayer(player2));
        assertEquals(2,gameHandler.countPlayer(player1));
        assertEquals(player1,winners.get(0));

        assertEquals(2, winners.size());
        assertEquals(player1,winners.get(0));
        player4.getPlayerBoard().addPoints(2);

        winners=gameHandler.winner();

        assertEquals(2, winners.size());
        assertEquals(player1, winners.get(0));
        assertEquals(player4, winners.get(1));



    }

}
