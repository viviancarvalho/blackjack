package com.demo.player.console;
import com.cardservice.model.Card;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console client que joga Blackjack chamando os microserviços via HTTP.
 *
 * Pré-requisitos:
 *  - card-microservice rodando em http://localhost:8081
 *  - regras-microservice rodando em http://localhost:8080/rules
 *
 * Para executar no IntelliJ:
 *  - Abra o módulo player-microservice.
 *  - Clique com o botão direito em ConsoleBlackjackHttp.main() e rode.
 */
public class ConsoleBlackjackHttp {

    private static final String CARD_BASE_URL = "http://localhost:8081/cards";
    private static final String RULES_BASE_URL = "http://localhost:8080/rules";

    private final RestTemplate restTemplate = new RestTemplate();

    private final List<Card> playerHand = new ArrayList<>();
    private final List<Card> dealerHand = new ArrayList<>();

    public static void main(String[] args) {
        new ConsoleBlackjackHttp().run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=======================================");
        System.out.println("        BLACKJACK – CONSOLE CLIENT     ");
        System.out.println("   (usando card-service + regras via HTTP)");
        System.out.println("=======================================\n");

        // Resetar baralho no card-service
        resetDeck();

        // Distribuir cartas iniciais
        playerHand.add(drawCard());
        playerHand.add(drawCard());
        dealerHand.add(drawCard());
        dealerHand.add(drawCard());

        boolean playerTurn = true;

        while (playerTurn) {
            int playerScore = calculateScore(playerHand);

            System.out.println("\nSuas cartas:");
            printHand(playerHand);
            System.out.println("Seu total: " + playerScore);

            System.out.println("\nCarta visível da casa:");
            System.out.println(" - " + formatCard(dealerHand.get(0)));

            if (playerScore > 21) {
                System.out.println("\nVocê estourou! 😬");
                playerTurn = false;
                break;
            }

            System.out.print("\nDigite [h] para HIT (comprar) ou [s] para STAND (parar): ");
            String acao = scanner.nextLine().trim().toLowerCase();

            if ("h".equals(acao)) {
                Card novaCarta = drawCard();
                System.out.println("Você comprou: " + formatCard(novaCarta));
                playerHand.add(novaCarta);
            } else if ("s".equals(acao)) {
                playerTurn = false;
            } else {
                System.out.println("Opção inválida, tente de novo.");
            }
        }

        // Turno da casa
        int playerScore = calculateScore(playerHand);
        if (playerScore <= 21) {
            System.out.println("\n=== Turno da casa ===");
            playDealer();
        }

        // Resultado final via regras-microservice
        exibirResultadoFinal();
    }

    // ------------------------- HTTP HELPERS -----------------------------

    private Card drawCard() {
        ResponseEntity<Card> response = restTemplate.getForEntity(
                CARD_BASE_URL + "/draw",
                Card.class
        );
        Card card = response.getBody();
        if (card == null) {
            throw new IllegalStateException("Não foi possível comprar carta do card-service");
        }
        return card;
    }

    private void resetDeck() {
        restTemplate.postForEntity(CARD_BASE_URL + "/reset", null, Void.class);
    }

    private String callWinnerEndpoint(int[] playerValues, int[] dealerValues) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

        for (int v : playerValues) {
            form.add("player", String.valueOf(v));
        }
        for (int v : dealerValues) {
            form.add("dealer", String.valueOf(v));
        }

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(form, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                RULES_BASE_URL + "/winner",
                HttpMethod.POST,
                request,
                String.class
        );

        return response.getBody();
    }

    // ------------------------- LÓGICA LOCAL -----------------------------

    private void playDealer() {
        while (true) {
            int dealerScore = calculateScore(dealerHand);
            System.out.println("Mão da casa:");
            printHand(dealerHand);
            System.out.println("Total da casa: " + dealerScore);

            if (dealerScore < 17) {
                System.out.println("Casa compra carta (regra: < 17).");
                Card c = drawCard();
                dealerHand.add(c);
                System.out.println("Casa comprou: " + formatCard(c));
            } else {
                System.out.println("Casa para (>= 17).");
                break;
            }
        }
    }

    private void exibirResultadoFinal() {
        int[] playerValues = playerHand.stream().mapToInt(Card::getValor).toArray();
        int[] dealerValues = dealerHand.stream().mapToInt(Card::getValor).toArray();

        System.out.println("\n================ RESULTADO FINAL ================");
        System.out.println("Suas cartas:");
        printHand(playerHand);
        System.out.println("Total jogador: " + calculateScore(playerHand));

        System.out.println("\nCartas da casa:");
        printHand(dealerHand);
        System.out.println("Total casa: " + calculateScore(dealerHand));

        try {
            String resultado = callWinnerEndpoint(playerValues, dealerValues);
            System.out.println("\n>>> Resultado segundo regras-microservice: " + resultado);
        } catch (Exception e) {
            System.out.println("\n[AVISO] Não foi possível chamar /rules/winner. " +
                    "Mostrando resultado local apenas.");
            System.out.println("Mensagem de erro: " + e.getMessage());
        }

        System.out.println("=================================================\n");
    }

    private int calculateScore(List<Card> hand) {
        int total = 0;
        int aces = 0;

        for (Card c : hand) {
            int v = c.getValor();
            if (v == 11) {
                aces++;
            }
            total += v;
        }

        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }

    private void printHand(List<Card> hand) {
        for (Card c : hand) {
            System.out.println(" - " + formatCard(c));
        }
    }

    private String formatCard(Card c) {
        // usar os getters reais do Card do card-microservice
        return c.getRank() + " (valor " + c.getValor() + ")";
    }
}

