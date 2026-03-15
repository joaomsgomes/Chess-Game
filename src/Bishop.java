
public class Bishop extends Piece{


    public Bishop(String colour, Position position, Board board) {

        super(colour, position, board);
    }


    public boolean isValidMove(Position newPosition) {

        int currentLine = getPosition().getLine();
        int currentColumn = getPosition().getColumn();
        int newLine = newPosition.getLine();
        int newColumn = newPosition.getColumn();

        int lineDifference = Math.abs(newLine - currentLine);
        int columnDifference = Math.abs(newColumn - currentColumn);

        if (lineDifference == columnDifference) {
            return isPathClear(currentLine, currentColumn, newLine, newColumn);
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

    public String getType() {return "Bishop";}
}