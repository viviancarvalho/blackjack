package com.demo.regra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RegrasApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegrasApplication.class, args);
        System.out.println("=== Regras do jogo Microservice Running ===");
    }
}
