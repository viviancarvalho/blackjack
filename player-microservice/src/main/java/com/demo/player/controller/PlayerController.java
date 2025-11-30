package com.demo.player.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/player")
public class PlayerController {

    private String name = "Jogador";
    private int points = 0;

    @GetMapping("/state")
    public String getState() {
        return "Nome: " + name + " | Pontos: " + points;
    }

    @PostMapping("/add/{value}")
    public String addPoints(@PathVariable int value) {
        points += value;
        return "Pontos atualizados: " + points;
    }

    @PostMapping("/reset")
    public String reset() {
        points = 0;
        return "Jogador resetado.";
    }
}
