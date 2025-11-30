package com.demo.regra.patterns.strategy;

import java.util.List;

import com.cardservice.model.Card;

/**
 * Interface de estratégia de pontuação.
 */
public interface ScoreStrategy {
    int calculateScore(List<Card> cartas);
}
