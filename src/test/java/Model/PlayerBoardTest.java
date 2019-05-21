package Model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerBoardTest  {
    /*
     * This method tests the constructor
     * */
    @Test
    public void testConstructor(){ ;
        Player player=new Player(2832, Colors.GREEN);
        assertNotNull(player.getPlayerBoard());
    }

    /**
     * this method tests the decrement of max reward
     * */
    @Test
    public void testDecrementMaxReward(){
        Player player=new Player(2832, Colors.GREEN);
        player.getPlayerBoard().decrementMaxReward();
        assertEquals(6,player.getPlayerBoard().getMaxReward());
    }

    /**
     * this method tests the add of points
     * */
    @Test
    public void testAddPoints(){
        Player player=new Player(2832, Colors.GREEN);
        player.getPlayerBoard().addPoints(4);
        assertEquals(4,player.getPlayerBoard().getPoints());
    }
}
