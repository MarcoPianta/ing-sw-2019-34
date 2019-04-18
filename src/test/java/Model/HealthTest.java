package Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthTest {

    /**
     * this method tests the count off marks
     * */
    @Test
    public void testCountMarksAddMarks(){
        Player playerTest=new Player();
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest",playerTest) ;
        playerBoard.getHealthPlayer().addMark(Colors.BLUE, 5 );
        playerBoard.getHealthPlayer().addMark(Colors.VIOLET, 2 );

        assertEquals(3,playerBoard.getHealthPlayer().countMarks(Colors.BLUE));
        assertEquals(0,playerBoard.getHealthPlayer().countMarks(Colors.YELLOW));
        assertEquals(2,playerBoard.getHealthPlayer().countMarks(Colors.VIOLET));
    }
    /**
     * this method test the player's death
     * */
    @Test
    public void testDeath(){
        Game gameTest= new Game("gameTest");
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
        Player playerTest=new Player();
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest",playerTest);

        int counterMark= playerBoard.getHealthPlayer().countMarks(Colors.BLUE);
        //case max damage and adrenaline action=2
        playerBoard.getHealthPlayer().addMark(Colors.BLUE,3);
        playerBoard.getHealthPlayer().addDamage(Colors.BLUE,9);
        assertEquals(0,playerBoard.getHealthPlayer().getDamageBar().size());
        assertEquals(0,playerBoard.getHealthPlayer().getAdrenalineAction());
        //assertEquals(6,playerBoard.getHealthPlayer().getMaxReward());
        assertEquals(0,playerBoard.getHealthPlayer().countMarks(Colors.BLUE));

        playerBoard.getHealthPlayer().addMark(Colors.BLUE,2);
        playerBoard.getHealthPlayer().addDamage(Colors.BLUE,1);
        assertEquals(3,playerBoard.getHealthPlayer().getDamageBar().size());
        assertEquals(1, playerBoard.getHealthPlayer().getAdrenalineAction());
    }
}
