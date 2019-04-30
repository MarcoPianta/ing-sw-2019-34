package Model;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    /**
     * this method tests if the player can do an action and tests trivial decrementActionCounter
     * */
    @Test
    public void canActTest(){
        Game game=new Game(5);
        Player player=new Player("playertest2832",game , Colors.GREEN, "playerTest");
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
        Game game=new Game(5);
        Player player=new Player("playertest2832",game , Colors.GREEN, "playerTest");
        NormalSquare normalSquare= new NormalSquare(null,null,null,null,null,null) ;
        assertNotSame(player.getPosition(), normalSquare);
        player.newPosition(normalSquare);
        assertSame(player.getPosition(), normalSquare);
    }
}
