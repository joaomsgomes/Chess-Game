
public class Player {
    private String color;

    public Player(String color) {
        this.color = color;
    }

    public void switchTurn() {
        color = color.equals("WHITE") ? "BLACK" : "WHITE";
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}