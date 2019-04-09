package Model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class CardPowerUpTest {

    @Test
    public void testConstructor() throws FileNotFoundException {
        CardPowerUp cardPowerUp = new CardPowerUp(PowerUpEnum.NEWTON_B.getAbbreviation());

        assertEquals(cardPowerUp.getName()+"_B", PowerUpEnum.NEWTON_B.getAbbreviation());
        assertEquals(cardPowerUp.getColor(), AmmoColors.BLUE);
        assertEquals(0, cardPowerUp.getCost());
        assertEquals(0, cardPowerUp.getDamage());
        assertEquals(0, cardPowerUp.getMark());
        assertEquals(0, cardPowerUp.getMyMove());
        assertEquals(2, cardPowerUp.getOtherMove());
        assertEquals(1, cardPowerUp.getTarget());
        assertEquals("during", cardPowerUp.getWhen());
        assertFalse(cardPowerUp.getVision());
    }
}