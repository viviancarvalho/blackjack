package com.demo.player.patterns.observer;

/**
 * Implementação simples de GameEventListener que apenas loga no console.
 */
public class GameEngineEventHandler implements GameEventListener {

    @Override
    public void onPlayerBust(Long playerId) {
        System.out.println("Jogador " + playerId + " estourou! Casa vence.");
    }

    @Override
    public void onPlayerStand(Long playerId) {
        System.out.println("Jogador " + playerId + " parou. Vez da casa.");
    }

    @Override
    public void onBlackjack(Long playerId) {
        System.out.println("Jogador " + playerId + " fez BLACKJACK!");
    }
}
