package eu.bilekpavel.tictactoeonline.websocket;

import eu.bilekpavel.tictactoeonline.game.Game;
import eu.bilekpavel.tictactoeonline.player.Player;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final Map<Player, WebSocketSession> SESSIONS = Collections.synchronizedMap(new HashMap<>());
    private Game game;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        Player p;
        if (SESSIONS.isEmpty()) {
            p = new Player(session.getId(), 'O');
        } else {
            p = new Player(session.getId(), 'X');
        }
        SESSIONS.put(p, session);

        System.out.println("New session established: " + session.getId());
        System.out.println("current SESSIONS size: " + SESSIONS.size());

        // TODO belongs to another place
        if (game == null) {
            game = new Game(p, 3);
            return;
        }

        game.addPlayer(new Player(session.getId(), 'X'));
        if (game.arePlayersReady()) {
            System.out.println("Game started");
            SESSIONS.forEach((key, value) -> {
                try {
                    value.sendMessage(
                            new TextMessage("[\"STARTED\"]"));
                    value.sendMessage(
                            new TextMessage(
                                    String.format("[\"SYMBOL\",\"%c\"]", key.getSymbol())
                            )
                    );
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull org.springframework.web.socket.CloseStatus status) {
        SESSIONS.values().remove(session);
        System.out.println("Session closed: " + session.getId());
        System.out.println("current SESSIONS size: " + SESSIONS.size());
    }

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        System.out.println("Client message: " + payload);

        if (!game.currentPlayer().getSession().equals(session.getId())) {
            session.sendMessage(
                    new TextMessage("[\"ERROR\",\"not your turn\"]")
            );
            return;
        }

        int x = Integer.parseInt(payload.split("-")[0]);
        int y = Integer.parseInt(payload.split("-")[1]);

        if (game.makeMove(x, y)) {
            SESSIONS.values().forEach(s -> {
                try {
                    s.sendMessage(
                            new TextMessage(
                                    String.format("[\"MOVE\",\"%d-%d\",\"%c\"]", x, y, game.currentPlayer().getSymbol())
                            )
                    );

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            game.switchPlayer();
        }

        if (game.doesPlayerWin()) {
            SESSIONS.values().forEach(s -> {
                try {
                    s.sendMessage(new TextMessage("[\"ENDED\"]"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
