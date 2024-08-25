package eu.bilekpavel.tictactoeonline.player;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue
    private String id;

    private String session;

    public Player(String session) {
        this.session = session;
    }
}
