package com.cardservice.service;

import com.cardservice.model.Card;
import com.cardservice.patterns.factory.CardFactory;
import aop.Auditable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Serviço responsável por gerenciar o baralho.
 */
@Service
public class DeckService {

    private final List<Card> deck = new ArrayList<>();

    public DeckService() {
        resetDeck();
    }

    /**
     * Reinicia o baralho com 52 cartas e embaralha.
     */
    @Auditable(action = "reset-deck", resource = "deck")
    public void resetDeck() {
        deck.clear();

        String[] naipes = {"♥", "♦", "♣", "♠"};
        String[] ranks = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};

        for (String n : naipes) {
            for (String r : ranks) {
                deck.add(CardFactory.createCard(n, r));
            }
        }

        Collections.shuffle(deck);
    }

    /**
     * Retira a próxima carta do topo do baralho.
     * Se o baralho estiver vazio, ele é recriado.
     */
    @Auditable(action = "draw-card", resource = "deck")
    public Card drawCard() {
        if (deck.isEmpty()) {
            resetDeck();
        }
        return deck.remove(0);
    }

    public int remainingCards() {
        return deck.size();
    }
}
