package Model;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    /**
     * this method tests who do the first damage
     * */
    @Test
    public void testFirstDamage(){
        Game game=new Game("gametest",8);
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831",game, Colors.BLUE, "playerTest2");
        Player player3=new Player("playertest2832",game, Colors.YELLOW, "playerTest3");
        Player player4=new Player("playertest2832",game, Colors.YELLOW, "playerTest4");


        player1.getPlayerBoard().getHealthPlayer().addDamage(player2,2);
        player1.getPlayerBoard().getHealthPlayer().addDamage(player3,2);
        player1.getPlayerBoard().getHealthPlayer().addDamage(player4,2);

        assertEquals(player2,player1.firstDamage(player2,player4,player1.getPlayerBoard().getHealthPlayer().getDamageBar()));
        assertEquals(player3,player1.firstDamage(player4,player3,player1.getPlayerBoard().getHealthPlayer().getDamageBar()));
    }
    /**
     * this method tests the count of damage
     * */
    @Test
    public void testCountColors(){

        Game game=new Game("gametest",8);
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831",game, Colors.BLUE, "playerTest2");
        Player player3=new Player("playertest2832",game, Colors.YELLOW, "playerTest3");
        Player player4=new Player("playertest2832",game, Colors.YELLOW, "playerTest4");

        player1.getPlayerBoard().getHealthPlayer().addDamage(player2,2);
        player1.getPlayerBoard().getHealthPlayer().addDamage(player3,4);

        assertEquals(2,player1.countColors(player2,player1.getPlayerBoard().getHealthPlayer().getDamageBar()));
        assertEquals(4,player1.countColors(player3,player1.getPlayerBoard().getHealthPlayer().getDamageBar()));
    }


    /**
     * this method tests the creation of array with the best murders
     * */
    @Test
    public void testCalculatePoints(){
        ArrayList<Player> bestMurder=new ArrayList<Player>();
        Game game=new Game("gametest",8);
        Player player=new Player("playertest2832",game , Colors.GREEN, "playerTest");
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831",game, Colors.BLUE, "playerTest2");
        Player player3=new Player("playertest2832",game, Colors.YELLOW, "playerTest3");
        Player player4=new Player("playertest2832",game, Colors.YELLOW, "playerTest4");


        player.getPlayerBoard().getHealthPlayer().addDamage(player1,2);
        player.getPlayerBoard().getHealthPlayer().addDamage(player3,3);
        player.getPlayerBoard().getHealthPlayer().addDamage(player2,3);
        player.getPlayerBoard().getHealthPlayer().addDamage(player4,2);


        bestMurder=player.calculatePoints(player.getPlayerBoard().getHealthPlayer().getDamageBar());
        assertEquals(player1,bestMurder.get(2));
        assertEquals(player2,bestMurder.get(1));
        assertEquals(player3,bestMurder.get(0));
        assertEquals(player4,bestMurder.get(3));

    }
    /**
     *
     * */
    @Test
    public void testAlreadyView(){
        ArrayList<Player> bestMurder=new ArrayList<>();
        boolean isPresentTest;
        Game game=new Game("gametest",8);
        Player player=new Player("playertest2832",game , Colors.GREEN, "playerTest");
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831",game, Colors.BLUE, "playerTest2");
        Player player3=new Player("playertest2832",game, Colors.YELLOW, "playerTest3");
        Player player4=new Player("playertest2832",game, Colors.YELLOW, "playerTest4");

        bestMurder.add(player1);
        isPresentTest=player.notAlreadyView(player1,bestMurder);
        assertFalse(isPresentTest);
        isPresentTest=player.notAlreadyView(player2,bestMurder);
        assertTrue(isPresentTest);

    }
    /**
     * this method tests if the player can do an action and tests trivial decrementActionCounter
     * */
    @Test
    public void canActTest(){
        Game game=new Game("gametest",5);
        Player player=new Player("playertest2832",game , Colors.GREEN, "playerTest");
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
        Game game=new Game("gametest",5);
        Player player=new Player("playertest2832",game , Colors.GREEN, "playerTest");
        NormalSquare normalSquare= new NormalSquare(null,null,null,null,null,null) ;
        assertFalse(player.getPosition()==normalSquare);
        player.newPosition(normalSquare);
        assertTrue(player.getPosition()==normalSquare);
    }

}
