package Model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class PlayerBoardTest  {

    @Test
    public void testConstructor(){
        Player playerTest=new Player();
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest",playerTest) ;
        assertTrue(playerBoard instanceof  PlayerBoard);
        assertEquals(Colors.GREEN, playerBoard.getColor());
        assertEquals("playerTest", playerBoard.getPlayerName());
    }



    /**
     * this method tests the decrement of max reward
     * */
    @Test
    public void testDecrementMaxReward(){
        Player playerTest=new Player();
        PlayerBoard playerBoard=new PlayerBoard(Colors.BLUE,"playertest",playerTest);
        playerBoard.decrementMaxReward();
        assertEquals(6,playerBoard.getMaxReward());
    }

    /**
     * this method tests the add of points
     * */
    @Test
    public void testAddPoints(){
        Player playerTest=new Player();
        PlayerBoard playerBoard=new PlayerBoard(Colors.BLUE,"playertest",playerTest);
        playerBoard.addPoints(4);
        assertEquals(4,playerBoard.getPoints());
    }
}
