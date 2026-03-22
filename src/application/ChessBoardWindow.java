package application;

import boardlayer.Board;
import chesslayer.ChessPiece;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.swing.*;

public class ChessBoardWindow {
    private JFrame frame;
    private JButton[][] boardButtons;
    private String selectedCell;
    private CompletableFuture<String> clickFuture;
    private boolean[][] greenTiles;
    private static final int BOARD_SIZE = 8;
    private static final int CELL_SIZE = 50;

    public ChessBoardWindow() {
        boardButtons = new JButton[BOARD_SIZE][BOARD_SIZE];
        selectedCell = null;
        clickFuture = new CompletableFuture<>();
        greenTiles = new boolean[BOARD_SIZE][BOARD_SIZE];
    }

    public void buildWindow() {
        frame = new JFrame("Chess Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create the chess board panel
        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        boardPanel.setPreferredSize(new Dimension(BOARD_SIZE * CELL_SIZE, BOARD_SIZE * CELL_SIZE));

        // Create the chess board with alternating colors
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                button.setFocusPainted(false);

                // Alternate colors (black and white)
                if ((row + col) % 2 == 0) {
                    button.setBackground(Color.WHITE);
                } else {
                    button.setBackground(Color.BLACK);
                }

                // Set the cell identifier
                char columnChar = (char) ('a' + col);
                String cellId = columnChar + String.valueOf(BOARD_SIZE - row);
                button.setName(cellId);

                // Add click listener
                final int currentRow = row;
                final int currentCol = col;
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (clickFuture != null && !clickFuture.isDone()) {
                            // Convert [row][col] to chess notation
                            String cell = ((JButton) e.getSource()).getName();
                            clickFuture.complete(cell);
                        }
                    }
                });

                boardButtons[currentRow][currentCol] = button;
                boardPanel.add(button);
            }
        }

        frame.add(boardPanel, BorderLayout.CENTER);

        // Add row and column labels
        addLabels(boardPanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addLabels(JPanel boardPanel) {
        // Add column labels (a-h)
        JPanel topPanel = new JPanel(new GridLayout(1, BOARD_SIZE));
        for (char c = 'a'; c <= 'h'; c++) {
            JLabel label = new JLabel(String.valueOf(c), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 12));
            label.setForeground(Color.BLACK);
            topPanel.add(label);
        }
        frame.add(topPanel, BorderLayout.NORTH);

        // Add row labels (1-8)
        JPanel leftPanel = new JPanel(new GridLayout(BOARD_SIZE, 1));
        for (int i = BOARD_SIZE; i >= 1; i--) {
            JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 12));
            label.setForeground(Color.BLACK);
            leftPanel.add(label);
        }
        frame.add(leftPanel, BorderLayout.WEST);
    }

    public String waitForClick() throws ExecutionException, InterruptedException {
        // Create a new future for this click
        clickFuture = new CompletableFuture<>();
        String result = clickFuture.get();
        return result;
    }

    // Method to receive green tiles matrix and highlight them
    // Note: pMov is filled with [columns][rows] indexing, so we need to convert
    public void setGreenTiles(boolean[][] pMov) {
        if (pMov == null || pMov.length != BOARD_SIZE || pMov[0].length != BOARD_SIZE) {
            return;
        }

        // Copy the green tiles matrix - need to convert [col][row] to [row][col]
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                // Convert from [col][row] to [row][col] indexing and makes it bottom to top
                greenTiles[7-row][col] = pMov[col][row];
            }
        }

        // Update the board buttons to show green highlights
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (greenTiles[row][col]) {
                    // Highlight with green border
                    boardButtons[row][col].setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                } else {
                    // Remove green border and restore original background
                    boardButtons[row][col].setBorder(null);
                    // Restore original background color
                    if ((row + col) % 2 == 0) {
                        boardButtons[row][col].setBackground(Color.WHITE);
                    } else {
                        boardButtons[row][col].setBackground(Color.BLACK);
                    }
                }
            }
        }

        // Revalidate and repaint the frame
        frame.revalidate();
        frame.repaint();
    }

    // Method to update board state (optional)
    public void updateBoard() {
        // This method can be extended to update the board state
        // based on game logic
    }

    // Method to close the window
    public void close() {
        if (frame != null) {
            frame.dispose();
        }
    }
    public void scanBoardPieces (ChessPiece[][] chessBoardPosition){
        for (int i = 0; i < Board.getColumns(); i++) {
            for (int j = 0; j < Board.getRows(); j++) {
                if(chessBoardPosition[i][j]!=null){
                    printPiece(chessBoardPosition[i][j].toString().trim());
                }
            }
            
        }
    }
    // New printPiece function
    public void printPiece(String piece) {
       // TODO - print chesspieces on the board
    }
}