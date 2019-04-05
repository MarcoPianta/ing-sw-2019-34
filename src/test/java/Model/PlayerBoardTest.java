package Model;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class PlayerBoardTest  {
/*
    @BeforeAll
    public void initPlayerBoard(){
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest") ;

    }
*/
    @Test
    public void testConstructor(){
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest") ;
        assertTrue(playerBoard instanceof  PlayerBoard);
        assertEquals(Colors.GREEN, playerBoard.getColor());
        assertEquals("playerTest", playerBoard.getPlayerName());

    }

    @Test
    public void testResetDamageBar (){
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest") ;
        ArrayList<Colors> damageBar = new ArrayList<Colors>();
        playerBoard.addDamage(Colors.BLUE,5);

        playerBoard.resetDamageBar();
        assertEquals(0,playerBoard.getDamageBar().size());
        assertEquals(0, playerBoard.getAdrenalineAction());
        assertEquals(6,playerBoard.getMaxReward());

    }

    @Test
    public void testResetMarks(){
        PlayerBoard playerBoard=new PlayerBoard(Colors.BLUE, "playertest");
        playerBoard.addMark(Colors.GREEN,3);
        playerBoard.addMark(Colors.BLUE,3);
        playerBoard.resetMark(Colors.GREEN);
        assertEquals(0,playerBoard.countMarks(Colors.GREEN));
        assertEquals(3,playerBoard.countMarks(Colors.BLUE));
    }

    @Test
    public void testAddWeapon() throws FileNotFoundException {
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest") ;
        String file= "electroscyte.json";
        CardWeapon cardWeapon=new CardWeapon(file);
        playerBoard.addWeapon(cardWeapon);
        assertEquals(1,playerBoard.getPlayerWeapons().size());
    }
  /*  @Test
    public void testSobstituteWeapons(){
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest") ;
        String file1= "electroscyte.json";
        CardWeapon cardWeapon1=new CardWeapon(file1);
        playerBoard.addWeapon(cardWeapon1);

        String file2= "electroscyte.json";
        CardWeapon cardWeapon2=new CardWeapon(file2);
        playerBoard.addWeapon(cardWeapon2);

        String file3= "electroscyte.json";
        CardWeapon cardWeapon3=new CardWeapon(file3);
        playerBoard.addWeapon(cardWeapon3);

        String file4= "electroscyte.json";
        CardWeapon cardWeapon4=new CardWeapon(file4);

        ArrayList<CardWeapon> newPlayerWeapons= new ArrayList<CardWeapon>();
        newPlayerWeapons.add(cardWeapon1);
        newPlayerWeapons.add(cardWeapon2);
        newPlayerWeapons.add(cardWeapon4);

        playerBoard.substituteWeapons(newPlayerWeapons);

        assertEquals(newPlayerWeapons,playerBoard.getPlayerWeapons());


    }
*/

   /* @Test
    public void testAddPowerUp() throws FileNotFoundException {
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest") ;
        String file= "";
        CardPowerUp cardPowerUp=new CardPowerUp(file);
        playerBoard.addPowerUp(cardPowerup);
        assertNotNull(playerBoard.getPlayerPowersUps());
        //assertFalse(playerBoard.getPlayerPowersUps().isEmpty());
    }

    */


    @Test
    public void testCountMarksAddMarks(){
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest") ;
        playerBoard.addMark(Colors.BLUE, 3 );
        playerBoard.addMark(Colors.VIOLET, 2 );

        assertEquals(3,playerBoard.countMarks(Colors.BLUE));
        assertEquals(0,playerBoard.countMarks(Colors.YELLOW));
        assertEquals(2,playerBoard.countMarks(Colors.VIOLET));

    }
    @Test
    public void testAddDamage(){
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest");
        int counterMark= playerBoard.countMarks(Colors.BLUE);
        //case max damege and andrenaline action=2
        playerBoard.addMark(Colors.BLUE,10);
        playerBoard.addDamage(Colors.BLUE,5);
        assertEquals(12,playerBoard.getDamageBar().size());
        assertEquals(2,playerBoard.getAdrenalineAction());

        //Case andrenaline action=1
        playerBoard.resetDamageBar();
        playerBoard.addMark(Colors.BLUE,2);
        playerBoard.addDamage(Colors.BLUE,1);
        assertEquals(1, playerBoard.getAdrenalineAction());
    }

    @Test
    public void testDecrementMaxReward(){
        PlayerBoard playerBoard=new PlayerBoard(Colors.BLUE,"playertest");
        playerBoard.decrementMaxReward();
        assertEquals(6,playerBoard.getMaxReward());
    }

    @Test
    public void testAddPoints(){
        PlayerBoard playerBoard=new PlayerBoard(Colors.BLUE,"playertest");
        playerBoard.addPoints(4);
        assertEquals(4,playerBoard.getPoints());
    }
}
