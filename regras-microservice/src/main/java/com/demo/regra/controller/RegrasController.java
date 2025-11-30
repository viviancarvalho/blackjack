package com.demo.regra.controller;

import aop.Auditable;
import org.springframework.web.bind.annotation.*;

/**
 * Microserviço simples com regras de pontuação para Blackjack.
 * Usa um cálculo básico para mostrar o conceito de regras isoladas.
 */
@RestController
@RequestMapping("/rules")
public class RegrasController {

    // --- Cálculo simples de pontuação considerando Ás como 11 ou 1 ---
    private int calcularPontuacao(int[] cartas) {
        int total = 0;
        int ases = 0;

        for (int valor : cartas) {
            if (valor == 11) { // Ás inicialmente como 11
                ases++;
            }
            total += valor;
        }

        // Ajusta Ás de 11 -> 1 se estourar
        while (total > 21 && ases > 0) {
            total -= 10;
            ases--;
        }

        return total;
    }

    @Auditable(action = "calc-score", resource = "rules")
    @PostMapping("/score")
    public int score(@RequestParam int[] cards) {
        return calcularPontuacao(cards);
    }

    @Auditable(action = "winner", resource = "rules")
    @PostMapping("/winner")
    public String winner(@RequestParam int[] player, @RequestParam int[] dealer) {

        int p = calcularPontuacao(player);
        int d = calcularPontuacao(dealer);

        if (p > 21) return "Dealer venceu (Jogador estourou)";
        if (d > 21) return "Jogador venceu (Dealer estourou)";
        if (p > d) return "Jogador venceu";
        if (d > p) return "Dealer venceu";

        return "Empate";
    }
}
