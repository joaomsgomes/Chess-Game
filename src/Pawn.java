

public class Pawn extends Piece {

    private boolean hasMoved = false;

    public Pawn(String colour, Position position, Board board) {
        super(colour, position, board);
        hasMoved = false;
    }

    public boolean isValidMove(Position newPosition) {
        
        int currentLine = getPosition().getLine();
        int currentColumn = getPosition().getColumn();
        int newLine = newPosition.getLine();
        int newColumn = newPosition.getColumn();

        // Verifica se o movimento está na mesma coluna
        if (newColumn == currentColumn) {
            if (getColor().equals("WHITE")) {
                // Movimento para os peões brancos
                if (!hasMoved && newLine == currentLine - 2) {
                    
                    return true; // Movimento de dois passos apenas na primeira jogada
                } else if (newLine == currentLine - 1) {
                    
                    return true; // Movimento normal de um passo
                }
            } else if (getColor().equals("BLACK")) {
                // Movimento para os peões pretos
                if (!hasMoved && newLine == currentLine + 2) {
                    
                    return true; // Movimento de dois passos apenas na primeira jogada
                } else if (newLine == currentLine + 1) {
                    
                    return true; // Movimento normal de um passo
                }
            }
        }
        return false; 
    }

    public void moved() {
        hasMoved = true;
    }

    public boolean isValidCapture(Position newPosition) {
        
        int currentLine = getPosition().getLine();
        int currentColumn = getPosition().getColumn();
        int newLine = newPosition.getLine();
        int newColumn = newPosition.getColumn();

        int lineDifference = currentLine - newLine;
        int columnDifference = Math.abs(currentColumn - newColumn);
        
        if (getColor().equals("WHITE")) {
            return lineDifference == 1 && columnDifference == 1;
        } else if (getColor().equals("BLACK")) {
            return lineDifference == -1 && columnDifference == 1;
        }
        return false;
    }
    

    public boolean verifyPawnToQueen() {
        
        switch(getColor()) {
            case "WHITE":
                return getPosition().getLine() == 0;
            case "BLACK":
                return getPosition().getLine() == 7;
            default:
                return false;
        }
    }
    public void moveToPosition(Position newPosition) {
        
        if (isValidMove(newPosition)) {
            setPosition(newPosition);
            hasMoved = true; // Atualiza o estado para indicar que o peão já se moveu
        }
    }

    public String getType() { return "Pawn"; }
}