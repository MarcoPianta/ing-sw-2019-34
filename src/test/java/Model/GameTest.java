package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    /*
     * this method tests the adding of new player
     * */
    @Test
    public void addPlayerTest(){
        Player player=new Player("playertest2832","gametest3782671", Colors.GREEN, "playerTest");
        Game game=new Game("gametest3782671");

        assertTrue(game.getPlayers().isEmpty() );
        game.addPlayer(player);
        assertFalse(game.getPlayers().isEmpty());
    }
    /*
     * this method tests increment current player and the choose of the first player
     * */

    @Test
    public void incrementCurrentPlayerTest(){
        Player player1=new Player("playertest2830","gametest3782671", Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831","gametest3782671", Colors.GREEN, "playerTest2");
        Player player3=new Player("playertest2832","gametest3782671", Colors.GREEN, "playerTest3");
        Game game=new Game("gametest3782671");

        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);

        game.chooseFirstPlayer();
        Player playerTest=game.getFirstPlayer();

        game.incrementCurrentPlayer();
        assertEquals(game.getCurrentPlayer(),game.getPlayers().get(game.getPlayers().indexOf(playerTest)+1));
        game.incrementCurrentPlayer();
        game.incrementCurrentPlayer();
        assertEquals(game.getCurrentPlayer(),playerTest);
    }

}
