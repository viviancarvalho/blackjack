package com.demo.player.patterns.strategy;
import com.cardservice.model.Card;
import java.util.List;

/**
 * Strategy de cálculo de pontuação do jogador no Blackjack.
 */
public class PlayerScoreStrategy implements ScoreStrategy {

    @Override
    public int calculateScore(List<Card> cartas) {
        int pontos = 0;
        int ases = 0;

        for (Card carta : cartas) {
            String valor = carta.getRank() + carta.getSuit(); // ex: "A♣", "10♦", "J♥"
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
                        v = 0; // fallback seguro
                    }
                }
            }

            pontos += v;
        }

        // Ajuste dos Áses: de 11 para 1 se estourar
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
