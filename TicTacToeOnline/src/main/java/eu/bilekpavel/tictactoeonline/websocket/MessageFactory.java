package eu.bilekpavel.tictactoeonline.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
public class MessageFactory {

    public TextMessage error(String message) {
        return new TextMessage(
                String.format("[\"ERROR\",\"%s\"]",
                        message
                )
        );
    }

    public TextMessage move(int x, int y, char symbol) {
        return new TextMessage(
                String.format("[\"MOVE\",\"%d-%d\",\"%c\"]",
                        x,
                        y,
                        symbol
                )
        );
    }

    public TextMessage symbol(char symbol) {
        return new TextMessage(
                String.format("[\"SYMBOL\",\"%c\"]",
                        symbol
                )
        );
    }

    public TextMessage started() {
        return new TextMessage("[\"STARTED\"]");
    }

    public TextMessage ended() {
        return new TextMessage("[\"ENDED\"]");
    }
}
