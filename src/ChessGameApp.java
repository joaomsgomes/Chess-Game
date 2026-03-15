import javax.swing.*;
import java.awt.*;

public class ChessGameApp extends JFrame {

    public ChessGameApp() {
        // Tamanho de cada casa no tabuleiro
        int tileSize = 75; 
        int boardSize = tileSize * 8; // 8x8 tabuleiro
        
        // Configura o tamanho do JFrame de acordo com o tamanho do tabuleiro
        setPreferredSize(new Dimension(boardSize + 12, boardSize + 30));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Inicializa o Board
        Board board = new Board();
        board.initializeBoard();

        // Cria e adiciona o BoardPanel com o tabuleiro
        BoardPanel boardPanel = new BoardPanel(board, this); // Passa a referência do JFrame
        updateTitle(boardPanel.getPlayer().getColor()); // Atualiza o título inicial
        add(boardPanel);

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateTitle(String playerColor) {
        setTitle("Chess Game (" + playerColor + "'s Turn)");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGameApp());
    }
}
