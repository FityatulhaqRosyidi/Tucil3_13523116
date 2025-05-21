public class State implements Comparable<State> {
    private Board board;
    private int cost;

    public State(Board board, int cost) {
        this.board = board;
        this.cost = cost;
    }

    @Override
    public int compareTo(State other) {
        return Integer.compare(this.cost, other.cost);
    }

    // getters
    public Board getBoard() {
        return board;
    }

    public int getCost() {
        return cost;
    }

    // setters
    public void setBoard(Board board) {
        this.board = board;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
