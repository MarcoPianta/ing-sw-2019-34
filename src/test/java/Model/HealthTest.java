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
        Game game= new Game("gameTest",5);
        Player playerTest= new Player("playerTest1", game,Colors.GREEN,"playertest");
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");

        playerTest.getPlayerBoard().getHealthPlayer().addDamage(player1,12);
        assertEquals(0,playerTest.getPlayerBoard().getHealthPlayer().getDamageBar().size());
        assertEquals(0,playerTest.getPlayerBoard().getHealthPlayer().getAdrenalineAction());
        assertEquals(1,player1.getPlayerBoard().getHealthPlayer().countMarks(playerTest));
        assertEquals(2,playerTest.getPlayerBoard().getPlayer().getGameId().getDeadRoute().getMurders().size());

        playerTest.getPlayerBoard().getHealthPlayer().addDamage(player1,11);
        assertEquals(1,player1.getPlayerBoard().getHealthPlayer().countMarks(playerTest));
        assertEquals(3,playerTest.getPlayerBoard().getPlayer().getGameId().getDeadRoute().getMurders().size());

    }

    /**
     * this method test the add of damage e the modified of adrenaline action
     * */
    @Test
    public void testAddDamage(){
        Game game=new Game("gametest",8);
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");
        Player playerTest=new Player("playertest2832",game, Colors.RED, "playerTest");


        //case max damage and adrenaline action=2
        playerTest.getPlayerBoard().getHealthPlayer().addMark(player1,3);
        playerTest.getPlayerBoard().getHealthPlayer().addDamage(player1,9);
        assertEquals(0,playerTest.getPlayerBoard().getHealthPlayer().getDamageBar().size());
        assertEquals(0,playerTest.getPlayerBoard().getHealthPlayer().getAdrenalineAction());
        assertEquals(6,playerTest.getPlayerBoard().getMaxReward());
        assertEquals(0,playerTest.getPlayerBoard().getHealthPlayer().countMarks(player1));

        playerTest.getPlayerBoard().getHealthPlayer().addMark(player1,2);
        playerTest.getPlayerBoard().getHealthPlayer().addDamage(player1,1);
        assertEquals(3,playerTest.getPlayerBoard().getHealthPlayer().getDamageBar().size());
        assertEquals(1, playerTest.getPlayerBoard().getHealthPlayer().getAdrenalineAction());
    }

    @Test
    public void killTest(){
        Game game=new Game("gametest",8);
        Player player5=new Player("playertest2832",game , Colors.RED, "playerTest");
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831",game, Colors.BLUE, "playerTest2");
        Player player3=new Player("playertest2832",game, Colors.YELLOW, "playerTest3");
        Player player4=new Player("playertest2832",game, Colors.VIOLET, "playerTest4");

        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);
        game.addPlayer(player5);

        player1.getPlayerBoard().getHealthPlayer().addDamage(player2,5);
        player1.getPlayerBoard().getHealthPlayer().addDamage(player3,4);
        player1.getPlayerBoard().getHealthPlayer().addDamage(player4,4);

        assertEquals(9,player2.getPlayerBoard().getPoints()); // 8+first Blood
        assertEquals(6,player3.getPlayerBoard().getPoints());
        assertEquals(4,player4.getPlayerBoard().getPoints());
        assertEquals(0,player5.getPlayerBoard().getPoints());

        assertEquals(0,player1.getPlayerBoard().getHealthPlayer().getDamageBar().size());
        assertEquals(0,player1.getPlayerBoard().getHealthPlayer().getAdrenalineAction());
        assertEquals(6,player1.getPlayerBoard().getMaxReward());
        assertEquals(1,player4.getPlayerBoard().getHealthPlayer().countMarks(player1));
        assertEquals(7,game.getDeadRoute().getSkulls());
        assertEquals(2,game.getDeadRoute().getMurders().size());


    }
}
