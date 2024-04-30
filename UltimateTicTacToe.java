import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class MiniBoard {
    char[][] board = new char[3][3];

    public MiniBoard() {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public boolean checkWin(char player) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true; // Row win
            }
        }
        // Check columns
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true; // Column win
            }
        }

        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true; // Diagonal win from top-left to bottom-right
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true; // Diagonal win from top-right to bottom-left
        }
        return false; // No win
    }

    //(tie)
    public boolean isFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false; // Not full yet
                }
            }
        }
        return true; // All cells filled (tie)
    }
}

class UltimateBoard {
    MiniBoard[][] macroBoard = new MiniBoard[3][3]; // 3x3 macro board with 3x3 mini boards
    char[][] status = new char[3][3];

    public UltimateBoard() {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                macroBoard[i][j] = new MiniBoard(); // Create new mini board
                status[i][j] = ' '; // Default status (no winner)
            }
        }
    }

    public void applyMove(char player, int macroX, int macroY, int miniX, int miniY) {

        if (status[macroX][macroY] == ' ' && macroBoard[macroX][macroY].board[miniX][miniY] == ' ') {
            macroBoard[macroX][macroY].board[miniX][miniY] = player; // Set the cell

            if (macroBoard[macroX][macroY].checkWin(player)) { // Check for win in the mini board
                status[macroX][macroY] = player; // Update macro board status
            } else if (macroBoard[macroX][macroY].isFull()) { // Check for tie
                status[macroX][macroY] = '.'; // Set as tied
            }
        }
    }

    public List<int[]> getValidMoves(int macroX, int macroY) {
        List<int[]> validMoves = new ArrayList<>();
        if (status[macroX][macroY] == ' ') {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (macroBoard[macroX][macroY].board[i][j] == ' ') {
                        validMoves.add(new int[]{i, j}); // Collect empty cells
                    }
                }
            }
        }
        return validMoves; // Return list of valid moves
    }

    public char checkGameWinner() {
        // Check rows for a win
        for (int i = 0; i < 3; i++) {
            if (status[i][0] == status[i][1] && status[i][1] == status[i][2] && status[i][0] != ' ') {
                return status[i][0]; // Row win in the macro board
            }
        }

        // Check columns for a win
        for (int i = 0; i < 3; i++) {
            if (status[0][i] == status[1][i] && status[1][i] == status[2][i] && status[0][i] != ' ') {
                return status[0][i]; // Column win in the macro board
            }
        }

        // Check diagonals for a win
        if (status[0][0] == status[1][1] && status[1][1] == status[2][2] && status[0][0] != ' ') {
            return status[0][0]; // Diagonal win from top-left to bottom-right
        }
        if (status[0][2] == status[1][1] && status[1][1] == status[2][0] && status[0][2] != ' ') {
            return status[0][2]; // Diagonal win from top-right to bottom-left
        }

        // If no winner, return a space or similar character to indicate no winner
        return ' ';
    }
}

class Minimax {// Minimax with Alpha-Beta Pruning
    private static final int MAX_DEPTH = 5;

    public int[] minimax(UltimateBoard board, int depth, boolean isMaximizing, int macroX, int macroY, int alpha, int beta) {
        char gameWinner = board.checkGameWinner(); // Check for game winner
        if (gameWinner == 'X') {
            return new int[]{-10 + depth}; // X wins
        } else if (gameWinner == 'O') {
            return new int[]{10 - depth}; // O wins
        }

        if (depth >= MAX_DEPTH) {
            return new int[]{0}; // Depth limit reached (no winner)
        }

        List<int[]> validMoves = board.getValidMoves(macroX, macroY);

        if (validMoves.isEmpty()) {
            return new int[]{0}; // Tie, no valid moves
        }

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = null;

        for (int[] move : validMoves) {
            int miniX = move[0];
            int miniY = move[1];

            if (isMaximizing) {
                board.applyMove('O', macroX, macroY, miniX, miniY);
                int[] result = minimax(board, depth + 1, false, macroX, macroY, alpha, beta);
                if (result[0] > bestScore) {
                    bestScore = result[0];
                    bestMove = move; // Update best move
                }
                alpha = Math.max(alpha, bestScore); // Adjust alpha
                if (alpha >= beta) {
                    break; // Alpha-beta pruning
                }
                board.macroBoard[macroX][macroY].board[miniX][miniY] = ' '; // Reset the cell
            } else {
                board.applyMove('X', macroX, macroY, miniX, miniY);
                int[] result = minimax(board, depth + 1, true, macroX, macroY, alpha, beta);
                if (result[0] < bestScore) {
                    bestScore = result[0];
                    bestMove = move; // Update best move
                }
                beta = Math.min(beta, bestScore); // Adjust beta
                if (alpha >= beta) {
                    break; // Pruning
                }
                board.macroBoard[macroX][macroY].board[miniX][miniY] = ' '; // Reset the cell
            }
        }

        if (bestMove == null) {
            bestMove = new int[]{-1, -1}; // No valid moves found
        }

        return new int[]{bestScore, bestMove[0], bestMove[1]};
    }
}

// GUI
public class UltimateTicTacToe extends JFrame {
    private UltimateBoard board; // Main game board
    private JButton[][] buttons; // Buttons for the macro board
    private char currentPlayer; // 'X' or 'O'

    public UltimateTicTacToe() {
        setTitle("Ultimate Tic-Tac-Toe");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 3)); // Layout for macro board

        board = new UltimateBoard(); // Initialize the board
        buttons = new JButton[3][3]; // Initialize buttons
        currentPlayer = 'X'; // Default first player

        initializeButtons(); // Initialize the macro board buttons
        setVisible(true); // Make the GUI visible
    }

    private void initializeButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton button = new JButton();
                button.addActionListener(new ButtonClickListener(i, j));
                buttons[i][j] = button;
                add(button); // Add to the layout
            }
        }
    }

    private class ButtonClickListener implements ActionListener {
        private final int macroX;
        private final int macroY;

        public ButtonClickListener(int macroX, int macroY) {
            this.macroX = macroX;
            this.macroY = macroY;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (board.status[macroX][macroY] == ' ') {
                JFrame miniBoardFrame = new JFrame("Mini Tic-Tac-Toe");
                miniBoardFrame.setSize(200, 200);
                miniBoardFrame.setLayout(new GridLayout(3, 3));

                // Add buttons for each mini board cell
                for (int miniX = 0; miniX < 3; miniX++) {
                    for (int miniY = 0; miniY < 3; miniY++) {
                        JButton miniButton = new JButton();
                        miniButton.addActionListener(new MiniButtonListener(macroX, macroY, miniX, miniY));
                        miniBoardFrame.add(miniButton); // Add button to mini board frame
                    }
                }

                miniBoardFrame.setVisible(true); // Show the mini board for interaction
            } else {
                JOptionPane.showMessageDialog(null, "This sub-board is not playable.");
            }
        }
    }

    private class MiniButtonListener implements ActionListener {
        private final int macroX;
        private final int macroY;
        private final int miniX;
        private final int miniY;

        public MiniButtonListener(int macroX, int macroY, int miniX, int miniY) {
            this.macroX = macroX;
            this.macroY = macroY;
            this.miniX = miniX;
            this.miniY = miniY;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (board.macroBoard[macroX][macroY].board[miniX][miniY] == ' ') {
                board.applyMove(currentPlayer, macroX, macroY, miniX, miniY);

                if (board.macroBoard[macroX][macroY].checkWin(currentPlayer)) {
                    buttons[macroX][macroY].setText(String.valueOf(currentPlayer));
                    buttons[macroX][macroY].setEnabled(false);

                    char gameWinner = board.checkGameWinner();
                    if (gameWinner != ' ') {
                        JOptionPane.showMessageDialog(null, "Player " + gameWinner + " wins!");
                        resetGame();
                    } else {
                        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                    }
                }

                // AI Minimax move
                Minimax ai = new Minimax();
                int[] aiMove = ai.minimax(board, 0, true, macroX, macroY, Integer.MIN_VALUE, Integer.MAX_VALUE);

                if (aiMove.length > 1 && aiMove[1] != -1 && aiMove[2] != -1) {
                    board.applyMove('O', macroX, macroY, aiMove[1], aiMove[2]);

                    if (board.macroBoard[macroX][macroY].checkWin('O')) {
                        buttons[macroX][macroY].setText("O");
                        buttons[macroX][macroY].setEnabled(false);

                        char gameWinner = board.checkGameWinner();
                        if (gameWinner != ' ') {
                            JOptionPane.showMessageDialog(null, "Player O wins!");
                            resetGame();
                        } else {
                            currentPlayer = 'X'; // Switch back to the player
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "This cell is already occupied.");
            }
        }
    }

    private void resetGame() {
        board = new UltimateBoard(); // Re-initialize the game board
        currentPlayer = 'X'; // Default starting player
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true); // Reset all buttons
            }
        }
    }

    public static void main(String[] args) {
        new UltimateTicTacToe(); // Start the GUI
    }
}