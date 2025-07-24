package Blackjack;

import java.util.*;

public class Shoe {
    private List<String> cards;
    private int deckPenetration;

    public Shoe(List<String> deck, int numOfDecks) {
        cards = new ArrayList<>();
        for (int i = 0; i < numOfDecks; i++) {
            cards.addAll(deck);
        }
        Collections.shuffle(cards);

        Random r1 = new Random();
        deckPenetration = (r1.nextInt(11) + 34) * numOfDecks;
    }

    public String dealCard() {
        deckPenetration--;
        return cards.remove(0);
    }

    public int getPenetration() {
        return deckPenetration;
    }

    public void reset(List<String> deck, int numOfDecks) {
        cards.clear();
        for (int i = 0; i < numOfDecks; i++) {
            cards.addAll(deck);
        }
        Collections.shuffle(cards);

        Random r1 = new Random();
        deckPenetration = (r1.nextInt(11) + 34) * numOfDecks;
    }

    public int size() {
        return cards.size();
    }
}
