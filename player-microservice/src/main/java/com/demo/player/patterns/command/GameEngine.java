package com.demo.player.patterns.command;

/**
 * Interface simples representando operações da engine de jogo
 * usadas pelos comandos de HIT e STAND.
 */
public interface GameEngine {
    void processHit(Long playerId);
    void processStand(Long playerId);
}
