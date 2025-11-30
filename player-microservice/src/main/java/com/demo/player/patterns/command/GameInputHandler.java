package com.demo.player.patterns.command;

/**
 * Invoker do padrão Command.
 */
public class GameInputHandler {

    public void execute(GameCommand command) {
        command.execute();
    }
}
