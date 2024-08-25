package eu.bilekpavel.tictactoeonline.game;

import eu.bilekpavel.tictactoeonline.player.Player;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue
    private String id;

    @Enumerated
    private GameState state;

    @OneToOne
    private Player player1;
    @OneToOne
    private Player player2;

    public Game(Player player1) {
        this.state = GameState.IN_PROGRESS;
        this.player1 = player1;
    }

    public void addPlayer(Player player) {
        if (player1 != null) {
            player2 = player;
        }
    }

    public boolean arePlayersReady() {
        return player1 != null && player2 != null;
    }
}
