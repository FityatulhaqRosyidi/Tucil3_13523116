
public class Door {
    private String direction;
    private int x;
    private int y;

    public Door(String direction, int x, int y) {
        this.direction = direction;
        this.x = x;
        this.y = y;
    }

    public String getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        return "Door{" +
                "direction='" + direction + '\'' +
                ", X =" + x +
                ", Y =" + x +
                '}';
    }

}
