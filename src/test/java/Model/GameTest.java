package Model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    /**
     * this method tests the adding of points after a kill
     * */
    @Test
    public void addPLayerPoints(){
        Game game=new Game("gametest");
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831",game, Colors.BLUE, "playerTest2");
        Player player3=new Player("playertest2832",game, Colors.YELLOW, "playerTest3");
        Player player4=new Player("playertest2832",game, Colors.RED, "playerTest3");
        Player player5=new Player("playertest2832",game, Colors.VIOLET, "playerTest3");
        ArrayList<Colors> bestMurder=new ArrayList<>();

        bestMurder.add(Colors.GREEN);
        bestMurder.add(Colors.BLUE);
        bestMurder.add(Colors.YELLOW);
        bestMurder.add(Colors.VIOLET);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);
        game.addPlayer(player5);

        game.addPlayerPoints(bestMurder,player4);
        assertEquals(8,player1.getPlayerBoard().getPoints());
        assertEquals(6,player2.getPlayerBoard().getPoints());
        assertEquals(4,player3.getPlayerBoard().getPoints());
        assertEquals(2,player5.getPlayerBoard().getPoints());
        assertEquals(6,player4.getPlayerBoard().getMaxReward());

        game.addPlayerPoints(bestMurder,player4);
        assertEquals(14,player1.getPlayerBoard().getPoints());
        assertEquals(10,player2.getPlayerBoard().getPoints());
        assertEquals(6,player3.getPlayerBoard().getPoints());
        assertEquals(3,player5.getPlayerBoard().getPoints());
        assertEquals(4,player4.getPlayerBoard().getMaxReward());

        game.addPlayerPoints(bestMurder,player4);
        assertEquals(18,player1.getPlayerBoard().getPoints());
        assertEquals(12,player2.getPlayerBoard().getPoints());
        assertEquals(7,player3.getPlayerBoard().getPoints());
        assertEquals(4,player5.getPlayerBoard().getPoints());
        assertEquals(2,player4.getPlayerBoard().getMaxReward());


    }
    /*
     * this method tests the adding of new player
     * */
    @Test
    public void addPlayerTest(){
        Game game=new Game("gametest");
        Player player=new Player("playertest2832",game, Colors.GREEN, "playerTest");

        assertTrue(game.getPlayers().isEmpty() );
        game.addPlayer(player);
        assertFalse(game.getPlayers().isEmpty());
    }
    /*
     * this method tests increment current player and the choose of the first player
     * */

    @Test
    public void incrementCurrentPlayerTest(){
        Game game=new Game("gametest");
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831",game, Colors.GREEN, "playerTest2");
        Player player3=new Player("playertest2832",game, Colors.GREEN, "playerTest3");


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
