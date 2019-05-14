package Model;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    /**
     * this method tests if the player can do an action and tests trivial decrementActionCounter
     * */
    @Test
    public void canActTest(){
        Player player=new Player("playertest2832", Colors.GREEN, "playerTest");
        assertTrue(player.canAct());
        player.decrementActionCounter();
        player.decrementActionCounter();
        assertFalse(player.canAct());
    }

    /*
     * This method tests the player's new position
     * */
    @Test
    public void newPositionTest(){
        Player player=new Player("playertest2832", Colors.GREEN, "playerTest");
        NormalSquare normalSquare= new NormalSquare(null,null,null,null,null,null) ;
        assertNotSame(player.getPosition(), normalSquare);
        player.newPosition(normalSquare);
        assertSame(player.getPosition(), normalSquare);
    }

    /*
     *this method tests the spawn of player and his internal method
     * */

    @Test
    public void spawnAndCalculateNewPositionTest() throws  FileNotFoundException{
        Game game=new Game(5, "map1");
        Player player=new Player("playertest2832", Colors.GREEN, "playerTest");
        game.addPlayer(player);
        player.spawn();
        assertEquals(1, player.getPlayerBoard().getHandPlayer().getPlayerPowerUps().size());
        //assertEquals(player.getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(0).getColor().getAbbreviation(),player.getPosition().getColor().getAbbreviation());
        assertTrue(player.getPosition().isSpawn());
    }
}
