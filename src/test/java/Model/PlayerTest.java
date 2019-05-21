package Model;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    /*
     * This method tests the player's new position
     * */
    @Test
    public void newPositionTest(){
        Player player=new Player(2832, Colors.GREEN);
        NormalSquare normalSquare= new NormalSquare(null,null,null,null,null,null) ;
        assertNotSame(player.getPosition(), normalSquare);
        player.newPosition(normalSquare);
        assertSame(player.getPosition(), normalSquare);
    }

    /*
     *this method tests the spawn of player
     * */
    @Test
    public void spawnTest() throws  FileNotFoundException{
        Game game=new Game(5, "map1");
        Player player=new Player(2832, Colors.GREEN);
        game.addPlayer(player);
        player.spawn(1);
        assertEquals(1, player.getPlayerBoard().getHandPlayer().getPlayerPowerUps().size());
        //assertEquals(player.getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(0).getColor().getAbbreviation(),player.getPosition().getColor().getAbbreviation());
        assertTrue(player.getPosition().isSpawn());
    }

    /*
     *
     * */
    @Test
    public void calculateNewPosition()throws FileNotFoundException{
        Game game=new Game(5, "map1");
        Player player=new Player(2832, Colors.GREEN);
        game.addPlayer(player);
        player.spawn(1);
        CardPowerUp cardPowerUp=player.getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(0);
        player.calculateNewPosition(player.getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(0));
        assertEquals(cardPowerUp.getColor(),player.getPosition().getColor());
        assertTrue(player.getPosition().isSpawn());

    }
}
