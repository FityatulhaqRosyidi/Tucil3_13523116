import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Loader {
    public static Board loadBoardFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        // Membaca ukuran papan
        String[] dimensions = reader.readLine().split(" ");
        int length = Integer.parseInt(dimensions[0]);
        int width = Integer.parseInt(dimensions[1]);

        // Membaca jumlah kendaraan (tidak perlu digunakan langsung)
        int numVehicles = Integer.parseInt(reader.readLine());

        String[][] grid = new String[length][width];
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        Door door = null;

        // Read grid configuration
        for (int i = 0; i < length; i++) {
            String line = reader.readLine();
            if (line == null) {
                throw new IOException("Unexpected end of file while reading grid.");
            }
            int colIndex = 0;
            for (int j = 0; j < width; j++) {
                String c = String.valueOf(line.charAt(j));
                if (c.equals("K")) {
                    if (i == 0) {
                        i--;
                        break;
                    } else {
                        continue;
                    }
                }
                if (!c.equals(" ")) { // Skip spaces
                    grid[i][colIndex] = c;
                    colIndex++;
                }
            }
        }

        // Menambahkan kendaraan ke papan berdasarkan konfigurasi
        for (char id = 'A'; id <= 'Z'; id++) {
            int xStart = -1, yStart = -1, xEnd = -1, yEnd = -1;

            // Mencari posisi kendaraan
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {
                    if (grid[i][j].equals(String.valueOf(id))) {
                        if (xStart == -1) {
                            xStart = i;
                            yStart = j;
                        }
                        xEnd = i;
                        yEnd = j;
                    }
                }
            }

            // Jika kendaraan ditemukan, tambahkan ke papan
            if (xStart != -1) {
                int vehicleLength = Math.max(xEnd - xStart + 1, yEnd - yStart + 1);
                String orientation = (xStart == xEnd) ? "H" : "V";
                vehicles.add(new Vehicle(String.valueOf(id), xStart, yStart, orientation, vehicleLength));
            }
        }

        reader.close();
        return new Board(length, width, numVehicles, vehicles, door, null) ;
    }

    public static Door loadDoorFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        // Membaca ukuran papan
        String[] dimensions = reader.readLine().split(" ");
        int length = Integer.parseInt(dimensions[0]);
        int width = Integer.parseInt(dimensions[1]);

        // Membaca ukuran papan
        reader.readLine();

        Door door = null;
        int row = 0;

        // Membaca hingga akhir file
        String line;
        while ((line = reader.readLine()) != null) {
            for (int col = 0; col < line.length(); col++) {
                String c = String.valueOf(line.charAt(col));
                if (c.equals("K")) {
                    if (row == 0) {
                        door = new Door("UP", 0, col);
                    } else if (row == length) {
                        door = new Door("DOWN", length - 1, col);
                    } else if (col == 0) {
                        door = new Door("LEFT", row, 0);
                    } else if (col == width) {
                        door = new Door("RIGHT", row, width - 1);
                    }
                    break; // Tidak perlu melanjutkan pencarian setelah pintu ditemukan
                }
            }
            if (door != null) {
                break;
            }
            row++;
        }

        reader.close();

        if (door == null) {
            throw new IOException("No door (K) found in the configuration.");
        }

        return door;
    }
}
