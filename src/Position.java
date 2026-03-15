public class Position {

    private int line;
    private int column;

    public Position(int line, int column) {

        if (line < 0 || line >= 8 || column < 0 || column >= 8) {
            throw new IllegalArgumentException("Position out of bounds.");
        }
        this.line = line;
        this.column = column;
    }

    public int getLine() { return this.line; }
    
    public int getColumn() { return this.column; }

    public Position translate(int lineDirection, int colDirection) {
        
        int newLine = this.line + lineDirection;
        int newColumn = this.column + colDirection;

        if (newLine < 8 && newLine >= 0 && newColumn >= 0 && newColumn < 8) {

            return new Position(newLine, newColumn);
        }
        return null;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return line == position.line && column == position.column;
    }

}