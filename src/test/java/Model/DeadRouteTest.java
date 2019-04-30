package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeadRouteTest {
    /*
     * this method tests the deadRoute's constructor
     * */
    @Test
    public void deadRouteConstructorTest(){
        Game game=new Game(8);
        assertNotNull(game.getDeadRoute());
        assertEquals(8,game.getDeadRoute().getSkulls());
    }

    /*
     * this method tests when the deadRoute is empty and there is the calculation of points
     * */
    @Test
    public void addMurdersTest(){
        Game game=new Game(2);
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831",game, Colors.BLUE, "playerTest2");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.getDeadRoute().addMurders(player1,1);
        assertEquals(1,game.getDeadRoute().getMurders().size());
        assertEquals(1,game.getDeadRoute().getSkulls());
        game.getDeadRoute().addMurders(player2,2);

        assertEquals(8,player2.getPlayerBoard().getPoints());
        assertEquals(6,player1.getPlayerBoard().getPoints());
    }
}
