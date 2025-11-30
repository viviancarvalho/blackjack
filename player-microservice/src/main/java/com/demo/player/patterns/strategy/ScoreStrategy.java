package com.demo.player.patterns.strategy;
import com.cardservice.model.Card;
import java.util.List;

/**
 * Estratégia de cálculo de pontuação.
 */
public interface ScoreStrategy {
    int calculateScore(List<Card> cartas);
}
