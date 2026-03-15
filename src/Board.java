

public class Board {


    private Piece[][] board = new Piece[8][8]; //Each Square can have a piece

    public Board() {
        initializeBoard();
    }

    public void resetBoard() {

        board = new Piece[8][8];
        initializeBoard();
    }


    public void initializeBoard() {

        // Initialize white pieces
        initializeMainRow(7,"WHITE");
        initializePawns(6,"WHITE");
        // Initialize black pieces
        initializeMainRow(0,"BLACK");
        initializePawns(1,"BLACK");

    }

    public void initializePawns(int row, String color) {
        for (int col = 0; col < 8; col++) {
            board[row][col] = new Pawn(color, new Position(row, col), this);
        }
    }   

    public void initializeMainRow(int row, String color) {

        board[row][0] = new Rook(color, new Position(row, 0), this);
        board[row][1] = new Knight(color, new Position(row, 1), this);
        board[row][2] = new Bishop(color, new Position(row, 2), this);
        board[row][3] = new Queen(color, new Position(row, 3), this);
        board[row][4] = new King(color, new Position(row, 4), this);
        board[row][5] = new Bishop(color, new Position(row, 5), this);
        board[row][6] = new Knight(color, new Position(row, 6), this);
        board[row][7] = new Rook(color, new Position(row, 7), this);
    }

    public Piece getPiece(Position position) {

        int line = position.getLine();
        int column = position.getColumn();

        return board[line][column];

    }
    
    public void movePiece(Piece piece, Position to) {

        Position from = piece.getPosition(); // Check the Starting Position
        updatePiece(null, from);  //Remove piece      
        piece.setPosition(to); //Update Piece's Position      
        updatePiece(piece, to);  //Add Piece to the board

        if (piece instanceof Pawn) {
            ((Pawn) piece).moved();
        } else if (piece instanceof Rook) {
            ((Rook) piece).moved();
        }
    }

    public void updatePiece(Piece piece, Position position) {
        board[position.getLine()][position.getColumn()] = piece;
    }


    public boolean isValidDestination(Position end) {

        Piece targetPiece = getPiece(end);
    
        return targetPiece == null;
    }

    public boolean verifyCapture(Position start, Position end) {

        //Function that if target piece really exists, different color, not a king

        return getPiece(end) != null
            && !getPiece(start).getColor().equals(getPiece(end).getColor())
            && !(getPiece(end) instanceof King);
    }


}

