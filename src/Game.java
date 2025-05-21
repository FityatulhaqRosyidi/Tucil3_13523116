import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Game {
    public static void run() throws Exception {
        while (true) {

            try {

                System.out.println("Welcome to the RushHour Solver!");
                System.out.println("Input path to the file containing the RushHour puzzle (e.g. p1.txt):");
                Scanner scanner = new Scanner(System.in);
                String filePath = scanner.nextLine();
                
                Board board = Loader.loadBoardFromFile("../test/" + filePath);
                Door door = Loader.loadDoorFromFile("../test/" + filePath);
                board.setDoor(door);
                
                System.out.println("Input Algorithm:");
                System.out.println("1. A star");
                System.out.println("2. UCS");
                System.out.println("3. Greedy BFS");
                int choice = scanner.nextInt();
    
                System.out.println("Input Heuristic:");
                System.out.println("1. Manhattan Distance");
                System.out.println("2. Euclidean Distance");
                int heuristicChoice = scanner.nextInt();
    
                switch (choice) {
                    case 1:
                        Solver.solveWithAStar(board, heuristicChoice);
                        break;
                    case 2:
                        Solver.solveWithUCS(board);
                        break;
                    case 3:
                        Solver.solveWithGreedyBFS(board, heuristicChoice);
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a valid algorithm.");
                }
    
                System.out.println("Do you want to solve another puzzle? (0.no/ any.yes)");
                int continueChoice = scanner.nextInt();
                if (continueChoice == 0) {
                    System.out.println("Thank you for playing!");
                    scanner.close();
                    break;
                } 

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
                System.out.println("Please try again.");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid index: " + e.getMessage());
                System.out.println("Please check the input file.");
            } catch (ClassCastException e) {
                System.out.println("Invalid class cast: " + e.getMessage());
                System.out.println("Please check the input file.");
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + e.getMessage());
                System.out.println("Please check the file path.");
            } catch (IOException e) {
                System.out.println("IO exception: " + e.getMessage());
                System.out.println("Please check the input file.");
            } catch (NullPointerException e) {
                System.out.println("Null pointer exception : " + e.getMessage());
                System.out.println("Please check the input file.");
            } catch (Exception e) {
                System.out.println("exception: " + e);
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            } 
            
        }

    
    }
}
