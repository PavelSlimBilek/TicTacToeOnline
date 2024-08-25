package eu.bilekpavel.tictactoeonline.game;

public class Plan {

    Character[][] board;

    public Plan(int size) {
        board = new Character[size][size];
        initializeBoard(size);
    }

    private void initializeBoard(int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = ' ';
            }
        }
    }
}
