package Model;

import java.util.ArrayList;

/**
 * This class is a collection of card, cards are collected in a deterministic order, the Drawer class then will randomly
 * extract card from deck
 * */
public class Deck<T extends Card> {
    private ArrayList<T> deck,trashDeck;
    private boolean mix;


    public Deck(boolean mix) {
        deck = new ArrayList<>();
        this.mix = mix;
        if(mix)
            trashDeck = new ArrayList<>();
    }

     /**
      * This method is used to add a cart to the deck, it returns true if the operation end successfully
      * */
    public boolean add(T t){
        deck.add(t);
        return deck.get(deck.size()-1) == t; // This is equivalent to an if-else statement checking the returned value
    }

    /**
     * This method allow the caller to get a card from the deck using an index, when deck is empty if deck type allow
     * mixing a new deck is created to simulate mixing
     * */
    public T getCard(int index) {
        T returned = null;
            if(!deck.isEmpty()) {
                if (index >= deck.size())
                    throw new IndexOutOfBoundsException();
                else{
                    returned = remove(index);
                }
            }
            else{
                if (mix) {
                    deck = getTrashDeck(); //Refill deck
                    if (index >= deck.size())
                        throw new IndexOutOfBoundsException();
                    else {
                        returned = remove(index);
                    }
                }
            }
            return returned;
    }
    public int getSize(){
        return deck.size();
    }

    /**
     * This method returns a copy of the trash deck
     * */
    private ArrayList<T> getTrashDeck() {
        return new ArrayList<>(trashDeck);
    }

    /**
     * This method copy the current deck in trashDeck if, and only if, the deck type allow mixing and trash deck is empty.
     * This method should be used only when deck is created to create a copy of the original deck that can be used to
     * simulate mixing
     * */
    public void setTrashDeck() {
        if(mix && trashDeck.isEmpty())
            this.trashDeck = new ArrayList<>(deck);
    }

    /**
     * This method remove the card specified by the index and return a copy of it
     * */
    private T remove(int index){
        T copy = deck.get(index);
        deck.remove(index);
        return copy;
    }
}
