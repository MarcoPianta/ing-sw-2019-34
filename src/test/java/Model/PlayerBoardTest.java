package Model;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerBoardTest  {
    /*
     * This method tests the constructor
     * */
    @Test
    public void testConstructor() throws FileNotFoundException {
        Game game=new Game(5,"map1");
        Player player=new Player("playertest2832",game , Colors.GREEN, "playerTest");
        assertNotNull(player.getPlayerBoard());
    }

    /**
     * this method tests the decrement of max reward
     * */
    @Test
    public void testDecrementMaxReward()throws FileNotFoundException{
        Game game=new Game(5,"map1");
        Player player=new Player("playertest2832",game , Colors.GREEN, "playerTest");
        player.getPlayerBoard().decrementMaxReward();
        assertEquals(6,player.getPlayerBoard().getMaxReward());
    }

    /**
     * this method tests the add of points
     * */
    @Test
    public void testAddPoints()throws FileNotFoundException{
        Game game=new Game(5,"map1");
        Player player=new Player("playertest2832",game , Colors.GREEN, "playerTest");
        player.getPlayerBoard().addPoints(4);
        assertEquals(4,player.getPlayerBoard().getPoints());
    }
}
