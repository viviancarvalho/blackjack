package com.cardservice.model;

/**
 * Modelo de carta usado pelo regras-microservice.
 * Ele casa com o JSON retornado pelo card-microservice.
 */
public class Card {

    private final String value;

    public Card(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
