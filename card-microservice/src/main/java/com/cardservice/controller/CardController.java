package com.cardservice.controller;

import com.cardservice.model.Card;
import com.cardservice.service.DeckService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST do microserviço de cartas.
 */
@RestController
@RequestMapping("/cards")
public class CardController {

    private final DeckService deckService;

    public CardController(DeckService deckService) {
        this.deckService = deckService;
    }

    /**
     * Retorna uma carta do baralho.
     */
    @GetMapping("/draw")
    public Card draw() {
        return deckService.drawCard();
    }

    /**
     * Reinicia o baralho.
     */
    @PostMapping("/reset")
    public String reset() {
        deckService.resetDeck();
        return "Deck resetado com sucesso!";
    }

    /**
     * Quantidade de cartas restantes.
     */
    @GetMapping("/remaining")
    public int remaining() {
        return deckService.remainingCards();
    }
}
