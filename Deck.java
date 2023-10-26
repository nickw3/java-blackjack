import java.util.ArrayList;

public class Deck<E> {
    ArrayList<E> deck = new ArrayList<E>();

    Card dealCard(){
        Card removed = (Card)this.deck.get(0);
        this.deck.remove(0);
        return removed;
    }

    Deck(){
        int count = 2;
        for(int i = 1; i <= 52; i++){
            if(i == 1 || i == 14 || i == 27 || i == 40){
                deck.add((E)new Card(i, "image/card/" + i + ".png", 11, false));
            }
            if((i >= 10 && i <= 13) || (i >= 23 && i <= 26)|| (i >= 36 && i <= 39)|| (i >= 49 && i <= 52)){
                deck.add((E)new Card(i, "image/card/" + i + ".png", 10, false));
                count = 2;
            }
            else {
                if(i != 1 && i != 14 && i != 27 && i != 40) {
                    deck.add((E)new Card(i, "image/card/" + i + ".png", count, false));
                    count++;
                }
            }
        }
    }
}
