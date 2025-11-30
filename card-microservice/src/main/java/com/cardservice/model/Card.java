package com.cardservice.model;

/**
 * Representa uma carta do baralho para o jogo de Blackjack.
 * Usada em conjunto com o padrão Factory (NumberCard, FaceCard).
 */
public abstract class Card {

    protected String naipe; // ♥ ♦ ♣ ♠
    protected String rank;  // 2–10, J, Q, K, A
    protected int valor;    // valor numérico para o Blackjack

    public String getNaipe() {
        return naipe;
    }

    public String getRank() {
        return rank;
    }

    public int getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return rank + " (" + valor + ")";
    }
}
