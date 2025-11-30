package com.demo.regra.patterns.singleton;

/**
 * Exemplo de Singleton representando uma engine de jogo.
 * Aqui apenas imprime mensagens no console.
 */
public class GameEngine {

    private static GameEngine instance;

    private GameEngine() {}

    public static synchronized GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }

    public void startRound() {
        System.out.println("Iniciando rodada...");
    }

    public void processHit(Long playerId) {
        System.out.println("Jogador " + playerId + " pediu HIT");
    }

    public void processStand(Long playerId) {
        System.out.println("Jogador " + playerId + " pediu STAND");
    }
}
