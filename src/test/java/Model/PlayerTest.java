package Model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    /**
     * this method tests if the player can do an action and tests trivial decrementActionCounter
     * */
    @Test
    public void canActTest(){
        Player player=new Player("polimiserver2832", Colors.GREEN, "playerTest");
        assertTrue(player.canAct());
        player.decrementActionCounter();
        player.decrementActionCounter();
        assertFalse(player.canAct());
    }

    @Test
    public void newPositionTest(){
        Player player=new Player("polimiserver2832", Colors.GREEN, "playerTest");
        SpawnSquare spawnSquare= new SpawnSquare(null,null,null,null,null) ;
        assertFalse(player.getPosition()==spawnSquare);
        player.newPosition(spawnSquare);
        assertTrue(player.getPosition()==spawnSquare);
    }

}
