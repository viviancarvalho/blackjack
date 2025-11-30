package com.demo.regra.patterns.adapter;

import com.cardservice.model.Card;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Adapter para conversar com o serviço da "casa" (dealer).
 * Usa RestTemplate ao invés de RestClient para evitar problemas de versão.
 */
@Component
public class HouseServiceAdapter {

    private static final String HOUSE_SERVICE_BASE_URL = "http://localhost:8082/house-service";

    private final RestTemplate restTemplate;

    public HouseServiceAdapter() {
        this.restTemplate = new RestTemplate();
    }

    public void addCard(Card card) {
        restTemplate.postForObject(
                HOUSE_SERVICE_BASE_URL + "/add-card",
                card,
                Void.class
        );
    }

    public int getScore() {
        Integer score = restTemplate.getForObject(
                HOUSE_SERVICE_BASE_URL + "/score",
                Integer.class
        );
        return score != null ? score : 0;
    }

    public void clearHand() {
        restTemplate.postForObject(
                HOUSE_SERVICE_BASE_URL + "/clear",
                null,
                Void.class
        );
    }

    public void playAuto() {
        restTemplate.postForObject(
                HOUSE_SERVICE_BASE_URL + "/play-auto",
                null,
                Void.class
        );
    }
}
