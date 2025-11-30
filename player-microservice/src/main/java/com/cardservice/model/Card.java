package com.cardservice.model;

public class Card {

    private String rank;   // valor da carta: 2..10, J, Q, K, A
    private String suit;   // naipe: ♠, ♥, ♦, ♣ ou texto ("HEARTS", etc.)

    public Card() {
    }

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }
    public Card(String code) {
        if (code == null || code.isEmpty()) {
            this.rank = "";
            this.suit = "";
            return;
        }
        this.suit = code.substring(code.length() - 1);
        this.rank = code.substring(0, code.length() - 1);
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    // usado pelo score: getValor()
    public int getValor() {
        if (rank == null) {
            return 0;
        }

        switch (rank) {
            case "J":
            case "Q":
            case "K":
                return 10;
            case "A":
                // por padrão 11; quem calcula o score ajusta o Ás depois
                return 11;
            default:
                try {
                    return Integer.parseInt(rank); // 2..10
                } catch (NumberFormatException e) {
                    return 0;
                }
        }
    }

    @Override
    public String toString() {
        return rank + " de " + suit;
    }
}
