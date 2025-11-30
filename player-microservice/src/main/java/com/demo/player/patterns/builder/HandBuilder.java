package com.demo.player.patterns.builder;
import com.cardservice.model.Card;
import java.util.ArrayList;
import java.util.List;

public class HandBuilder {

    private final List<Card> cartas = new ArrayList<>();

    public HandBuilder addCard(Card carta) {
        cartas.add(carta);
        return this;
    }

    public Hand build() {
        int pontos = calcularPontos(cartas);
        return new Hand(new ArrayList<>(cartas), pontos);
    }

    /**
     * Calcula a pontuação do blackjack:
     * - 2–10 = valor numérico
     * - J, Q, K = 10
     * - A = 11 (e depois ajusta pra 1 se estourar)
     */
    private int calcularPontos(List<Card> cartas) {
        int pontos = 0;
        int ases = 0;

        for (Card carta : cartas) {
            String valor = carta.getRank() + carta.getSuit(); // ex: "A♣", "10♦", "J♥"

            // Remove o naipe, se for o último char
            String rank = extrairRank(valor);

            int v;
            switch (rank) {
                case "J", "Q", "K" -> v = 10;
                case "A" -> {
                    v = 11;
                    ases++;
                }
                default -> {
                    try {
                        v = Integer.parseInt(rank);
                    } catch (NumberFormatException e) {
                        v = 0; // fallback safe
                    }
                }
            }

            pontos += v;
        }

        // Ajusta Ás de 11 para 1 se estourar
        while (pontos > 21 && ases > 0) {
            pontos -= 10;
            ases--;
        }

        return pontos;
    }

    private String extrairRank(String valor) {
        if (valor == null || valor.isEmpty()) {
            return "";
        }

        // se terminar com símbolo de naipe, remove
        char last = valor.charAt(valor.length() - 1);
        if (last == '♣' || last == '♦' || last == '♥' || last == '♠') {
            return valor.substring(0, valor.length() - 1);
        }

        return valor;
    }
}
