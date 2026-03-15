

public class Knight extends Piece {

    public Knight(String colour, Position position, Board board) {
        super(colour, position, board);
    }

    public boolean isValidMove(Position newPosition) {
        
        int lineDifference = Math.abs(newPosition.getLine() - getPosition().getLine()); // Usando getPosition()
        int columnDifference = Math.abs(newPosition.getColumn() - getPosition().getColumn()); // Usando getPosition()

            // Verifica os movimentos válidos do cavalo
        if (lineDifference == 1 && columnDifference == 2) {
            return true;
        } else if (lineDifference == 2 && columnDifference == 1) {
            return true;
        } else{
            return false;
        }
        
    }

    public boolean isValidCapture(Position newPosition) {
        return isValidMove(newPosition) ;
    }

    public void moveToPosition(Position newPosition) {
        if (isValidMove(newPosition)) {
            
            setPosition(newPosition);
        }
    }

    public String getType() {return "Knight";}
}