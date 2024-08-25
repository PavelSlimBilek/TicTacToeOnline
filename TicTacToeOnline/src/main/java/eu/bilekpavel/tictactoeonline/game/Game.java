package eu.bilekpavel.tictactoeonline.game;

import eu.bilekpavel.tictactoeonline.player.Player;
import jakarta.persistence.*;

public class Game {

    @GeneratedValue
    private String id;

    private Plan plan;
    private int size;

    private GameState state;
    private int turn;

    private Player player1;
    private char player1Symbol;

    private Player player2;
    private char player2Symbol;

    private Player currentPlayer;
    private Player winner;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        currentPlayer = player1;

        this.player2 = player2;

        this.size = 3;
        state = GameState.IN_PROGRESS;
        plan = new Plan(size);
        turn = 1;

        player1Symbol = 'O';
        player2Symbol = 'X';
    }

    public Game(Player player) {
        player1 = player;
        currentPlayer = player1;
        this.size = 3;
        state = GameState.IN_PROGRESS;
        plan = new Plan(size);
        turn = 1;

        player1Symbol = 'O';
        player2Symbol = 'X';
    }

    public Player getP1() {
        return player1;
    }

    public Player getP2() {
        return player2;
    }

    public void addPlayer(Player player) {
        if (player1 != null) {
            player2 = player;
        }
    }

    public boolean arePlayersReady() {
        return player1 != null && player2 != null;
    }

    public boolean makeMove(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            return false;
        }

        if (plan.board[x][y] == player1Symbol || plan.board[x][y] == player2Symbol) {
            return false;
        }

        plan.board[x][y] = currentPlayer.getSymbol();
        turn++;

        return true;
    }

    public void switchPlayer() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
    }

    public boolean doesPlayerWin () {
        char symbol = currentPlayer.getSymbol();
        // Check rows and columns
        for (int i = 0; i < size; i++) {
            if (checkRow(i, symbol) || checkColumn(i, symbol)) {
                return true;
            }
        }
        // Check diagonals
        if (checkPrimaryDiagonal(symbol) || checkSecondaryDiagonal(symbol)) {
            return true;
        }
        return false;
    }

    private boolean checkRow (int row, char symbol){
        for (int i = 0; i < size; i++) {
            if (plan.board[row][i] == null || plan.board[row][i] != symbol) {
                return false;
            }
        }
        return true;
    }

    private boolean checkColumn (int col, char symbol){
        for (int i = 0; i < size; i++) {
            if (plan.board[i][col] == null || plan.board[i][col] != symbol) {
                return false;
            }
        }
        return true;
    }

    private boolean checkPrimaryDiagonal (char symbol){
        for (int i = 0; i < size; i++) {
            if (plan.board[i][i] == null || plan.board[i][i] != symbol) {
                return false;
            }
        }
        return true;
    }

    private boolean checkSecondaryDiagonal (char symbol){
        for (int i = 0; i < size; i++) {
            if (plan.board[i][size - 1 - i] == null || plan.board[i][size - 1 - i] != symbol) {
                return false;
            }
        }
        return true;
    }

    public Player currentPlayer() {
        return currentPlayer;
    }
}
