import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.ArrayList;

public class BoardPanel extends JPanel {


    private Board board;
    private Piece selectedPiece;
    private Position selectedPosition;
    private final int tileSize = 75;
    private Player player;
    private ChessGameApp app;
    private boolean isInCheck = false;
    private Piece attackingPiece = null;


    private BufferedImage whitePawn, blackPawn;
    private BufferedImage whiteKnight, blackKnight;
    private BufferedImage whiteBishop, blackBishop;
    private BufferedImage whiteRook, blackRook;
    private BufferedImage whiteQueen, blackQueen;
    private BufferedImage whiteKing, blackKing;

    //Adds a label to the interface
    public BoardPanel(Board board, ChessGameApp app) {
        this.board = board;
        this.player = new Player("WHITE");
        this.app = app;

        setPreferredSize(new Dimension(800, 800));
        loadImages(); //Load every image for each piece
        addMouseListener(new PieceSelectionListener());
    }

    public Player getPlayer() {
        return player;
    }


    private void loadImages() {
        try {
            whitePawn = ImageIO.read(new File("../Images/white-pawn.png"));
            blackPawn = ImageIO.read(new File("../Images/black-pawn.png"));

            whiteKnight = ImageIO.read(new File("../Images/white-knight.png"));
            blackKnight = ImageIO.read(new File("../Images/black-knight.png"));

            whiteBishop = ImageIO.read(new File("../Images/white-bishop.png"));
            blackBishop = ImageIO.read(new File("../Images/black-bishop.png"));

            whiteRook = ImageIO.read(new File("../Images/white-rook.png"));
            blackRook = ImageIO.read(new File("../Images/black-rook.png"));

            whiteQueen = ImageIO.read(new File("../Images/white-queen.png"));
            blackQueen = ImageIO.read(new File("../Images/black-queen.png"));

            whiteKing = ImageIO.read(new File("../Images/White-King.png"));
            blackKing = ImageIO.read(new File("../Images/black-king.png"));

        } catch (IOException e) {
            System.err.println("Erro ao carregar imagens das peças: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawPieces(g);
    }

    private void drawBoard(Graphics g) {
        
        Color lightBrown = new Color(222, 184, 135);
        Color darkBrown = new Color(139, 69, 19);

        for (int row = 0; row < 8; row++) {

            for (int col = 0; col < 8; col++) {

                g.setColor((row + col) % 2 == 0 ? lightBrown : darkBrown);
                g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
            }
        }
    }

    private void drawPieces(Graphics g) {
        
        for (int row = 0; row < 8; row++) {

            for (int col = 0; col < 8; col++) {
                
                Piece piece = board.getPiece(new Position(row, col));
                if (piece != null) {
                    drawPiece(g, piece, col * tileSize, row * tileSize);
                }
            }
        }
    }

    private void drawPiece(Graphics g, Piece piece, int x, int y) {

        BufferedImage image = switch (piece.getType()) {

            case "Pawn" -> piece.getColor().equals("WHITE") ? whitePawn : blackPawn;
            case "Knight" -> piece.getColor().equals("WHITE") ? whiteKnight : blackKnight;
            case "Bishop" -> piece.getColor().equals("WHITE") ? whiteBishop : blackBishop;
            case "Rook" -> piece.getColor().equals("WHITE") ? whiteRook : blackRook;
            case "Queen" -> piece.getColor().equals("WHITE") ? whiteQueen : blackQueen;
            case "King" -> piece.getColor().equals("WHITE") ? whiteKing : blackKing;

            default -> null;
        };
        if (image != null) {

            g.drawImage(image, x + 10, y + 10, 55, 55, this);
        }
    }


    private boolean isPlayerTurn(String color) {

        return player.getColor().equals(color);
    }

    private void switchTurn() {
        player.switchTurn(); 
        app.updateTitle(player.getColor());  
    }

    
    private class PieceSelectionListener extends MouseAdapter {

        
        @Override
        public void mousePressed(MouseEvent e) {
            int col = e.getX() / tileSize;
            int row = e.getY() / tileSize;
            
            if (row < 0 || row >= 8 || col < 0 || col >= 8) {
                return;
            }

            Position clickedPosition = new Position(row, col);

            // Select the piece at the clicked position
            if (selectedPiece == null) {
                selectedPiece = board.getPiece(clickedPosition);
                selectedPosition = clickedPosition;

                // Check if the piece belongs to the current player
                if (selectedPiece == null || !isPlayerTurn(selectedPiece.getColor())) {
                    resetSelectedAttributes();
                    return;
                }

                // If the opponent is in check, allow only moves that resolve the check
            } else if (isInCheck) {

                Position opponentKingPosition = findKingPosition(player.getColor());
                Piece opponentKing = board.getPiece(opponentKingPosition);

                if (opponentKing instanceof King) {
                    ((King) opponentKing).gotChecked();
                }

                // Allow moves that resolve the check or moving the king itself
                if (selectedPiece.getType().equals("King")) {
                    // Allow king's move
                    if (moveCheckedKing(selectedPiece, clickedPosition))  {
            
                        if (isSafeMoveForKing(selectedPosition, clickedPosition)) {

                            board.movePiece(selectedPiece, clickedPosition);
                            isInCheck = false;
                            attackingPiece = null;
                            resetSelectedAttributes();
                            repaint();
                            switchTurn();
                            return;
                        } else {
                            resetSelectedAttributes();
                        }
                    }
                } else {
                    // Check if the selected piece can either block or capture the attacking piece
                    if (defendCheck(selectedPiece, opponentKingPosition, clickedPosition, selectedPosition) && isKingExposed(selectedPosition, clickedPosition, selectedPiece)) {
                        
                            board.movePiece(selectedPiece, clickedPosition);
                            attackingPiece = null;
                            resetSelectedAttributes();
                            repaint();
                            switchTurn();
                            return;
                    }
                    resetSelectedAttributes();
                }
            } else {
                // Continue with normal movement logic
                if (move(clickedPosition) && isKingExposed(selectedPosition, clickedPosition, selectedPiece)) {
                    
                    board.movePiece(selectedPiece, clickedPosition);
                    isInCheck = isOpponentKingInCheck(player.getColor());
                    attackingPiece = selectedPiece;

                    Piece transformedPiece = transformPawnToQueen(selectedPiece);
                    if (transformedPiece != null) {
                        board.updatePiece(transformedPiece, clickedPosition);
                    }

                    resetSelectedAttributes();
                    repaint();
                    switchTurn();

                } else if (capture(selectedPosition, clickedPosition, selectedPiece) && isKingExposed(selectedPosition, clickedPosition, selectedPiece)) {

                    board.movePiece(selectedPiece, clickedPosition);
                    isInCheck = isOpponentKingInCheck(player.getColor());
                    attackingPiece = selectedPiece;

                    Piece transformedPiece = transformPawnToQueen(selectedPiece);
                    if (transformedPiece != null) {
                        board.updatePiece(transformedPiece, clickedPosition);
                    }

                    resetSelectedAttributes();
                    repaint();
                    switchTurn();

                }
                else if(castle(clickedPosition, selectedPiece)) {

                    board.movePiece(selectedPiece, clickedPosition);
                    rookPerformCastle(clickedPosition);
                    resetSelectedAttributes();
                    repaint();
                    switchTurn();

                } else {
                    resetSelectedAttributes();
                }
            }
        }

        /////////////////////////////////////// POSSIBLE MOVES //////////////////////////////////////////////////



        public boolean capture(Position selectedPosition, Position clickedPosition, Piece selectedPiece) {

            return selectedPiece.isValidCapture(clickedPosition) && board.verifyCapture(selectedPosition, clickedPosition);

        }

        public boolean move(Position clickedPosition) {

            return selectedPiece.isValidMove(clickedPosition) && board.isValidDestination(clickedPosition);
        }

        public boolean defendCheck(Piece selectedPiece, Position opponentKingPosition, Position clickedPosition, Position selectedPosition) {

            return canBlockCheck(selectedPiece, opponentKingPosition, clickedPosition)
            && (selectedPiece.isValidMove(clickedPosition) || selectedPiece.isValidCapture(clickedPosition) 
            && board.verifyCapture(selectedPosition,clickedPosition));
        }

        public boolean moveCheckedKing(Piece selectedPiece, Position clickedPosition) {

            return (selectedPiece.isValidMove(clickedPosition) && board.isValidDestination(clickedPosition)) || selectedPiece.isValidCapture(clickedPosition);
        }

        public boolean castle(Position newPosition, Piece selectedPiece) {
        
            if (selectedPiece instanceof King) {
                // Realiza o cast para King
                King king = (King) selectedPiece;
                return !isInCheck && king.isPathClear(king.getPosition().getLine(), 
                king.getPosition().getColumn(),
                newPosition.getLine(),
                newPosition.getColumn())
                && Math.abs(king.isValidCastle(newPosition)) == 1 
                && isCastleSafe(newPosition);
                    
            }
            return false;
        }

        
 ////////////////////////////////////// SUPPORT FUNCTIONS /////////////////////////////////////////

        
        // Função para verificar se o jogador está em checkmate
        /*public boolean isCheckMate(String playerColor) {
            String opponentColor = playerColor.equals("WHITE") ? "BLACK" : "WHITE";

            // Localiza a posição do rei do jogador
            Position kingPosition = findKingPosition(playerColor);
        
            // Verifica todas as peças do jogador
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Position currentPosition = new Position(row, col);
                    Piece piece = board.getPiece(currentPosition);
                    if piece.getColor().equals(playerColor) {
                        if (canBlockCheck(piece, kingPosition, ))


                    }

                    
                }
            }

            return true;
        }
        */

        public boolean isCastleSafe(Position castlePosition) {

            int step = ((King) selectedPiece).isValidCastle(castlePosition);
            int col = selectedPiece.getPosition().getColumn();
            int row = selectedPiece.getPosition().getLine();

            for (int i = col; i != castlePosition.getColumn() + step; i += step) {
                Position checkPosition = new Position(row, i);
                if (!isSafeMoveForKing(selectedPosition, checkPosition)) {
                    return false;
                }
            }
            return true;
        }
        
        public void rookPerformCastle(Position castlePosition) {
            
            int row = selectedPiece.getPosition().getLine() == 7 ? 7 : 0; // 7 for white, 0 for black
            int typeCastle = castlePosition.getColumn() == 6 ? 1 : -1; // 1 for Short -1 for Long
            
            Rook rook = (Rook) (typeCastle == 1 
            ? board.getPiece(new Position(row, 7))  // King-side rook
            : board.getPiece(new Position(row, 0))); // Queen-side rook

            Position rookPosition = rook.getPosition();

            board.updatePiece(null, rookPosition); //Remove the rook from original position
            if (typeCastle == 1){
                board.updatePiece(rook, new Position(rookPosition.getLine(), rookPosition.getColumn() - 2));
            }
            else {
                board.updatePiece(rook, new Position(rookPosition.getLine(),rookPosition.getColumn() + 2 ));
            }
        }        
        
    
        public Piece transformPawnToQueen(Piece piece) {

            if (piece instanceof Pawn) {
                if (((Pawn) piece).verifyPawnToQueen()) {

                    Position piecePosition = piece.getPosition();
                    Piece newQueen = new Queen(piece.getColor(), piecePosition, board);
                    board.updatePiece(null, piecePosition);
                    board.updatePiece(newQueen, piecePosition);
                    return newQueen;
                }
            }
            return null;
        }

        public void resetSelectedAttributes() {
            selectedPiece = null;
            selectedPosition = null;
        }


        public boolean isOpponentKingInCheck(String playerColor) {

            // Determina a cor do rei adversário
            String opponentColor = playerColor.equals("WHITE") ? "BLACK" : "WHITE";
            
            // Localiza a posição do rei adversário
            Position opponentKingPosition = findKingPosition(opponentColor);
            
            if (opponentKingPosition == null) {
                // Caso de erro: não encontrou o rei (não deveria ocorrer em um jogo válido)
                return false;
            }
            
            // Verifica se qualquer peça do jogador atual pode atingir o rei adversário
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {

                    Position position = new Position(row, col);
                    Piece piece = board.getPiece(position);
                    
                    // Verifica se é uma peça do jogador atual
                    if (piece != null && piece.getColor().equals(playerColor)) {
                        
                        // Verifica se a peça pode mover-se até a posição do rei adversário
                        if (piece.isValidCapture(opponentKingPosition)) {
                            return true; // O rei adversário está em cheque
                        }
                    }
                }
            }
            
            return false; // O rei adversário não está em cheque
        }


        public boolean canBlockCheck(Piece blockingPiece, Position opponentKingPosition, Position clickedPosition) {
    
            
            Position attackerPosition = attackingPiece.getPosition();
            
            int rowDirection = Integer.signum(opponentKingPosition.getLine() - attackerPosition.getLine());
            int colDirection = Integer.signum(opponentKingPosition.getColumn() - attackerPosition.getColumn());
            
            
            Position currentPosition = attackerPosition.translate(rowDirection, colDirection);

            while (currentPosition != null && !currentPosition.equals(opponentKingPosition)) {
                
                if (blockingPiece.isValidMove(currentPosition) && board.isValidDestination(currentPosition) && clickedPosition.equals(currentPosition)) {

                    isInCheck = false; // The check can be blocked
                    return true;
                }

                // Move to the next position towards the king
                currentPosition = currentPosition.translate(rowDirection, colDirection);
            }

            // If no blocking move found, check if the blocking piece can capture the attacker
            if (blockingPiece.isValidCapture(attackerPosition)) {

                isInCheck = false;
                attackingPiece = null;
                return true;
            }
            

            return false;
        }



        public Position findKingPosition(String color) {

            for (int row = 0; row < 8; row++) {

                for (int col = 0; col < 8; col++) {

                    Position position = new Position(row, col);
                    Piece piece = board.getPiece(position);
                    
                    if (piece != null && piece.getType().equals("King") && piece.getColor().equals(color)) {
                        return position;
                    }
                }
            }
            return null; 
        }


        public boolean isSafeMoveForKing(Position kingPosition, Position destination) {
            
            Piece originalPieceAtDestination = board.getPiece(destination);
            board.updatePiece(null, kingPosition); // Remove o rei da posição original
            board.updatePiece(new King(player.getColor(), destination, board), destination); // Move o rei para a posição de destino

            boolean willBeInCheck = isOpponentKingInCheck(player.getColor().equals("WHITE") ? "BLACK" : "WHITE"); // Verifica se o rei está em cheque na nova posição
            
            board.updatePiece(new King(player.getColor(), kingPosition, board), kingPosition); // Retorna o rei para a posição original
            board.updatePiece(originalPieceAtDestination, destination); // Restaura a peça original na posição de destino

            return !willBeInCheck; // Retorna true se o movimento for seguro (rei não está em cheque)
        }


        public boolean isKingExposed(Position piecePosition, Position destination, Piece piece) {
            // Salva a peça originalmente na posição de destino (se houver)
            Piece originalPieceAtDestination = board.getPiece(destination);

            // Remove a peça da sua posição original
            board.updatePiece(null, piecePosition);

            // Cria uma nova instância da peça na posição de destino usando switch
            Piece movedPiece = null;
            switch (piece.getType()) {
                case "King":
                    movedPiece = new King(piece.getColor(), destination, board);
                    break;
                case "Queen":
                    movedPiece = new Queen(piece.getColor(), destination, board);
                    break;
                case "Rook":
                    movedPiece = new Rook(piece.getColor(), destination, board);
                    break;
                case "Bishop":
                    movedPiece = new Bishop(piece.getColor(), destination, board);
                    break;
                case "Knight":
                    movedPiece = new Knight(piece.getColor(), destination, board);
                    break;
                case "Pawn":
                    movedPiece = new Pawn(piece.getColor(), destination, board);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown piece type: " + piece.getType());
            }

            // Move a peça para a posição de destino
            board.updatePiece(movedPiece, destination);

            // Verifica se o rei está em cheque na nova posição
            boolean willBeInCheck = isOpponentKingInCheck(piece.getColor().equals("WHITE") ? "BLACK" : "WHITE");

            // Restaura a peça original na posição inicial
            board.updatePiece(piece, piecePosition);

            // Restaura a peça original na posição de destino
            board.updatePiece(originalPieceAtDestination, destination);

            // Retorna true se o movimento for seguro (o rei não estará em cheque)
            return !willBeInCheck;
        }
        

    }

}
