package Model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    /**
     * this method tests if the player can do an action and tests trivial decrementActionCounter
     * */
    @Test
    public void canActTest(){
        Player player=new Player("playertest2832","gametest3782671", Colors.GREEN, "playerTest");
        assertTrue(player.canAct());
        player.decrementActionCounter();
        player.decrementActionCounter();
        assertFalse(player.canAct());
    }

    /*
     *
     * */
    @Test
    public void newPositionTest(){
        Player player=new Player("playertest2832","gametest262518", Colors.GREEN, "playerTest");
        SpawnSquare spawnSquare= new SpawnSquare(null,null,null,null,null) ;
        assertFalse(player.getPosition()==spawnSquare);
        player.newPosition(spawnSquare);
        assertTrue(player.getPosition()==spawnSquare);
    }

}
