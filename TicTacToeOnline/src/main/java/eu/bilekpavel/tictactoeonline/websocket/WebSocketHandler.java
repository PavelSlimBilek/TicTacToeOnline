package eu.bilekpavel.tictactoeonline.websocket;

import eu.bilekpavel.tictactoeonline.game.Game;
import eu.bilekpavel.tictactoeonline.player.Player;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final Set<WebSocketSession> SESSIONS = Collections.synchronizedSet(new HashSet<>());

    private static final Set<Game> GAMES = Collections.synchronizedSet(new HashSet<>());
    private static final Set<Player> PLAYERS = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        SESSIONS.add(session);
        System.out.println("New session established: " + session.getId());
        System.out.println("current SESSIONS size: " + SESSIONS.size());

        // TODO belongs to another place
        if (GAMES.isEmpty()) {
            Player p = new Player(session.getId());
            Game g = new Game(p);
            GAMES.add(g);
            return;
        }

        Game currentGame = GAMES.iterator().next();
        currentGame.addPlayer(new Player(session.getId()));
        if (currentGame.arePlayersReady()) {
            System.out.println("Game started");
            SESSIONS.forEach(s -> {
                try {
                    s.sendMessage(new TextMessage("[STARTED]"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull org.springframework.web.socket.CloseStatus status) {
        SESSIONS.remove(session);
        System.out.println("Session closed: " + session.getId());
        System.out.println("current SESSIONS size: " + SESSIONS.size());
    }

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        System.out.println("Client message: " + payload);
        SESSIONS.forEach(s -> {
            try {
                s.sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
