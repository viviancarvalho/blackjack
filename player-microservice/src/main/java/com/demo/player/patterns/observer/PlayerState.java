package com.demo.player.patterns.observer;
import com.cardservice.model.Card;
import java.util.ArrayList;
import java.util.List;

public class PlayerState {

    private final Long playerId;
    private final List<Card> cartas = new ArrayList<>();
    private final List<GameEventListener> listeners = new ArrayList<>();

    public PlayerState(Long playerId) {
        this.playerId = playerId;
    }

    public void addListener(GameEventListener listener) {
        listeners.add(listener);
    }

    public void addCard(Card carta) {
        cartas.add(carta);
        int score = calcularPontos(cartas);

        if (score > 21) {
            // jogador estourou
            listeners.forEach(l -> l.onPlayerBust(playerId));
        } else if (score == 21) {
            // blackjack
            listeners.forEach(l -> l.onBlackjack(playerId));
        }
    }

    public void stand() {
        listeners.forEach(l -> l.onPlayerStand(playerId));
    }

    public List<Card> getCartas() {
        return new ArrayList<>(cartas);
    }

    public int getScore() {
        return calcularPontos(cartas);
    }

    /**
     * Cálculo de pontuação do Blackjack:
     * - 2–10 = valor numérico
     * - J, Q, K = 10
     * - A = 11 (ajustado para 1 se estourar)
     */
    private int calcularPontos(List<Card> cartas) {
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
                        v = 0; // fallback
                    }
                }
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

        // Se terminar com símbolo de naipe, remove
        char last = valor.charAt(valor.length() - 1);
        if (last == '♣' || last == '♦' || last == '♥' || last == '♠') {
            return valor.substring(0, valor.length() - 1);
        }

        return valor;
    }
}
