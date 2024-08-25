package eu.bilekpavel.tictactoeonline.player;

import lombok.Getter;

@Getter
public class Player {

    private String session;
    private char symbol;

    public Player(String session, char symbol) {
        this.session = session;
        this.symbol = symbol;
    }
}
