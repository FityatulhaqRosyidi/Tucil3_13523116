import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class Solver {
    public static int degreeOfFreedom(Vehicle vehicle, Board board, int count) {
        if (count == 0) {
            return 0;
        }

        int degree = 0;
        int currentX = vehicle.getX();
        int currentY = vehicle.getY();
        Vehicle obsVehicle1 = null;
        Vehicle obsVehicle2 = null;
        if (vehicle.isHorizontal()) {
            for (int y = currentY - 1; y >= 0; y--) {
                String cell = board.getGrid()[currentX][y];
                if (cell.equals(".")) {
                    degree++;
                } else {
                    obsVehicle1 = Finder.findVehicleWithId(board.getVehicles(), cell);
                    if (obsVehicle1 != null) {
                        break;
                    }
                }
            }
            for (int y = currentY + vehicle.getLength(); y < board.getWidth(); y++) {
                String cell = board.getGrid()[currentX][y];
                if (cell.equals(".")) {
                    degree++;
                } else {
                    obsVehicle2 = Finder.findVehicleWithId(board.getVehicles(), cell);
                    if (obsVehicle2 != null) {
                        break;
                    }
                }
            }
        } else {
            for (int x = currentX - 1; x >= 0; x++) {
                String cell = board.getGrid()[x][currentY];
                if (cell.equals(".")) {
                    degree++;
                } else {
                    obsVehicle1 = Finder.findVehicleWithId(board.getVehicles(), cell);
                    if (obsVehicle1 != null) {
                        break;
                    }
                }
            }
            for (int x = currentY + vehicle.getLength(); x < board.getLength(); x++) {
                String cell = board.getGrid()[x][currentY];
                if (cell.equals(".")) {
                    degree++;
                } else {
                    obsVehicle2 = Finder.findVehicleWithId(board.getVehicles(), cell);
                    if (obsVehicle2 != null) {
                        break;
                    }
                }
            }
        }
        degree *= count;
        if (obsVehicle1 != null) {
            degree += degreeOfFreedom(obsVehicle1, board, count - 1);
        } 
        if (obsVehicle2 != null) {
            degree += degreeOfFreedom(obsVehicle2, board, count - 1);
        }

        return degree;
    }

    public static int heuristic(Board board, int heuristicChoice) {
        board.displayGrid();

        int totalCost = 0;
        Vehicle primaryVehicle = null;
        for (Vehicle vehicle : board.getVehicles()) {
            if (vehicle.getId().equals("P")) {
                primaryVehicle = vehicle;
            }
        }
        totalCost += degreeOfFreedom(primaryVehicle, board, board.getNumVehicles());

        int distanceFromDoor;
        if (heuristicChoice == 1) {
            distanceFromDoor = manhattanDistance(primaryVehicle.getX(), primaryVehicle.getY(), board.getDoor().getX(), board.getDoor().getY()) - 1;
        } else if (heuristicChoice == 2) {
            distanceFromDoor = euclideanDistance(primaryVehicle.getX(), primaryVehicle.getY(), board.getDoor().getX(), board.getDoor().getY()) - 1;
        } else {
            System.out.println("Invalid heuristic choice");
            return -1;
        }

        if (primaryVehicle.isHorizontal()) {
            totalCost += (board.getWidth() - distanceFromDoor) * 100000;
        } else {
            totalCost += (board.getLength() - distanceFromDoor) * 100000;
        }
            

        if (primaryVehicle.isHorizontal()) {
            if (board.getDoor().getDirection().equals("RIGHT")) {
                for (int i = primaryVehicle.getY() + primaryVehicle.getLength(); i < board.getWidth(); i++) {
                    String cell = board.getGrid()[primaryVehicle.getX()][i];
                    if (cell.equals(".")) {
                        totalCost += 10000;
                    } else {
                        for (Vehicle vehicle : board.getVehicles()) {
                            if (vehicle.getX() == primaryVehicle.getX() && vehicle.getY() == i) {
                                totalCost += degreeOfFreedom(vehicle, board, board.getNumVehicles());
                                
                                break;
                            }
                        }
                    }
                }
            } else if (board.getDoor().getDirection().equals("LEFT")) {
                for (int i = primaryVehicle.getY() - 1; i >= 0; i--) {
                    String cell = board.getGrid()[primaryVehicle.getX()][i];
                    if (cell.equals(".")) {
                        totalCost *= 10;
                    } else {
                        for (Vehicle vehicle : board.getVehicles()) {
                            if (vehicle.getX() == primaryVehicle.getX() && vehicle.getY() == i) {
                                totalCost += degreeOfFreedom(vehicle, board, board.getNumVehicles());
                                
                                break;
                            }
                        }
                    }
                }
            } else {
                System.out.println("Invalid door direction");
            }

        } else {  // vertical
            if (board.getDoor().getDirection().equals("UP")) {
                for (int i = primaryVehicle.getX() - 1; i >= 0; i--) {
                    String cell = board.getGrid()[i][primaryVehicle.getY()];
                    if (cell.equals(".")) {
                        totalCost *= 10;
                    } else {
                        for (Vehicle vehicle : board.getVehicles()) {
                            if (vehicle.getX() == i && vehicle.getY() == primaryVehicle.getY()) {
                                totalCost += degreeOfFreedom(vehicle, board, board.getNumVehicles());
                                
                                
                                break;
                            }
                        }
                    }
                }
            } else if (board.getDoor().getDirection().equals("DOWN")) {
                for (int i = primaryVehicle.getX() + primaryVehicle.getLength(); i < board.getLength(); i++) {
                    String cell = board.getGrid()[i][primaryVehicle.getY()];
                    if (cell.equals(".")) {
                        totalCost *= 10;
                    } else {
                        for (Vehicle vehicle : board.getVehicles()) {
                            if (vehicle.getX() == i && vehicle.getY() == primaryVehicle.getY()) {
                                totalCost += degreeOfFreedom(vehicle, board, board.getNumVehicles());
                                
                                break;
                            }
                        }
                    }
                }
            } else {
                System.out.println("Invalid door direction");
            }
        }

        return totalCost;
    }
    
    public static void solveWithAStar(Board board, int heuristicChoice) {
        System.out.println("Solving with A* algorithm...");

        int h = heuristic(board, heuristicChoice);
        int nodeCount = 0;
        int cost = 0;

        ArrayList<Vehicle> visited = new ArrayList<>();
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.reverseOrder());
        PriorityQueue<State> tempPQ = new PriorityQueue<>(Comparator.reverseOrder());
        pq.add(new State(board, 0 + h));
        while (!pq.isEmpty()) {
            nodeCount++;
            cost ++;

            if (nodeCount > 100) {
                System.out.println("Depth limit reached. No solution found.");
                return;
            }
            
            State currentState = pq.poll();
            Board currentBoard = currentState.getBoard();
            System.out.println("Step " + (nodeCount - 1) + ":");

            currentBoard.displayGrid();
            
            if (currentBoard.solved()) {
                System.out.println("Solution found!");
                System.out.println("Node Visited: " + nodeCount);
                return;
            }
            

            // Generate new states by moving the vehicle
            List<Board> newStates = currentBoard.getPossibleState();
            for (Board newState : newStates) {
                int newH = heuristic(newState, heuristicChoice);
                tempPQ.add(new State(newState, cost + 1 + newH));
            }
            while(!tempPQ.isEmpty()) {
                State tempState = tempPQ.poll();
                if (!visited.contains(tempState.getBoard().getMovedVehicle())) {
                    visited.add(tempState.getBoard().getMovedVehicle());
                    pq.add(tempState);
                    break;
                }
            }
            tempPQ.clear();
        }
    }
    
    public static void solveWithUCS(Board board) {
        System.out.println("Solving with UCS algorithm...");

        int g = 0;
        int nodeCount = 0;

        ArrayList<Vehicle> visited = new ArrayList<>();
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.reverseOrder());
        pq.add(new State(board, g));
        while (!pq.isEmpty()) {
            nodeCount++;
            
            State currentState = pq.poll();
            Board currentBoard = currentState.getBoard();
            int cost = currentState.getCost();
            if (cost > 100) {
                System.out.println("Depth limit reached. No solution found.");
                return;
            }
            System.out.println("Step " + (nodeCount - 1) + ":");
            currentBoard.displayGrid();

            if (currentBoard.solved()) {
                System.out.println("Solution found!");
                return;
            }
            
            if (visited.contains(currentBoard.getMovedVehicle())) {
                continue;
            } else {
                visited.add(currentBoard.getMovedVehicle());
            }

            // Generate new states by moving the vehicle
            List<Board> newStates = currentBoard.getPossibleState();
            for (Board newState : newStates) {
                pq.add(new State(newState, cost + 1));
            }
        }
    }

    public static void solveWithGreedyBFS(Board board, int heuristicChoice) {
        System.out.println("Solving with Greedy Best-First Search algorithm...");

        int h = heuristic(board, heuristicChoice);
        int depth = 0;
        int nodeCount = 0;

        ArrayList<Vehicle> visited = new ArrayList<>();
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.reverseOrder());
        PriorityQueue<State> tempPQ = new PriorityQueue<>(Comparator.reverseOrder());
        pq.add(new State(board, h));
        while (!pq.isEmpty()) {
            nodeCount++;

            if (depth > 100) {
                System.out.println("Depth limit reached. No solution found.");
                return;
            }
            
            State currentState = pq.poll();
            Board currentBoard = currentState.getBoard();
            System.out.println("Step " + (depth - 1) + ":");
            currentBoard.displayGrid();

            if (currentBoard.solved()) {
                System.out.println("Solution found!");
                return;
            }
            

            depth++;

            // Generate new states by moving the vehicle
            List<Board> newStates = currentBoard.getPossibleState();
            for (Board newState : newStates) {
                int newH = heuristic(newState, heuristicChoice);
                tempPQ.add(new State(newState, newH));
            }
            while(!tempPQ.isEmpty()) {
                State tempState = tempPQ.poll();
                if (!visited.contains(tempState.getBoard().getMovedVehicle())) {
                    visited.add(tempState.getBoard().getMovedVehicle());
                    pq.add(tempState);
                    break;
                }
            }
            tempPQ.clear();
        }
    }

    public static int manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public static int euclideanDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }


}

