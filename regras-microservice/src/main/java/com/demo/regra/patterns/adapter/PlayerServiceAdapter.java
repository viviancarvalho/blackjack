package com.demo.regra.patterns.adapter; // ⚠️ mantenha o package que já estava no seu arquivo

import com.cardservice.model.Card;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Adapter para conversar com o player-microservice.
 * Usa RestTemplate ao invés de RestClient para evitar problemas de versão.
 */
@Component
public class PlayerServiceAdapter {

    private static final String PLAYER_SERVICE_BASE_URL = "http://localhost:8080/player";

    private final RestTemplate restTemplate;

    public PlayerServiceAdapter() {
        this.restTemplate = new RestTemplate();
    }

    public Long createPlayer(String name) {
        return restTemplate.postForObject(
                PLAYER_SERVICE_BASE_URL + "/create",
                name,
                Long.class
        );
    }

    public void addCardToPlayer(Long playerId, Card card) {
        restTemplate.postForObject(
                PLAYER_SERVICE_BASE_URL + "/" + playerId + "/add-card",
                card,
                Void.class
        );
    }

    public int getPlayerScore(Long playerId) {
        Integer score = restTemplate.getForObject(
                PLAYER_SERVICE_BASE_URL + "/" + playerId + "/score",
                Integer.class
        );
        return score != null ? score : 0;
    }

    public void clearPlayerHand(Long playerId) {
        restTemplate.postForObject(
                PLAYER_SERVICE_BASE_URL + "/" + playerId + "/clear",
                null,
                Void.class
        );
    }

    public void stand(Long playerId) {
        restTemplate.postForObject(
                PLAYER_SERVICE_BASE_URL + "/" + playerId + "/stand",
                null,
                Void.class
        );
    }
}
