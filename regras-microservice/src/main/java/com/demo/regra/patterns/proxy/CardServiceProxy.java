package com.demo.regra.patterns.proxy; // mantenha esse package se for o mesmo do seu arquivo

import com.cardservice.model.Card;
import com.demo.regra.patterns.adapter.CardServiceAdapter;
import org.springframework.stereotype.Component;

/**
 * Proxy para o CardServiceAdapter.
 * Aqui daria pra colocar cache, limite de requisições, logs, etc.
 */
@Component
public class CardServiceProxy {

    private final CardServiceAdapter adapter;
    private int cardsDrawn = 0; // só pra ter um estado/prova de proxy

    public CardServiceProxy(CardServiceAdapter adapter) {
        this.adapter = adapter;
    }

    public Card drawCard() {
        Card card = adapter.drawCard();
        cardsDrawn++;
        return card;
    }

    public int getCardsDrawn() {
        return cardsDrawn;
    }
}
