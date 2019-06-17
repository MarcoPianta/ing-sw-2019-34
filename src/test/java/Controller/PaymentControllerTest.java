package Controller;

import Model.CardPowerUp;
import Model.PowerUpEnum;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentControllerTest {
    @Test
    public void paymentTest() throws FileNotFoundException {
        ArrayList<Integer> players=new ArrayList<>();
        ArrayList<Integer> cost=new ArrayList<>();
        ArrayList<Integer> powerUps=new ArrayList<>();
        powerUps.add(0);
        powerUps.add(1);
        cost.add(2);
        cost.add(2);
        cost.add(2);
        players.add(1233);
        GameHandler gameHandler=new GameHandler(8,players,"map1");
        gameHandler.getGame().setCurrentPlayer(gameHandler.getGame().getPlayers().get(0));
        CardPowerUp cardPowerUp = new CardPowerUp(PowerUpEnum.NEWTON_B.getAbbreviation());
        CardPowerUp cardPowerUp2 = new CardPowerUp(PowerUpEnum.NEWTON_B.getAbbreviation());
        gameHandler.getGame().getPlayers().get(0).getPlayerBoard().getHandPlayer().addPowerUp(cardPowerUp);
        gameHandler.getGame().getPlayers().get(0).getPlayerBoard().getHandPlayer().addPowerUp(cardPowerUp2);
        gameHandler.getGame().getPlayers().get(0).getPlayerBoard().getHandPlayer().addAmmo(3,3,3);

        gameHandler.getPaymentController().payment(cost,powerUps);
        System.out.println(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(1).getColor().getAbbreviation());
        System.out.println(gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getPlayerPowerUps().get(0).getColor().getAbbreviation());
        assertEquals(1,gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0]);
        assertEquals(1,gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1]);
        assertEquals(3,gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2]);

        gameHandler.getGame().getPlayers().get(0).getPlayerBoard().getHandPlayer().addAmmo(1,1,0);
        gameHandler.getPaymentController().payment(cost);
        assertEquals(0,gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[0]);
        assertEquals(0,gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[1]);
        assertEquals(1,gameHandler.getGame().getCurrentPlayer().getPlayerBoard().getHandPlayer().getAmmoRYB()[2]);
    }


}
