package com.cardservice.model;

/**
 * Representa uma carta numérica (2 a 10) no baralho.
 */
public class NumberCard extends Card {

    public NumberCard(String naipe, int valor) {
        this.naipe = naipe;
        this.rank = String.valueOf(valor);
        this.valor = valor;
    }
}
