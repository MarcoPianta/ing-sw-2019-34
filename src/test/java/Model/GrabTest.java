package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GrabTest {
    /**
     * This method test all the three constructor methods
     */
    @Test
    public void ConstructorTest(){
        //TODO fix when the class related to Player will be finished --> control if the Item has been grabbed
        Colors color = null;
        Player testPlayer = new Player("playerID", "gameID", color, "name" );
        CardWeapon weapon = null;
        Grab action = new Grab();
        action.Grab(testPlayer, weapon);
        assertTrue(action instanceof Grab );

        CardOnlyAmmo onlyAmmo= null;
        action.Grab(testPlayer, onlyAmmo);
        assertTrue(action instanceof Grab );

        CardNotOnlyAmmo notOnlyAmmo = null;
        action.Grab(testPlayer, notOnlyAmmo);
        assertTrue(action instanceof Grab );
    }

    @Test
    public void isValidTest(){
        //TODO the method isValid has to be finished
    }

    @Test
    public void executeTest(){
        //TODO the method execute has to be finished
    }
}
