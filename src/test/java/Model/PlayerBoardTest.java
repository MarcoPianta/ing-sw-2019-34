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
    public void testResetDamageBar(){
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest") ;
        ArrayList<Colors> damageBar = new ArrayList<Colors>();
        damageBar.add(Colors.BLUE);
        playerBoard.resetDamageBar();
        assertEquals(0,playerBoard.getDamageBar().size());


    }

    @Test
    public void testAddWeapon() throws FileNotFoundException {
        PlayerBoard playerBoard= new PlayerBoard(Colors.GREEN, "playerTest") ;
        String file= "electroscyte.json";
        CardWeapon cardWeapon=new CardWeapon(file);
        playerBoard.addWeapon(cardWeapon);
        assertNotNull(playerBoard.getPlayerWeapons());
    }



}
