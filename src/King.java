
public class King extends Piece{

    private boolean hasMoved;
    private boolean hasBeenChecked;

    public King(String colour, Position position, Board board) {

        super(colour, position, board);
        hasMoved = false;
        hasBeenChecked = false; // Inicializa a verificação de checagem
    }

    public boolean isValidMove(Position newPosition) {
        
        int lineDifference = Math.abs(newPosition.getLine() - getPosition().getLine());
        int columnDifference = Math.abs(newPosition.getColumn() - getPosition().getColumn());
        
        return lineDifference <= 1 && columnDifference <= 1; // O rei pode mover-se uma casa em qualquer direção
    }


    public boolean isValidCapture(Position newPosition) {
        return isValidMove(newPosition);
    }

    public void gotChecked() {
        hasBeenChecked = true;
    }

    public void moveToPosition(Position newPosition) {
        if (isValidMove(newPosition)) {
            setPosition(newPosition); // Atualiza a posição
            hasMoved = true; // Atualiza se o rei já se moveu
        }
    }

    public int isValidCastle(Position castlePosition) {
        // O rei e a torre não devem ter se movido anteriormente
        if (hasBeenChecked || hasMoved) {
            
            return 0;
        }

        int row = getPosition().getLine();
        int col = getPosition().getColumn();

        // Check for Short or Long Castle
        boolean isKingSideCastle = castlePosition.getColumn() == col + 2;
        boolean isQueenSideCastle = castlePosition.getColumn() == col - 3;

        if (!isKingSideCastle && !isQueenSideCastle) {
            return 0;
        }

        // Determina a posição da torre correspondente
        Position rookPosition = isKingSideCastle ? new Position(row, 7) : new Position(row, 0);
        Piece rook = getBoard().getPiece(rookPosition);
        
        // Verifica se a torre existe e não se moveu
        if (rook == null || !rook.getType().equals("Rook") || ((Rook) rook).getHasMoved()) {
            return 0;
        }

        return isKingSideCastle ? 1 : -1;
    }

    public boolean getHasBeenChecked() { return hasBeenChecked;}

    public String getType() {return "King";}


}
