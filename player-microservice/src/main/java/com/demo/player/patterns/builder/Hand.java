package com.demo.player.patterns.builder;
import com.cardservice.model.Card;
import java.util.List;

public class Hand {

    private final List<Card> cartas;
    private final int pontos;

    public Hand(List<Card> cartas, int pontos) {
        this.cartas = cartas;
        this.pontos = pontos;
    }

    public List<Card> getCartas() {
        return cartas;
    }

    public int getPontos() {
        return pontos;
    }
}
