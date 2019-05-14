package Model;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HealthTest {
    /**
     * this method tests the count of marks
     * */
    @Test
    public void testCountMarksAddMarks(){
        Player player1=new Player("playertest2830", Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831", Colors.BLUE, "playerTest2");
        Player playerTest=new Player("playertest2832", Colors.RED, "playerTest");

        PlayerBoard playerBoard= new PlayerBoard(playerTest) ;
        playerBoard.getHealthPlayer().addMark(player1, 5 );
        assertEquals(3,playerBoard.getHealthPlayer().getMark().size());
        playerBoard.getHealthPlayer().addMark(player2, 2 );
        assertEquals(5,playerBoard.getHealthPlayer().getMark().size());
    }
    /**
     * this method test the player's death
     * */
    @Test
    public void testDeath() throws FileNotFoundException{
        Game game = new Game(5,"map1");
        Player playerTest = new Player("playerTest1", Colors.GREEN, "playertest");
        Player player1 = new Player("playertest2830", Colors.RED, "playerTest1");
        Player player2 = new Player("playertest2830", Colors.BLUE, "playerTest2");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(playerTest);

        playerTest.getPlayerBoard().getHealthPlayer().addDamage(player1, 12);
        player2.getPlayerBoard().getHealthPlayer().addDamage(player1, 11);
        assertEquals(playerTest,game.getDeadPlayer().get(0));
        playerTest.getPlayerBoard().getHealthPlayer().death();
        assertEquals(0, playerTest.getPlayerBoard().getHealthPlayer().getDamageBar().size());
        assertEquals(0, playerTest.getPlayerBoard().getHealthPlayer().getAdrenalineAction());
        assertEquals(playerTest, player1.getPlayerBoard().getHealthPlayer().getMark().get(0));
        assertNull(playerTest.getPosition());
        assertEquals(2, playerTest.getPlayerBoard().getPlayer().getGameId().getDeadRoute().getMurders().size());
        assertEquals(10,player1.getPlayerBoard().getPoints());//first blood,bestMurder amd double kill

        player2.getPlayerBoard().getHealthPlayer().death();
        playerTest.getPlayerBoard().getHealthPlayer().addDamage(player1, 11);
        assertEquals(playerTest, player1.getPlayerBoard().getHealthPlayer().getMark().get(0));
        assertEquals(3, playerTest.getPlayerBoard().getPlayer().getGameId().getDeadRoute().getMurders().size());
        assertEquals(19,player1.getPlayerBoard().getPoints());
    }

    /**
     * this method test the add of damage e the modified of adrenaline action
     * */
    @Test
    public void testAddDamage()throws FileNotFoundException{
        Game game=new Game(5,"map1");
        Player player1=new Player("playertest2830", Colors.GREEN, "playerTest1");
        Player playerTest=new Player("playertest2832", Colors.RED, "playerTest");
        game.addPlayer(player1);
        game.addPlayer(playerTest);
        //case max damage and adrenaline action=2
        playerTest.getPlayerBoard().getHealthPlayer().addMark(player1,3);
        playerTest.getPlayerBoard().getHealthPlayer().addDamage(player1,4);
        assertEquals(2,playerTest.getPlayerBoard().getHealthPlayer().getAdrenalineAction());
        playerTest.getPlayerBoard().getHealthPlayer().addDamage(player1,6);
        assertEquals(12 ,playerTest.getPlayerBoard().getHealthPlayer().getDamageBar().size());
        assertEquals(2,playerTest.getPlayerBoard().getHealthPlayer().getAdrenalineAction());
        assertEquals(8,playerTest.getPlayerBoard().getMaxReward());
        assertEquals(0,playerTest.getPlayerBoard().getHealthPlayer().getMark().size());
        assertEquals(playerTest, playerTest.getGameId().getDeadPlayer().get(0));

    }
    /*
     *This method tests all method of a player's kill
     * */

    @Test
    public void killTest()throws FileNotFoundException{
        Game game=new Game(8,"map1");
        Player player5=new Player("playertest2832", Colors.RED, "playerTest");
        Player player1=new Player("playertest2830", Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831", Colors.BLUE, "playerTest2");
        Player player3=new Player("playertest2832", Colors.YELLOW, "playerTest3");
        Player player4=new Player("playertest2832", Colors.VIOLET, "playerTest4");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);
        game.addPlayer(player5);

        player1.getPlayerBoard().getHealthPlayer().addDamage(player2,2);
        player1.getPlayerBoard().getHealthPlayer().addDamage(player3,3);
        player1.getPlayerBoard().getHealthPlayer().addDamage(player4,1);
        player1.getPlayerBoard().getHealthPlayer().addDamage(player5,3);
        player1.getPlayerBoard().getHealthPlayer().addDamage(player2,1);
        player1.getPlayerBoard().getHealthPlayer().addDamage(player4,2);
        player1.getPlayerBoard().getHealthPlayer().death();// in reality controller called death's method
        assertEquals(9,player2.getPlayerBoard().getPoints()); // 8+first Blood
        assertEquals(6,player3.getPlayerBoard().getPoints());
        assertEquals(4,player4.getPlayerBoard().getPoints());
        assertEquals(2,player5.getPlayerBoard().getPoints());

        assertEquals(0,player1.getPlayerBoard().getHealthPlayer().getDamageBar().size());
        assertEquals(0,player1.getPlayerBoard().getHealthPlayer().getAdrenalineAction());
        assertEquals(6,player1.getPlayerBoard().getMaxReward());
        assertEquals(player1,player4.getPlayerBoard().getHealthPlayer().getMark().get(0));
        assertEquals(7,game.getDeadRoute().getSkulls());
        assertEquals(2,game.getDeadRoute().getMurders().size());
    }
}
