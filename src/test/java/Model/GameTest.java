package Model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    /**
     * this method tests who do the first damage
     * */
    @Test
    public void testFirstDamage(){
        Game game=new Game("gametest",8);
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831",game, Colors.BLUE, "playerTest2");
        Player player3=new Player("playertest2832",game, Colors.YELLOW, "playerTest3");
        Player player4=new Player("playertest2832",game, Colors.VIOLET, "playerTest4");


        player1.getPlayerBoard().getHealthPlayer().addDamage(player2,2);
        player1.getPlayerBoard().getHealthPlayer().addDamage(player3,2);
        player1.getPlayerBoard().getHealthPlayer().addDamage(player4,2);

        assertEquals(player2,game.firstDamage(player2,player4,player1.getPlayerBoard().getHealthPlayer().getDamageBar()));
        assertEquals(player3,game.firstDamage(player4,player3,player1.getPlayerBoard().getHealthPlayer().getDamageBar()));
    }
    /**
     * this method tests the count of damage
     * */
    @Test
    public void testCountPlayers(){

        Game game=new Game("gametest",8);
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831",game, Colors.BLUE, "playerTest2");
        Player player3=new Player("playertest2832",game, Colors.YELLOW, "playerTest3");

        player1.getPlayerBoard().getHealthPlayer().addDamage(player2,2);
        player1.getPlayerBoard().getHealthPlayer().addDamage(player3,4);

        assertEquals(2,game.countPlayers(player2,player1.getPlayerBoard().getHealthPlayer().getDamageBar()));
        assertEquals(4,game.countPlayers(player3,player1.getPlayerBoard().getHealthPlayer().getDamageBar()));
    }


    /**
     * this method tests the creation of array with the best murders
     * */
    @Test
    public void testCalculatePoints(){
        ArrayList<Player> bestMurder=new ArrayList<Player>();
        Game game=new Game("gametest",8);
        Player player=new Player("playertest2832",game , Colors.RED, "playerTest");
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831",game, Colors.BLUE, "playerTest2");
        Player player3=new Player("playertest2832",game, Colors.YELLOW, "playerTest3");
        Player player4=new Player("playertest2832",game, Colors.VIOLET, "playerTest4");


        player.getPlayerBoard().getHealthPlayer().addDamage(player1,2);
        player.getPlayerBoard().getHealthPlayer().addDamage(player3,3);
        player.getPlayerBoard().getHealthPlayer().addDamage(player2,3);
        player.getPlayerBoard().getHealthPlayer().addDamage(player4,2);


        bestMurder=game.calculatePoints(player.getPlayerBoard().getHealthPlayer().getDamageBar(),false,player);
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
        Player player1=new Player("playertest2830",game, Colors.RED, "playerTest1");
        Player player2=new Player("playertest2831",game, Colors.BLUE, "playerTest2");


        bestMurder.add(player1);
        isPresentTest=game.notAlreadyView(player1,bestMurder);
        assertFalse(isPresentTest);
        isPresentTest=game.notAlreadyView(player2,bestMurder);
        assertTrue(isPresentTest);

    }

    /**
     * this method tests the adding of points after a kill
     * */
    @Test
    public void addPLayerPoints(){
        Game game=new Game("gametest",8);
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831",game, Colors.BLUE, "playerTest2");
        Player player3=new Player("playertest2832",game, Colors.YELLOW, "playerTest3");
        Player player4=new Player("playertest2832",game, Colors.RED, "playerTest3");
        Player player5=new Player("playertest2832",game, Colors.VIOLET, "playerTest3");
        ArrayList<Player> bestMurder=new ArrayList<>();

        bestMurder.add(player1);
        bestMurder.add(player2);
        bestMurder.add(player3);
        bestMurder.add(player4);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);
        game.addPlayer(player5);

        game.addPlayerPoints(bestMurder,player5);
        assertEquals(8,player1.getPlayerBoard().getPoints());
        assertEquals(6,player2.getPlayerBoard().getPoints());
        assertEquals(4,player3.getPlayerBoard().getPoints());
        assertEquals(2,player4.getPlayerBoard().getPoints());
        assertEquals(6,player5.getPlayerBoard().getMaxReward());

        game.addPlayerPoints(bestMurder,player5);
        assertEquals(14,player1.getPlayerBoard().getPoints());
        assertEquals(10,player2.getPlayerBoard().getPoints());
        assertEquals(6,player3.getPlayerBoard().getPoints());
        assertEquals(3,player4.getPlayerBoard().getPoints());
        assertEquals(4,player5.getPlayerBoard().getMaxReward());

        game.addPlayerPoints(bestMurder,player5);
        assertEquals(18,player1.getPlayerBoard().getPoints());
        assertEquals(12,player2.getPlayerBoard().getPoints());
        assertEquals(7,player3.getPlayerBoard().getPoints());
        assertEquals(4,player4.getPlayerBoard().getPoints());
        assertEquals(2,player5.getPlayerBoard().getMaxReward());
    }

    @Test
    public void addDeadRoutePointsTest(){
        Game game=new Game("gametest",3);
        ArrayList<Player> bestMurder=new ArrayList<>();
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831",game, Colors.BLUE, "playerTest2");
        Player player3=new Player("playertest2832",game, Colors.YELLOW, "playerTest3");
        bestMurder.add(player1);
        bestMurder.add(player2);
        bestMurder.add(player3);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addDeadRoutePoints(bestMurder);

        assertEquals(8,player1.getPlayerBoard().getPoints());
        assertEquals(6,player2.getPlayerBoard().getPoints());
        assertEquals(4,player3.getPlayerBoard().getPoints());


    }
    /*
     * this method tests the adding of new player
     * */
    @Test
    public void addPlayerTest(){
        Game game=new Game("gametest",8);
        Player player=new Player("playertest2832",game, Colors.GREEN, "playerTest");

        assertTrue(game.getPlayers().isEmpty() );
        game.addPlayer(player);
        assertFalse(game.getPlayers().isEmpty());
    }
    /*
     * this method tests increment current player and the choose of the first player
     * */

    @Test
    public void incrementCurrentPlayerTest(){
        Game game=new Game("gametest",5);
        Player player1=new Player("playertest2830",game, Colors.GREEN, "playerTest1");
        Player player2=new Player("playertest2831",game, Colors.RED, "playerTest2");
        Player player3=new Player("playertest2832",game, Colors.BLUE, "playerTest3");


        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);

        game.chooseFirstPlayer();
        Player playerTest=game.getFirstPlayer();

        game.incrementCurrentPlayer();
        if(game.getPlayers().indexOf(playerTest)==2)
            assertEquals(0,game.getPlayers().indexOf(game.getCurrentPlayer()));
        else
            assertEquals(game.getPlayers().indexOf(playerTest) +1,game.getPlayers().indexOf(game.getCurrentPlayer()));
        game.incrementCurrentPlayer();
        game.incrementCurrentPlayer();
        assertEquals(game.getCurrentPlayer(),playerTest);
    }

}
