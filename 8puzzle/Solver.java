import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
  private final Stack<Board> paths = new Stack<>();
  private boolean isSolved = false;


  private static class GameNode {
    private final Board board;
    private final GameNode parent;
    private final int moves;
    private final int priority;

    public GameNode(Board board, int moves, GameNode parent) {
      this.board = board;
      this.moves = moves;
      this.parent = parent;
      this.priority = board.manhattan() + moves;
    }

    public int getPriority() {
      return priority;
    }
  }

  private void getPath(GameNode node) {
    this.paths.push(node.board);
    while (node.parent != null) {
      node = node.parent;
      this.paths.push(node.board);
    }
  }

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    if (initial == null) {
      throw new IllegalArgumentException();
    }

    MinPQ<GameNode> minPQ = new MinPQ<>(Comparator.comparingInt(GameNode::getPriority));
    minPQ.insert(new GameNode(initial, 0, null));

    MinPQ<GameNode> twinMinPQ = new MinPQ<>(Comparator.comparingInt(GameNode::getPriority));
    twinMinPQ.insert(new GameNode(initial.twin(), 0, null));


    while (!minPQ.isEmpty() || !minPQ.isEmpty()) {
      var current = minPQ.delMin();
      var currentTwin = twinMinPQ.delMin();
      if (current.board.isGoal()) {
        this.isSolved = true;
        this.getPath(current);
        return;
      }
      if (currentTwin.board.isGoal()) {
        this.isSolved = false;
        return;
      }

      for (Board neighbor : current.board.neighbors()) {
        if (current.parent == null || !current.parent.board.equals(neighbor)) {
          minPQ.insert(new GameNode(neighbor, current.moves + 1, current));
        }
      }

      for (Board neighbor : currentTwin.board.neighbors()) {
        if (currentTwin.parent == null || !currentTwin.parent.board.equals(neighbor)) {
          twinMinPQ.insert(new GameNode(neighbor, currentTwin.moves + 1, currentTwin));
        }
      }
    }
  }

  // is the initial board solvable? (see below)
  public boolean isSolvable() {
    return this.isSolved;
  }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves() {
    if (!this.isSolved) {
      return -1;
    }

    return this.paths.size() - 1;
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution() {
    if (!this.isSolved) {
      return null;
    }
    return this.paths;
  }

  // test client (see below)
  public static void main(String[] args) {

    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
      StdOut.println("No solution possible");
    else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution())
        StdOut.println(board);

      int moves = solver.moves();
      System.out.println(" moves " + moves);
    }
  }
}