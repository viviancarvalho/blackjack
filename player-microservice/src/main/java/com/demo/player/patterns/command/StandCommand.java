package com.demo.player.patterns.command;

/**
 * Comando para o jogador parar (STAND).
 */
public class StandCommand implements GameCommand {

    private final GameEngine engine;
    private final Long playerId;

    public StandCommand(GameEngine engine, Long playerId) {
        this.engine = engine;
        this.playerId = playerId;
    }

    @Override
    public void execute() {
        engine.processStand(playerId);
    }
}
