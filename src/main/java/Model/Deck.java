package Model;

import java.util.ArrayList;

public class Deck<T> {
    private ArrayList<T> deck;
    private boolean mix;

    public Deck(boolean mix){
        deck = new ArrayList<>();
        this.mix = mix;
    }

     /**This method is used to add a cart to the deck, it returns true if the operation end successfully*/
    public boolean add(T t){
        deck.add(t);
        if (deck.get(deck.size()-1) == t)
            return true;
        else
            return false;
    }

    /**This method allow the caller to get a card from the deck using an index*/
    public T getCard(int index){
        if (index >= deck.size())
            throw new IndexOutOfBoundsException();
        else
            if (mix)
                remove();
            return deck.get(index);
    }
    public int getSize(){
        return deck.size();
    }

    private void remove(){}


}
