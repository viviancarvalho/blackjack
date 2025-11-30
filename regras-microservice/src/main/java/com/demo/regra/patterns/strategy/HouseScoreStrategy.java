package com.demo.regra.patterns.strategy;

import com.cardservice.model.Card;

import java.util.List;

/**
 * Strategy de pontuação da casa (dealer) no Blackjack.
 * Versão compatível com Java 8.
 */
public class HouseScoreStrategy implements ScoreStrategy {

    @Override
    public int calculateScore(List<Card> cartas) {
        int pontos = 0;
        int ases = 0;

        for (Card c : cartas) {
            String valor = c.value(); // ex: "A♣", "10♦", "J♥"
            String rank = extrairRank(valor);

            int v;
            switch (rank) {
                case "J":
                case "Q":
                case "K":
                    v = 10;
                    break;

                case "A":
                    v = 11;
                    ases++;
                    break;

                default:
                    try {
                        v = Integer.parseInt(rank);
                    } catch (NumberFormatException e) {
                        v = 0; // fallback seguro
                    }
                    break;
            }

            pontos += v;
        }

        // Ajusta Ás de 11 -> 1 se estourar
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

        char last = valor.charAt(valor.length() - 1);
        if (last == '♣' || last == '♦' || last == '♥' || last == '♠') {
            return valor.substring(0, valor.length() - 1);
        }

        return valor;
    }
}
