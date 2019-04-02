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
    @Test
    public void testCalcolaPunteggio(){
        Game game;
        Player p;
        p = new Player();

        game = new Game();
        assertEquals(0, game.calcolaPunteggio(p));

    }
}
