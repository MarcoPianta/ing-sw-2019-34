package Model;

/**
 * This class is used to collect all the decks used in a game.
 * It creates decks and the relative drawers.
 * */
public class DeckCollector {
    private Deck<CardWeapon> cardWeaponDeck;
    private Deck<CardPowerUp> cardPowerUpDeck;
    private Deck<CardAmmo> cardAmmoDeck;

    private Drawer<CardWeapon> cardWeaponDrawer;
    private Drawer<CardPowerUp> cardPowerUpDrawer;
    private Drawer<CardAmmo> cardAmmoDrawer;

    public DeckCollector(){
        createDecks();
    }

    /**
     * This method creates decks and relative drawers.
     * FileNotFoundException is not caught because
     * */
    private void createDecks() {
        DeckCreator deckCreator = new DeckCreator();
        cardWeaponDeck = deckCreator.createDeck("WEAPON");
        cardWeaponDrawer = cardWeaponDeck.createDrawer();

        cardPowerUpDeck = deckCreator.createDeck("POWERUP");
        cardPowerUpDrawer = cardPowerUpDeck.createDrawer();

        cardAmmoDeck = deckCreator.createDeck("AMMO");
        cardAmmoDrawer = cardAmmoDeck.createDrawer();
    }

    /**
     * The following methods return the drawer of a specific deck, drawer can then be used to draw cards from the deck.
     * */

    public Drawer<CardWeapon> getCardWeaponDrawer() {
        return cardWeaponDrawer;
    }

    public Drawer<CardPowerUp> getCardPowerUpDrawer() {
        return cardPowerUpDrawer;
    }

    public Drawer<CardAmmo> getCardAmmoDrawer() {
        return cardAmmoDrawer;
    }

    /**
     * The following methods return a specific deck
     * */

    public Deck<CardWeapon> getCardWeaponDeck() {
        return cardWeaponDeck.copy();
    }

    public Deck<CardPowerUp> getCardPowerUpDeck() {
        return cardPowerUpDeck.copy();
    }

    public Deck<CardAmmo> getCardAmmoDeck() {
        return cardAmmoDeck.copy();
    }
}
