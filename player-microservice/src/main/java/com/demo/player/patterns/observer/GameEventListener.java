package com.demo.player.patterns.observer;

/**
 * Observer para eventos do jogador.
 */
public interface GameEventListener {
    void onPlayerBust(Long playerId);
    void onPlayerStand(Long playerId);
    void onBlackjack(Long playerId);
}
