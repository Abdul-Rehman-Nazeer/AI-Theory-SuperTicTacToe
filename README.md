**Ultimate Tic-Tac-Toe Game**

**Overview**

This is a Java-based Ultimate Tic-Tac-Toe game with a GUI built using Swing. The game features a 3x3 grid of Tic-Tac-Toe boards (a "macro board"), with each cell containing a 3x3 mini-board. Players take turns making moves, and the game ends when a player wins three mini-boards in a row, column, or diagonal, or when all mini-boards are full resulting in a tie.

**Features**

Player vs. AI: The game allows a human player to play against an AI opponent that uses a Minimax algorithm with Alpha-Beta Pruning.
Dynamic GUI: A graphical user interface (GUI) with a 3x3 grid representing the macro-board. Clicking a macro-board cell opens a mini-board to play.
Win/Tie Detection: The code checks for wins by rows, columns, or diagonals and handles ties when all mini-boards are full.
Game Reset: Includes a reset function to start a new game.

**How to Run**

Prerequisites: Ensure that you have a Java Development Kit (JDK) installed on your system.
Compile the Code: Open a terminal or command prompt in the directory where your Java code is located and compile it:
sh
Copy code
javac UltimateTicTacToe.java
Run the Game: After compilation, run the code to start the GUI:
sh
Copy code
java UltimateTicTacToe

**Playing the Game**

Start a New Game: Once the GUI is open, you can start a new game by clicking on a macro-board cell, which opens a mini-board.
Make a Move: Click a cell within the mini-board to place your mark (X for the human player). The AI (playing as O) will then make its move.
Check for a Winner: The game automatically checks for a win or tie and displays a message if a player wins or the game ends in a tie.
Restart the Game: To reset the game and start over, click the "restart" button.

**Code Structure**

MiniBoard: Represents a single 3x3 Tic-Tac-Toe board.
UltimateBoard: Represents the 3x3 grid of mini-boards and tracks the overall status.
Minimax: Implements the Minimax algorithm with Alpha-Beta Pruning to determine AI moves.
UltimateTicTacToe: The main GUI class that initializes the game and handles user interactions.

**Issues and Contributions**

If you encounter any issues or have suggestions for improvement, feel free to open an issue or submit a pull request on GitHub. Contributions are welcome!
