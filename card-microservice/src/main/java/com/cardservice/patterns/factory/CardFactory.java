package com.cardservice.patterns.factory;

import com.cardservice.model.Card;
import com.cardservice.model.NumberCard;
import com.cardservice.model.FaceCard;

/**
 * Factory responsável por criar instâncias de cartas,
 * aplicando o padrão Factory Method.
 */
public class CardFactory {

    public static Card createCard(String naipe, String rank) {

        if (rank.matches("[2-9]|10")) {
            return new NumberCard(naipe, Integer.parseInt(rank));
        }

        return new FaceCard(naipe, rank);
    }
}
