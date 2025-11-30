package com.cardservice.model;

/**
 * Representa uma carta de figura (J, Q, K, A) no baralho.
 */
public class FaceCard extends Card {

    public FaceCard(String naipe, String rank) {
        this.naipe = naipe;
        this.rank = rank;

        switch (rank) {
            case "J", "Q", "K" -> this.valor = 10;
            case "A" -> this.valor = 11;
            default -> this.valor = 0;
        }
    }
}
