package Model;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
    }

    /*
     *
     * */
    @Test
    public void calculateNewPosition()throws FileNotFoundException{
        Game game=new Game(5, "map1");
        Player player=new Player(2832, Colors.GREEN);
        game.addPlayer(player);
        NormalSquare normalSquare= new NormalSquare(null,null,null,null,null,null) ;
        player.newPosition(normalSquare);
        assertFalse(player.getPosition().isSpawn());

        String file= PowerUpEnum.NEWTON_B.getAbbreviation();
        CardPowerUp cardPowerUp=new CardPowerUp(file);
        game.getPlayers().get(0).getPlayerBoard().getHandPlayer().addPowerUp(cardPowerUp);
        player.calculateNewPosition(player.getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(0));

        //assertEquals(Colors.BLUE.getAbbreviation(),player.getPosition().getColor().getAbbreviation());
        assertTrue(player.getPosition().isSpawn());

    }

    @Test
    public void calculateMaxAmmoTest()throws FileNotFoundException{
        Game game=new Game(5, "map1");
        Player player=new Player(2832, Colors.GREEN);
        game.addPlayer(player);

        String file= PowerUpEnum.NEWTON_B.getAbbreviation();
        CardPowerUp cardPowerUp=new CardPowerUp(file);
        String file2= PowerUpEnum.TARGETTINGSCOPE_R.getAbbreviation();
        CardPowerUp cardPowerUp2=new CardPowerUp(file2);
        game.getPlayers().get(0).getPlayerBoard().getHandPlayer().addPowerUp(cardPowerUp);
        game.getPlayers().get(0).getPlayerBoard().getHandPlayer().addPowerUp(cardPowerUp2);
        game.getPlayers().get(0).getPlayerBoard().getHandPlayer().addAmmo(1,2,2);

        Integer[] ammo;
        ammo=game.getPlayers().get(0).calculateMaxAmmo(true);
        assertEquals(2,ammo[0]);
        assertEquals(2,ammo[1]);
        assertEquals(3,ammo[2]);

        ammo=game.getPlayers().get(0).calculateMaxAmmo(false);
        assertEquals(2,ammo[0]);
        assertEquals(2,ammo[1]);
        assertEquals(3,ammo[2]);
    }

    @Test
    public void isValidCostTest()throws FileNotFoundException{
        Game game=new Game(5, "map1");
        Player player=new Player(2832, Colors.GREEN);
        game.addPlayer(player);

        Integer[] cost = {1, 2, 1};
        assertFalse(game.getPlayers().get(0).isValidCost(1));
        assertFalse(game.getPlayers().get(0).isValidCost(cost,false));
        game.getPlayers().get(0).getPlayerBoard().getHandPlayer().addAmmo(1,2,1);
        assertTrue(game.getPlayers().get(0).isValidCost(cost,false));

        String file= PowerUpEnum.TARGETTINGSCOPE_R.getAbbreviation();
        CardPowerUp cardPowerUp=new CardPowerUp(file);
        System.out.println(cardPowerUp.getWhen());
        game.getPlayers().get(0).getPlayerBoard().getHandPlayer().addPowerUp(cardPowerUp);

        assertFalse(game.getPlayers().get(0).isValidCost(cost,true));
        game.getPlayers().get(0).getPlayerBoard().getHandPlayer().addAmmo(1,0,0);
        assertTrue(game.getPlayers().get(0).isValidCost(cost,true));

        assertTrue(game.getPlayers().get(0).isValidCost(1));
    }
}
