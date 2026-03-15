import java.util.List;
import java.util.ArrayList;


public abstract class Piece{
    
    
    private String _colour;
    private Position _position;
    private Board _board;

    public Piece(String colour, Position position, Board board) {

        _colour = colour;
        _position = position;
        _board = board;
    }

    public abstract boolean isValidMove(Position newPosition);

    public abstract boolean isValidCapture(Position newPosition);

    public abstract void moveToPosition(Position position);
    
    public boolean isPathClear(int currentLine, int currentColumn, int newLine, int newColumn) {


        int lineStep = Integer.compare(newLine, currentLine); // -1, 0 ou 1
        int columnStep = Integer.compare(newColumn, currentColumn); // -1, 0 ou 1

        int line = currentLine + lineStep;
        int column = currentColumn + columnStep;

        // Verifica cada posição entre a posição atual e a nova posição
        while (line != newLine || column != newColumn) {
            if (_board.getPiece(new Position(line, column)) != null) {
                return false; // Há uma peça no caminho
            }
            line += lineStep;
            column += columnStep;
        }
        return true;
    }


    ///////////////GETTERS///////////////////////

    public Position getPosition() { return _position; }

    public String getColor() { return _colour; }

    public abstract String getType();

    public Board getBoard() { return _board;}

    ///////////////SETTERS///////////////////////

    public void setPosition(Position newPosition){

        _position = newPosition;
    }

}