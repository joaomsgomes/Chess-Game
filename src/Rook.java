
public class Rook extends Piece{

    private boolean hasMoved;

    public Rook(String color, Position position, Board board) {
        super(color, position, board);
        hasMoved = false;
    }

    public void moved() {
        hasMoved = true;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    public boolean isValidMove(Position newPosition){

        int currentLine = getPosition().getLine();
        int currentColumn = getPosition().getColumn();
        int newLine = newPosition.getLine();
        int newColumn = newPosition.getColumn();

        int lineDifference = Math.abs(newLine - currentLine);
        int columnDifference = Math.abs(newColumn - currentColumn);

        if (lineDifference == 0 || columnDifference == 0){
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

    public String getType() {return "Rook";}

}