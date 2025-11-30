package com.cardservice.service;
import com.cardservice.model.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementação simples de baralho usada pelo player-microservice.
 * Ela existe apenas para satisfazer os imports:
 *   import com.cardservice.service.DeckService;
 */
public class DeckService {

    private final List<Card> deck = new ArrayList<>();

    public DeckService() {
        resetDeck();
    }

    /**
     * Reseta o baralho para 52 cartas e embaralha.
     */
    public void resetDeck() {
        deck.clear();

        String[] naipes = {"♣", "♦", "♥", "♠"};
        String[] ranks  = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};

        for (String n : naipes) {
            for (String r : ranks) {
                // Usa o value como algo tipo "A♠", "10♥" etc.
                deck.add(new Card(r + n));
            }
        }

        Collections.shuffle(deck);
    }

    /**
     * Compra uma carta do topo.
     */
    public Card draw() {
        if (deck.isEmpty()) {
            resetDeck();
        }
        return deck.remove(0);
    }

    /**
     * Alias para draw(), caso o seu código use drawCard().
     */
    public Card drawCard() {
        return draw();
    }

    /**
     * Quantas cartas ainda restam no baralho.
     */
    public int remaining() {
        return deck.size();
    }

    /**
     * Alias para remaining(), caso o código use remainingCards().
     */
    public int remainingCards() {
        return remaining();
    }
}
