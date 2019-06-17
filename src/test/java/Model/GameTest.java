package Model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    /**
     * this method tests CalculatePoints and his private method
     * */
    @Test
    public void testCalculatePoints()throws  FileNotFoundException{
        ArrayList<Player> bestMurder=new ArrayList<>();
        Game game=new Game(8,"map1");
        Player player=new Player(2832, Colors.RED);
        Player player1=new Player(2830, Colors.GREEN);
        Player player2=new Player(2831, Colors.BLUE);
        Player player3=new Player(2832, Colors.YELLOW);
        Player player4=new Player(2832, Colors.VIOLET);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);
        for(int i=0; i<3;i++)
            bestMurder.add(player1);
        for(int i=0; i<2;i++)
            bestMurder.add(player2);
        for(int i=0; i<3;i++)
            bestMurder.add(player3);
        for(int i=0; i<3;i++)
            bestMurder.add(player4);
        game.calculatePoints(bestMurder,false,player);

        assertEquals(8,player1.getPlayerBoard().getPoints());
        assertEquals(6,player3.getPlayerBoard().getPoints());
        assertEquals(4,player4.getPlayerBoard().getPoints());
        assertEquals(2,player2.getPlayerBoard().getPoints());
        assertEquals(6,player.getPlayerBoard().getMaxReward());
    }

    /*
     * this method tests the adding of new player
     * */
    @Test
    public void addPlayerTest()throws  FileNotFoundException{
        Game game=new Game(8,"map1");
        Player player=new Player(2832, Colors.GREEN);

        assertTrue(game.getPlayers().isEmpty() );
        game.addPlayer(player);
        assertFalse(game.getPlayers().isEmpty());
        assertEquals(game,player.getGameId());
    }
    /*
     * this method tests increment current player and the choose of the first player
     * */

    @Test
    public void incrementCurrentPlayerTest() throws FileNotFoundException {
        Game game=new Game(5,"map1");
        Player player1=new Player(2830, Colors.GREEN);
        Player player2=new Player(2831, Colors.RED);
        Player player3=new Player(2832, Colors.BLUE);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);

        game.setCurrentPlayer(game.getPlayers().get(2));

        game.incrementCurrentPlayer();

        assertEquals(game.getPlayers().get(0),game.getCurrentPlayer());
        game.incrementCurrentPlayer();
        game.incrementCurrentPlayer();
        assertEquals(game.getPlayers().get(2),game.getCurrentPlayer());
    }

}
