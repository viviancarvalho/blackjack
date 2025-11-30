package com.demo.regra.patterns.facade; // ⚠️ mantenha o package que já estava no seu arquivo

import com.cardservice.model.Card;
import com.demo.regra.patterns.adapter.CardServiceAdapter;
import com.demo.regra.patterns.adapter.HouseServiceAdapter;
import com.demo.regra.patterns.adapter.PlayerServiceAdapter;
import org.springframework.stereotype.Service;

/**
 * Facade que orquestra a engine do jogo de Blackjack.
 * Usa os adapters de Player, Casa (dealer) e Cartas.
 */
@Service
public class GameEngineFacade {

    private final PlayerServiceAdapter playerServiceAdapter;
    private final CardServiceAdapter cardServiceAdapter;
    private final HouseServiceAdapter houseServiceAdapter;

    public GameEngineFacade(PlayerServiceAdapter playerServiceAdapter,
                            CardServiceAdapter cardServiceAdapter,
                            HouseServiceAdapter houseServiceAdapter) {
        this.playerServiceAdapter = playerServiceAdapter;
        this.cardServiceAdapter = cardServiceAdapter;
        this.houseServiceAdapter = houseServiceAdapter;
    }

    /**
     * Inicia um novo jogo para o jogador informado.
     * Cria o player, limpa a mão da casa e distribui cartas iniciais.
     */
    public Long startNewGame(String playerName) {
        Long playerId = playerServiceAdapter.createPlayer(playerName);
        houseServiceAdapter.clearHand();

        // Duas cartas para o jogador
        hit(playerId);
        hit(playerId);

        // Uma carta inicial para a casa (pode ajustar se quiser duas)
        Card houseCard = cardServiceAdapter.drawCard();
        houseServiceAdapter.addCard(houseCard);

        return playerId;
    }

    /**
     * Jogador pede carta (Hit).
     */
    public void hit(Long playerId) {
        Card card = cardServiceAdapter.drawCard();
        playerServiceAdapter.addCardToPlayer(playerId, card);
    }

    /**
     * Jogador para (Stand) e a casa joga até finalizar.
     */
    public void stand(Long playerId) {
        playerServiceAdapter.stand(playerId);
        houseServiceAdapter.playAuto();
    }

    public int getPlayerScore(Long playerId) {
        return playerServiceAdapter.getPlayerScore(playerId);
    }

    public int getHouseScore() {
        return houseServiceAdapter.getScore();
    }

    /**
     * Retorna o resultado do jogo em forma de String simples.
     */
    public String getResult(Long playerId) {
        int playerScore = getPlayerScore(playerId);
        int houseScore  = getHouseScore();

        if (playerScore > 21) {
            return "HOUSE_WINS";
        }
        if (houseScore > 21) {
            return "PLAYER_WINS";
        }
        if (playerScore > houseScore) {
            return "PLAYER_WINS";
        }
        if (playerScore < houseScore) {
            return "HOUSE_WINS";
        }
        return "PUSH"; // empate
    }
}
