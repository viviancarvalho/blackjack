package com.demo.regra.patterns.adapter;

import com.cardservice.model.Card;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Adapter para conversar com o card-microservice.
 * Usa RestTemplate em vez de RestClient para evitar problema de versão.
 */
@Component
public class CardServiceAdapter {

    private static final String CARD_SERVICE_URL = "http://localhost:8081/cards/draw";

    private final RestTemplate restTemplate;

    public CardServiceAdapter() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Pede uma carta ao microserviço de cartas.
     * Assume que o JSON retornado bate com o modelo com.cardservice.model.Card
     * (ex: { "value": "A♣" }).
     */
    public Card drawCard() {
        return restTemplate.getForObject(CARD_SERVICE_URL, Card.class);
    }
}
