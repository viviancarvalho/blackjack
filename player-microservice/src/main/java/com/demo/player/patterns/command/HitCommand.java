package com.demo.player.patterns.command;

/**
 * Comando para solicitar uma carta (HIT).
 */
public class HitCommand implements GameCommand {

    private final GameEngine engine;
    private final Long playerId;

    public HitCommand(GameEngine engine, Long playerId) {
        this.engine = engine;
        this.playerId = playerId;
    }

    @Override
    public void execute() {
        engine.processHit(playerId);
    }
}
