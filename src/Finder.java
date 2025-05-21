import java.util.ArrayList;

public class Finder {

    public static Vehicle findVehicleWithId(ArrayList<Vehicle> vehicles, String id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId() == id) {
                return vehicle; 
            }
        }
        return null; 
    }
}
