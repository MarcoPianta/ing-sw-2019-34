package Model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {


    /**
     * This method test a deck that cannot be mixed
     * */
    @Test
    public void notMixedTest() throws FileNotFoundException {
        Deck<CardWeapon> deck;
        deck = new Deck(false);

        assertEquals(0, deck.getSize()); //When created the deck must be empty
        assertNull(deck.getCard(0)); //Deck must not contains any card
        deck.add(new CardWeapon(WeaponDictionary.CYBERBLADE.getAbbreviation()));
        assertEquals(1, deck.getSize()); //After a card is added the deck size must be 1
        assertThrows(IndexOutOfBoundsException.class , () -> {
            deck.getCard(2);
        });//When parameter of getCard is higher than deck size getCard must throw an IndexOutOfBoundsException
    }

    /**
     * This method test a deck that can be mixed
     * */
    @Test
    public void mixedTest() {
        Deck<CardAmmo> deck;
        deck = new Deck(true);

        //The following TO-DO statement should be uncommented when all json file for ammo will be created
        //TODO deck.add(new CardOnlyAmmo(AmmoEnum.AMMO1.getAbbreviation()));

        //The following statement MUST be removed when TO-DO will be uncommented
        deck.add(new CardOnlyAmmo());

        deck.setTrashDeck();
        assertEquals(1, deck.getSize()); //After a card is added the deck size must be 1
        assertNotNull(deck.getCard(0));
        assertNotNull(deck.getCard(0)); //Also if card is removed the deck allow mixing so when deck is empty it must be recreated
        assertThrows(IndexOutOfBoundsException.class , () -> {
            deck.getCard(2);
        });//When parameter of getCard is higher than deck size getCard must throw an IndexOutOfBoundsException even when the deck is refilled
    }
}
