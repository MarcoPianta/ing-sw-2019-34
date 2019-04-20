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
        Colors colorTest=null;
        Game game=new Game("gametest");
        Player player=new Player("playertest2832",game , Colors.GREEN, "playerTest");
        player.getPlayerBoard().getHealthPlayer().addDamage(Colors.BLUE,2);
        player.getPlayerBoard().getHealthPlayer().addDamage(Colors.RED,2);
        player.getPlayerBoard().getHealthPlayer().addDamage(Colors.YELLOW,2);
        colorTest=player.firstDamage(Colors.BLUE,Colors.YELLOW,player.getPlayerBoard().getHealthPlayer().getDamageBar());
        assertEquals(Colors.BLUE,colorTest);
        colorTest=player.firstDamage(Colors.YELLOW,Colors.RED,player.getPlayerBoard().getHealthPlayer().getDamageBar());
        assertEquals(Colors.RED,colorTest);
    }
    /**
     * this method tests the count of damage
     * */
    @Test
    public void testCountColors(){
        Colors colorTest=null;
        int counterTest=0;
        Game game=new Game("gametest");
        Player player=new Player("playertest2832",game , Colors.GREEN, "playerTest");
        player.getPlayerBoard().getHealthPlayer().addDamage(Colors.BLUE,2);
        player.getPlayerBoard().getHealthPlayer().addDamage(Colors.RED,4);
        counterTest=player.countColors(Colors.BLUE,player.getPlayerBoard().getHealthPlayer().getDamageBar());
        assertEquals(2,counterTest);
        counterTest=player.countColors(Colors.RED,player.getPlayerBoard().getHealthPlayer().getDamageBar());
        assertEquals(4,counterTest);
    }


    /**
     * this method tests the creation of array with the best murders
     * */
    @Test
    public void testCalculatePoints(){
        ArrayList<Colors> bestMurder=new ArrayList<>();
        Game game=new Game("gametest");
        Player player=new Player("playertest2832",game , Colors.GREEN, "playerTest");
        player.getPlayerBoard().getHealthPlayer().addDamage(Colors.BLUE,2);
        player.getPlayerBoard().getHealthPlayer().addDamage(Colors.YELLOW,3);
        player.getPlayerBoard().getHealthPlayer().addDamage(Colors.RED,3);
        player.getPlayerBoard().getHealthPlayer().addDamage(Colors.VIOLET,2);


        bestMurder=player.calculatePoints(player.getPlayerBoard().getHealthPlayer().getDamageBar());
        assertEquals(Colors.BLUE,bestMurder.get(2));
        assertEquals(Colors.RED,bestMurder.get(1));
        assertEquals(Colors.YELLOW,bestMurder.get(0));
        assertEquals(Colors.VIOLET,bestMurder.get(3));

    }
    /**
     *
     * */
    @Test
    public void testAlreadyView(){
        ArrayList<Colors> bestMurder=new ArrayList<>();
        boolean isPresentTest;
        Game game=new Game("gametest");
        Player player=new Player("playertest2832",game , Colors.GREEN, "playerTest");
        bestMurder.add(Colors.BLUE);
        isPresentTest=player.notAlreadyView(Colors.BLUE,bestMurder);
        assertFalse(isPresentTest);
        isPresentTest=player.notAlreadyView(Colors.YELLOW,bestMurder);
        assertTrue(isPresentTest);

    }
    /**
     * this method tests if the player can do an action and tests trivial decrementActionCounter
     * */
    @Test
    public void canActTest(){
        Game game=new Game("gametest");
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
        Game game=new Game("gametest");
        Player player=new Player("playertest2832",game , Colors.GREEN, "playerTest");
        NormalSquare normalSquare= new NormalSquare(null,null,null,null,null,null) ;
        assertFalse(player.getPosition()==normalSquare);
        player.newPosition(normalSquare);
        assertTrue(player.getPosition()==normalSquare);
    }

}
