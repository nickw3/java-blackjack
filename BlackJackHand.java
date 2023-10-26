import java.util.ArrayList;

public class BlackJackHand {
    ArrayList<Card> hand = new ArrayList<Card>();

    ArrayList<Card> getHand(){
        return this.hand;
    }

    BlackJackHand dealCard(Deck deck, BlackJackHand hand){
        Card addedCard = deck.dealCard();
        BlackJackHand newHand = hand;
        newHand.hand.add(addedCard);
        return newHand;
    }

    Deck createDealerHand(Deck deck){
        Deck newDeck = deck;
        this.hand.add((Card)newDeck.deck.get(0));
        newDeck.deck.remove(0);
        this.hand.add((Card)newDeck.deck.get(0));
        newDeck.deck.remove(0);
        this.hand.get(1).setHidden(true);
        return newDeck;
    }

    Deck createPlayerHand(Deck deck){
        Deck newDeck = deck;
        this.hand.add((Card)newDeck.deck.get(0));
        newDeck.deck.remove(0);
        this.hand.add((Card)newDeck.deck.get(0));
        newDeck.deck.remove(0);
        return newDeck;
    }

    int getHandValue(){
        int total = 0;
        for(Card c : hand){
            total += c.value;
        }
        return total;
    }
}
