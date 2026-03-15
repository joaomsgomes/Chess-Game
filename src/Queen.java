

public class Queen extends Piece {

    public Queen(String colour, Position position, Board board) {

        super(colour,position, board);
    }


    public boolean isValidMove(Position newPosition) {

        int currentLine = getPosition().getLine();
        int currentColumn = getPosition().getColumn();
        int newLine = newPosition.getLine();
        int newColumn = newPosition.getColumn();

        int lineDifference = Math.abs(newLine - currentLine);
        int columnDifference = Math.abs(newColumn - currentColumn);

        // Verifica se o movimento é em linha reta (horizontal ou vertical) ou diagonal
        boolean isLinearMove = (lineDifference == 0 || columnDifference == 0);  // Linha ou coluna
        boolean isDiagonalMove = lineDifference == columnDifference;             // Diagonal

        // Confirma que o movimento está permitido e que o caminho está livre
        if ((isLinearMove || isDiagonalMove) && isPathClear(currentLine, currentColumn, newLine, newColumn)) {
            return true;
        }
        return false; 
    }

    public boolean isValidCapture(Position newPosition) {
        return isValidMove(newPosition);
    }

    public void moveToPosition(Position newPosition) {
        
        if (isValidMove(newPosition)){
            
            setPosition(newPosition);

        }
    }

    public String getType() {return "Queen";}

}


