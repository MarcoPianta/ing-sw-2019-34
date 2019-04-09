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

    /**
     * this method tests the adding of a new weapon and the removal of  ammo
     */
    @Test
    public void testAddWeapon() throws FileNotFoundException {
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest") ;
        String file= WeaponDictionary.FURNACE.getAbbreviation();
        CardWeapon cardWeapon=new CardWeapon(file);
        playerBoard.addAmmo(3,3,3);
        playerBoard.addWeapon(cardWeapon);
        assertFalse(playerBoard.getPlayerWeapons().isEmpty());

    }

    /**
     * this method tests the adding of a new weapon when there are already three weapons
     * */
    @Test
    public void testSubstituteWeapons() throws FileNotFoundException{
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest") ;
        String file1= WeaponDictionary.ELECTROSCYTE.getAbbreviation();
        CardWeapon cardWeapon1=new CardWeapon(file1);
        playerBoard.addWeapon(cardWeapon1);

        String file2= WeaponDictionary.CYBERBLADE.getAbbreviation();
        CardWeapon cardWeapon2=new CardWeapon(file2);
        playerBoard.addWeapon(cardWeapon2);

        String file3= WeaponDictionary.FLAMETHROWER.getAbbreviation();
        CardWeapon cardWeapon3=new CardWeapon(file3);
        playerBoard.addWeapon(cardWeapon3);

        String file4= WeaponDictionary.FURNACE.getAbbreviation();
        CardWeapon cardWeapon4=new CardWeapon(file4);

        ArrayList<CardWeapon> newPlayerWeapons= new ArrayList<CardWeapon>();
        newPlayerWeapons.add(cardWeapon1);
        newPlayerWeapons.add(cardWeapon2);
        newPlayerWeapons.add(cardWeapon4);

        playerBoard.substituteWeapons(newPlayerWeapons);

        assertEquals(newPlayerWeapons,playerBoard.getPlayerWeapons());

    }

    /**
     * this method tests the adding of power up
     * */
    @Test
    public void testAddPowerUp() throws FileNotFoundException {
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest") ;
        String file= PowerUpEnum.NEWTON_B.getAbbreviation();
        CardPowerUp cardPowerUp=new CardPowerUp(file);
        playerBoard.addPowerUp(cardPowerUp);
        assertFalse(playerBoard.getPlayerPowerUps().isEmpty());
    }

    /**
    * this method tests the adding of a offload weapon
    * */
    @Test
    public void testAddOffloadWeapon() throws FileNotFoundException{
       PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest") ;
       String file= WeaponDictionary.FURNACE.getAbbreviation();
       CardWeapon cardWeapon=new CardWeapon(file);
       playerBoard.addOffloadWeapon(cardWeapon);
       assertEquals(1,playerBoard.getPlayerOffloadWeapons().size());
    }

    /**
     * this method tests the charge of a weapon
     * */
    @Test
    public void testChargeWeapon() throws FileNotFoundException{
       PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest") ;
       String file1= WeaponDictionary.FURNACE.getAbbreviation();
       CardWeapon cardWeapon1=new CardWeapon(file1);
       String file2= WeaponDictionary.ELECTROSCYTE.getAbbreviation();
       CardWeapon cardWeapon2=new CardWeapon(file2);
       String file= PowerUpEnum.NEWTON_B.getAbbreviation();
       CardPowerUp cardPowerUp=new CardPowerUp(file);

       int red=3;
       int yellow=3;
       int blue=3;
       playerBoard.addAmmo(red,yellow,blue);
       playerBoard.addOffloadWeapon(cardWeapon1);
       playerBoard.addOffloadWeapon(cardWeapon2);
       playerBoard.addPowerUp(cardPowerUp);

       playerBoard.chargeWeapon(cardWeapon1, 1,0,1);
       assertEquals(1,playerBoard.getPlayerOffloadWeapons().size());
       assertEquals(2, playerBoard.getAmmoRYB()[0]);
       assertEquals(3, playerBoard.getAmmoRYB()[1]);
       assertEquals(2, playerBoard.getAmmoRYB()[2]);

       playerBoard.chargeWeapon(cardWeapon2,0,0,1,cardPowerUp);
       assertEquals(0,playerBoard.getPlayerOffloadWeapons().size());
       assertEquals(2, playerBoard.getAmmoRYB()[0]);
       assertEquals(3, playerBoard.getAmmoRYB()[1]);
       assertEquals(2, playerBoard.getAmmoRYB()[2]);
       assertEquals(0,playerBoard.getPlayerPowerUps().size());
   }
    /**
     * this method tests the adding off ammo
     * */
    @Test
    public  void testAddAmmo(){
       PlayerBoard playerBoard= new PlayerBoard(Colors.BLUE, "playertest");
       int red=2;
       int yellow=1;
       int blue=0;
       playerBoard.addAmmo(red,yellow,blue);
       assertEquals(2, playerBoard.getAmmoRYB()[0]);
       assertEquals(1, playerBoard.getAmmoRYB()[1]);
       assertEquals(0, playerBoard.getAmmoRYB()[2]);

       red=2;
       playerBoard.addAmmo(red,yellow,blue);
       assertEquals(3,playerBoard.getAmmoRYB()[0]);
    }

    /**
     * this method tests the count off marks
     * */
    @Test
    public void testCountMarksAddMarks(){
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest") ;
        playerBoard.addMark(Colors.BLUE, 5 );
        playerBoard.addMark(Colors.VIOLET, 2 );

        assertEquals(3,playerBoard.countMarks(Colors.BLUE));
        assertEquals(0,playerBoard.countMarks(Colors.YELLOW));
        assertEquals(2,playerBoard.countMarks(Colors.VIOLET));

    }

    /**
     * this method test the add of damage e the modified of adrenaline action
     * */
    @Test
    public void testAddDamage(){
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest");
        int counterMark= playerBoard.countMarks(Colors.BLUE);
        //case max damage and adrenaline action=2
        playerBoard.addMark(Colors.BLUE,3);
        playerBoard.addDamage(Colors.BLUE,9);
        assertEquals(0,playerBoard.getDamageBar().size());
        assertEquals(0,playerBoard.getAdrenalineAction());
        assertEquals(6,playerBoard.getMaxReward());
        assertEquals(0,playerBoard.countMarks(Colors.BLUE));

        playerBoard.addMark(Colors.BLUE,2);
        playerBoard.addDamage(Colors.BLUE,1);
        assertEquals(3,playerBoard.getDamageBar().size());
        assertEquals(1, playerBoard.getAdrenalineAction());
    }

    /**
     * this method tests the decrement of max reward
     * */
    @Test
    public void testDecrementMaxReward(){
        PlayerBoard playerBoard=new PlayerBoard(Colors.BLUE,"playertest");
        playerBoard.decrementMaxReward();
        assertEquals(6,playerBoard.getMaxReward());

    }

    /**
     * this method tests the add of points
     * */
    @Test
    public void testAddPoints(){
        PlayerBoard playerBoard=new PlayerBoard(Colors.BLUE,"playertest");
        playerBoard.addPoints(4);
        assertEquals(4,playerBoard.getPoints());
    }
}
