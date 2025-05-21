
public class Vehicle {
    private String id;
    private int x;
    private int y;
    private String orientation;
    private int length;

    public Vehicle(String id, int x, int y, String orientation, int length) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.length = length;
    }

    // getters
    public String getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getOrientation() {
        return orientation;
    }

    public int getLength() {
        return length;
    }

    // setters 
    public void setId(String id) {
        this.id = id;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public void setLength(int length) {
        this.length = length;
    }

    // other services
    public String toString() {
        return "Vehicle{" +
                "id='" + id + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", orientation='" + orientation + '\'' +
                ", length=" + length +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;

        Vehicle vehicle = (Vehicle) o;

        if (x != vehicle.x) return false;
        if (y != vehicle.y) return false;
        if (length != vehicle.length) return false;
        if (id != vehicle.id) return false;
        return orientation.equals(vehicle.orientation);
    }

    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + orientation.hashCode();
        result = 31 * result + length;
        return result;
    }

    public Vehicle clone() {
        return new Vehicle(id, x, y, orientation, length);
    }

    public boolean isHorizontal() {
        return orientation.equals("H");
    }

    public boolean isVertical() {
        return orientation.equals("V");
    }

    public void giveColor() {
        String yellow = "\u001B[33m"; // ANSI kode warna kuning
        String reset = "\u001B[0m";   // ANSI kode reset warna
        this.id = yellow + id + reset; // Modifikasi id dengan warna kuning
    }
    
}