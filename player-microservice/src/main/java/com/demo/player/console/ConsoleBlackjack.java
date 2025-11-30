package com.demo.player.console;
import com.cardservice.model.Card;
import com.cardservice.service.DeckService;
import com.demo.player.patterns.builder.Hand;
import com.demo.player.patterns.builder.HandBuilder;
import com.demo.player.patterns.command.GameEngine;
import com.demo.player.patterns.command.GameInputHandler;
import com.demo.player.patterns.command.HitCommand;
import com.demo.player.patterns.command.StandCommand;
import com.demo.player.patterns.observer.GameEngineEventHandler;
import com.demo.player.patterns.observer.GameEventListener;
import com.demo.player.patterns.observer.PlayerState;
import com.demo.player.patterns.strategy.PlayerScoreStrategy;
import com.demo.player.patterns.strategy.ScoreStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Jogo de Blackjack no console, usando os padrões e serviços criados.
 * Não depende do servidor HTTP rodando, apenas das classes dos microserviços.
 */
public class ConsoleBlackjack {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        DeckService deckService = new DeckService();
        ScoreStrategy scoreStrategy = new PlayerScoreStrategy();

        System.out.println("=== BLACKJACK (Console) ===");
        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine();
        Long playerId = 1L;

        // OBSERVER
        PlayerState playerState = new PlayerState(playerId);
        GameEventListener listener = new GameEngineEventHandler();
        playerState.addListener(listener);

        // ENGINE usada pelo padrão COMMAND
        GameEngine engine = new GameEngine() {
            @Override
            public void processHit(Long id) {
                Card card = deckService.drawCard();
                System.out.println("Você comprou: " + card);
                playerState.addCard(card);
            }

            @Override
            public void processStand(Long id) {
                playerState.stand();
            }
        };

        GameInputHandler inputHandler = new GameInputHandler();

        // Mão do jogador
        HandBuilder handBuilder = new HandBuilder();
        List<Card> dealerCards = new ArrayList<>();

        // Duas cartas iniciais para o jogador
        handBuilder.addCard(deckService.drawCard());
        handBuilder.addCard(deckService.drawCard());
        Hand playerHand = handBuilder.build();

        // Duas cartas para o dealer
        dealerCards.add(deckService.drawCard());
        dealerCards.add(deckService.drawCard());

        System.out.println("\nOlá, " + nome + "! Vamos começar.");
        mostrarMaoJogador(playerHand);
        mostrarMaoDealerParcial(dealerCards);

        // Atualiza estado inicial no observer
        for (Card c : playerHand.getCartas()) {
            playerState.addCard(c);
        }

        boolean jogadorParou = false;
        while (!jogadorParou && playerHand.getPontos() < 21) {
            System.out.print("[H]it (comprar carta) ou [S]tand (parar)? ");
            String acao = scanner.nextLine().trim().toUpperCase();

            if (acao.equals("H")) {
                inputHandler.execute(new HitCommand(engine, playerId));
                // reconstrói a mão a partir do estado
                HandBuilder novaBuilder = new HandBuilder();
                for (Card c : playerState.getCartas()) {
                    novaBuilder.addCard(c);
                }
                playerHand = novaBuilder.build();
                mostrarMaoJogador(playerHand);
            } else if (acao.equals("S")) {
                inputHandler.execute(new StandCommand(engine, playerId));
                jogadorParou = true;
            } else {
                System.out.println("Opção inválida.");
            }

            if (playerHand.getPontos() > 21) {
                System.out.println("\nVocê estourou! Dealer vence.");
                encerrar(scanner);
                return;
            }
        }

        // Jogo da casa
        int dealerScore = calcularScoreDealer(dealerCards, scoreStrategy);
        while (dealerScore < 17) {
            Card nova = deckService.drawCard();
            dealerCards.add(nova);
            dealerScore = calcularScoreDealer(dealerCards, scoreStrategy);
        }

        // Resultado final
        System.out.println("\n=== RESULTADO FINAL ===");
        mostrarMaoJogador(playerHand);
        mostrarMaoDealerCompleta(dealerCards, dealerScore);

        int playerScore = playerHand.getPontos();

        if (playerScore > 21) {
            System.out.println("Você estourou. Dealer vence.");
        } else if (dealerScore > 21) {
            System.out.println("Dealer estourou. " + nome + " vence!");
        } else if (playerScore > dealerScore) {
            System.out.println(nome + " vence!");
        } else if (dealerScore > playerScore) {
            System.out.println("Dealer vence.");
        } else {
            System.out.println("Empate.");
        }

        encerrar(scanner);
    }

    private static int calcularScoreDealer(List<Card> cartas, ScoreStrategy strategy) {
        return strategy.calculateScore(cartas);
    }

    private static void mostrarMaoJogador(Hand hand) {
        System.out.println("\nSuas cartas:");
        hand.getCartas().forEach(c -> System.out.println(" - " + c));
        System.out.println("Total: " + hand.getPontos());
    }

    private static void mostrarMaoDealerParcial(List<Card> cartas) {
        System.out.println("\nCarta visível do dealer: " + cartas.get(0));
        System.out.println("(Uma carta está virada para baixo)\n");
    }

    private static void mostrarMaoDealerCompleta(List<Card> cartas, int score) {
        System.out.println("Cartas do dealer:");
        cartas.forEach(c -> System.out.println(" - " + c));
        System.out.println("Total dealer: " + score);
    }

    private static void encerrar(Scanner scanner) {
        System.out.println("\nObrigado por jogar Blackjack!");
        scanner.close();
    }
}
