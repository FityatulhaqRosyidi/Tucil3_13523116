import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Board {
    private int length;
    private int width;
    private ArrayList<Vehicle> vehicles;
    private int numVehicles;
    private String[][] grid;
    private Door door;
    private Vehicle movedVehicle;

    public Board(int length, int width, int numVehicles, ArrayList<Vehicle> vehicles, Door door, Vehicle movedVehicle) {
        this.length = length;
        this.width = width;
        this.vehicles = vehicles;
        this.numVehicles = numVehicles;
        this.grid = new String[length][width];
        this.door = door;
        this.movedVehicle = movedVehicle;

        updateGrid();
    }

    // getters
    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public int getNumVehicles() {
        return numVehicles;
    }

    public String[][] getGrid() {
        return grid;
    }

    public Door getDoor() {
        return door;
    }

    public Vehicle getMovedVehicle() {
        return movedVehicle;
    }
    

    // setters
    public void setLength(int length) {
        this.length = length;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    public void setMovedVehicle(Vehicle movedVehicle) {
        this.movedVehicle = movedVehicle;
    }

    // other services 
    public boolean solved() {
        return this.grid[door.getX()][door.getY()].equals("P");
    }

    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;  // cek referensi sama
        if (obj == null || getClass() != obj.getClass()) return false;

        Board other = (Board) obj;

        // Bandingkan ukuran grid dulu (optional tapi efisien)
        if (this.length != other.length || this.width != other.width) return false;

        // Bandingkan isi grid menggunakan Arrays.deepEquals
        return Arrays.deepEquals(this.grid, other.grid);
    }

    @Override
    public int hashCode() {
        // Gunakan Arrays.deepHashCode untuk array 2D
        int result = Objects.hash(length, width);
        result = 31 * result + Arrays.deepHashCode(grid);
        return result;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        numVehicles++;
        updateGrid();
    }

    public void updateGrid() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                this.grid[i][j] = ".";
            }
        }
        for (Vehicle vehicle : vehicles) {
            int x = vehicle.getX();
            int y = vehicle.getY();
            String orientation = vehicle.getOrientation();
            int length = vehicle.getLength();

            if (orientation.equals("H")) {
                for (int j = 0; j < length; j++) {
                    this.grid[x][y + j] = vehicle.getId();
                }
            } else if (orientation.equals("V")) {
                for (int i = 0; i < length; i++) {
                    this.grid[x + i][y] = vehicle.getId();
                }
            }
        }
    }

    public void displayGrid() {
        System.out.println("-".repeat(length * 2 + 2));
        for (int i = 0; i < length; i++) {
            System.out.print("|");
            for (int j = 0; j < width; j++) {
                String cell = grid[i][j];
                if (movedVehicle != null && cell.equals(movedVehicle.getId())) {
                    System.out.print("\u001B[33m" + cell + "\u001B[0m" + " "); 
                } else {
                    System.out.print(cell + " ");
                }
            }
            System.out.println("|");
        }
        System.out.println("-".repeat(length * 2 + 2));
    }

    public List<Board> getPossibleState() {
        List<Board> states = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            int x = vehicle.getX();
            int y = vehicle.getY();
            String orientation = vehicle.getOrientation();
            int vehicleLength = vehicle.getLength();

            // Try moving the vehicle left or up
            if (orientation.equals("H")) {
                if (y - 1 >= 0 && grid[x][y - 1] == ".") {
                    Vehicle newVehicle = new Vehicle(vehicle.getId(), x, y - 1, orientation, vehicleLength);
                    states.add(createNewBoardState(vehicle, newVehicle));
                }
                if (y + vehicleLength < width && grid[x][y + vehicleLength] == ".") {
                    Vehicle newVehicle = new Vehicle(vehicle.getId(), x, y + 1, orientation, vehicleLength);
                    states.add(createNewBoardState(vehicle, newVehicle));
                }
            } else if (orientation.equals("V")) {
                if (x - 1 >= 0 && grid[x - 1][y] == ".") {
                    Vehicle newVehicle = new Vehicle(vehicle.getId(), x - 1, y, orientation, vehicleLength);
                    states.add(createNewBoardState(vehicle, newVehicle));
                }
                if (x + vehicleLength < length && grid[x + vehicleLength][y] == ".") {
                    Vehicle newVehicle = new Vehicle(vehicle.getId(), x + 1, y, orientation, vehicleLength);
                    states.add(createNewBoardState(vehicle, newVehicle));
                }
            }
        }
        return states;
    }

    private Board createNewBoardState(Vehicle oldVehicle, Vehicle newVehicle) {
        ArrayList<Vehicle> newVehicles = new ArrayList<>(vehicles);
        newVehicles.remove(oldVehicle);
        newVehicles.add(newVehicle);
        return new Board(length, width, newVehicles.size(), newVehicles, door, newVehicle);
    }


    
}
