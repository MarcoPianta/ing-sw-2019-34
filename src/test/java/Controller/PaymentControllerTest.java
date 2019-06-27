package Controller;

import Model.CardPowerUp;
import Model.Colors;
import Model.Player;
import Model.PowerUpEnum;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentControllerTest {

    /**
     * In this test when the player start his turn receive 2 random powerUp, then he draw another random powerUp
     * in the first group of assertion he has to pay whit his powerUp and also his ammo
     * in the second group of assertion he has to pay whit only with his ammo
     * */
    @Test
    public void paymentTest() throws FileNotFoundException {
        int[] cost = {0, 2, 0};
        ArrayList<Integer> powerUps = new ArrayList<>();
        ArrayList<Integer> players = new ArrayList<>();
        powerUps.add(0);
        powerUps.add(1);
        powerUps.add(2);
        players.add(1233);
        GameHandler gameHandler = new GameHandler(8, players,"map1",null);
        gameHandler.getGame().setCurrentPlayer(gameHandler.getGame().getPlayers().get(0));
        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().addPowerUp(gameHandler.getGame().getDeckCollector().getCardPowerUpDrawer().draw());

        for (int i = 0; i < 3; i++){

            if(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getColor().getAbbreviation().equals("YELLOW")) {
                cost[1]++;
            }
            if(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(i).getColor().getAbbreviation().equals("BLUE")) {
                cost[2]++;
            }
        }

        gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().addAmmo(3,3,3);
        gameHandler.getPaymentController().payment(cost, powerUps);

        assertEquals(3, gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0]);
        assertEquals(1, gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1]);
        assertEquals(3, gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2]);

        gameHandler.getGame().getPlayers().get(0).getPlayerBoard().getHandPlayer().addAmmo(3,3,3);
        gameHandler.getPaymentController().payment(cost);
        assertEquals((3 - cost[0]), gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0]);
        assertEquals((3 - cost[1]), gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1]);
        assertEquals((3 - cost[2]), gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2]);
    }


}
