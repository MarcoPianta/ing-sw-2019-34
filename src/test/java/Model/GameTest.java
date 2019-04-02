package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void testConstructor(){
        Game game;

        game = new Game();
        assertTrue(game instanceof Game);
    }
    @Test
    public void testGetWinner(){
        Game game;

        game = new Game();
        assertNull(game.getWinner());
    }
}
