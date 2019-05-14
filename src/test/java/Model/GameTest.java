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
        Player player=new Player("playertest2832", Colors.RED, "playerTest");
        Player player1=new Player("playertest2830", Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831", Colors.BLUE, "playerTest2");
        Player player3=new Player("playertest2832", Colors.YELLOW, "playerTest3");
        Player player4=new Player("playertest2832", Colors.VIOLET, "playerTest4");
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
        Player player=new Player("playertest2832", Colors.GREEN, "playerTest");

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
        Player player1=new Player("playertest2830", Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831", Colors.RED, "playerTest2");
        Player player3=new Player("playertest2832", Colors.BLUE, "playerTest3");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);

        game.chooseFirstPlayer();
        Player playerTest=game.getFirstPlayer();

        game.incrementCurrentPlayer();
        if(game.getPlayers().indexOf(playerTest)==2)
            assertEquals(0,game.getPlayers().indexOf(game.getCurrentPlayer()));
        else
            assertEquals(game.getPlayers().indexOf(playerTest) +1,game.getPlayers().indexOf(game.getCurrentPlayer()));
        game.incrementCurrentPlayer();
        game.incrementCurrentPlayer();
        assertEquals(game.getCurrentPlayer(),playerTest);
    }

}
