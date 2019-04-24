package Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthTest {

    /**
     * this method tests the count off marks
     * */
    @Test
    public void testCountMarksAddMarks(){
        Game game=new Game("gametest",8);
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831",game, Colors.BLUE, "playerTest2");
        Player player3=new Player("playertest2832",game, Colors.YELLOW, "playerTest3");
        Player playerTest=new Player("playertest2832",game, Colors.RED, "playerTest");

        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest",playerTest) ;
        playerBoard.getHealthPlayer().addMark(player1, 5 );
        playerBoard.getHealthPlayer().addMark(player2, 2 );

        assertEquals(3,playerBoard.getHealthPlayer().countMarks(player1));
        assertEquals(0,playerBoard.getHealthPlayer().countMarks(player3));
        assertEquals(2,playerBoard.getHealthPlayer().countMarks(player2));
    }
    /**
     * this method test the player's death
     * */
    @Test
    public void testDeath(){
        Game gameTest= new Game("gameTest",5);
        Player playerTest= new Player("playerTest1", gameTest,Colors.GREEN,"playertest");
        PlayerBoard playerBoard=new PlayerBoard(Colors.GREEN,"playerTest",playerTest);
        playerBoard.getHealthPlayer().death();
        assertEquals(0,playerBoard.getHealthPlayer().getDamageBar().size());
        assertEquals(0,playerBoard.getHealthPlayer().getAdrenalineAction());

    }

    /**
     * this method test the add of damage e the modified of adrenaline action
     * */
    @Test
    public void testAddDamage(){
        Game game=new Game("gametest",8);
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831",game, Colors.BLUE, "playerTest2");
        Player player3=new Player("playertest2832",game, Colors.YELLOW, "playerTest3");
        Player playerTest=new Player("playertest2832",game, Colors.RED, "playerTest");
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest",playerTest);

        int counterMark= playerBoard.getHealthPlayer().countMarks(player1);
        //case max damage and adrenaline action=2
        playerBoard.getHealthPlayer().addMark(player1,3);
        playerBoard.getHealthPlayer().addDamage(player1,9);
        assertEquals(0,playerBoard.getHealthPlayer().getDamageBar().size());
        assertEquals(0,playerBoard.getHealthPlayer().getAdrenalineAction());
        //assertEquals(6,playerBoard.getHealthPlayer().getMaxReward());
        assertEquals(0,playerBoard.getHealthPlayer().countMarks(player1));

        playerBoard.getHealthPlayer().addMark(player1,2);
        playerBoard.getHealthPlayer().addDamage(player1,1);
        assertEquals(3,playerBoard.getHealthPlayer().getDamageBar().size());
        assertEquals(1, playerBoard.getHealthPlayer().getAdrenalineAction());
    }
}
