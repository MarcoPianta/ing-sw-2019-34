package Model;

import java.util.ArrayList;

/**
 * This class is a collection of card, cards are collected in a deterministic order, the Drawer class then will randomly
 * extract card from cardDeck
 * */
public class Deck<T extends Card> {
    private ArrayList<T> cardDeck;
    private ArrayList<T> trashDeck;
    private boolean mix;


    public Deck(boolean mix) {
        cardDeck = new ArrayList<>();
        this.mix = mix;
        if(mix)
            trashDeck = new ArrayList<>();
    }

     /**
      * This method is used to add a cart to the cardDeck, it returns true if the operation end successfully
      * */
    public void add(T t){
        cardDeck.add(t);
    }

    /**
     * This method allow the caller to get a card from the cardDeck using an index, when cardDeck is empty if cardDeck type allow
     * mixing a new cardDeck is created to simulate mixing
     * */
    public T getCard(int index) {
        T returned = null;
            if(!cardDeck.isEmpty()) {
                if (index >= cardDeck.size())
                    throw new IndexOutOfBoundsException();
                else{
                    returned = remove(index);
                }
            }
            else{
                if (mix) {
                    cardDeck = getTrashDeck(); //Refill cardDeck
                    if (index >= cardDeck.size())
                        throw new IndexOutOfBoundsException();
                    else {
                        returned = remove(index);
                    }
                }
            }
            return returned;
    }

    /**
     * Returns the cardDeck size
     * */
    public int getSize(){
        return cardDeck.size();
    }

    /**
     * This method returns a copy of the trash cardDeck
     * */
    private ArrayList<T> getTrashDeck() {
        return new ArrayList<>(trashDeck);
    }

    /**
     * This method copy the current cardDeck in trashDeck if, and only if, the cardDeck type allow mixing and trash cardDeck is empty.
     * This method should be used only when cardDeck is created to create a copy of the original cardDeck that can be used to
     * simulate mixing
     * */
    public void setTrashDeck() {
        if(mix && trashDeck.isEmpty())
            this.trashDeck = new ArrayList<>(cardDeck);
    }

    /**
     * This method returns the iterator used to extract cards from cardDeck
     * */
    public Drawer<T> createDrawer(){
        return new Drawer<>(this);
    }

    /**
     * This method remove the card specified by the index and return a copy of it
     * */
    private T remove(int index){
        T copy = cardDeck.get(index);
        cardDeck.remove(index);
        return copy;
    }

    public Deck<T> copy(){
        Deck<T> copy = new Deck<>(mix);
        copy.cardDeck = new ArrayList<>(this.cardDeck);
        copy.mix = this.mix;
        if (mix)
            copy.trashDeck = new ArrayList<>(this.trashDeck);
        return copy;
    }
}
