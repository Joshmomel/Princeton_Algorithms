import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class Solver {

  private final Stack<Board> paths = new Stack<>();
  private boolean isSolved = false;


  private static class GameNode {
    private final Board board;
    private final GameNode parent;
    private final int moves;


    public GameNode(Board board, int moves, GameNode parent) {
      this.board = board;
      this.moves = moves;
      this.parent = parent;
    }

    public Board getBoard() {
      return board;
    }

    public int getMoves() {
      return moves;
    }

    public int getPriority() {
      return this.board.manhattan() + this.moves;
    }

    public GameNode getParent() {
      return parent;
    }
  }

  private void getPath(GameNode node) {
    this.paths.push(node.board);
    while (node.getParent() != null) {
//      System.out.println("priority " + node.getPriority());
//      System.out.println("moves " + node.getMoves());
//      System.out.println("manhattan " + node.getBoard().manhattan());
//      System.out.println();
      node = node.parent;
      this.paths.push(node.board);
    }
  }

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    MinPQ<GameNode> minPQ = new MinPQ<>(Comparator.comparingInt(GameNode::getPriority));
    minPQ.insert(new GameNode(initial, 0, null));

    ArrayList<Board> previousBoards = new ArrayList<>();

    int step = 0;
    while (!minPQ.isEmpty()) {
      var current = minPQ.min();
      if (current.getBoard().isGoal()) {
        this.isSolved = true;
        this.getPath(current);
        return;
      }

      minPQ.delMin();
      previousBoards.add(current.getBoard());

//      System.out.println("queue is " + minPQ.size());
//      System.out.println("start step " + step);
//      if (step == 100) {
//        System.out.println();
//      }
//      System.out.println("neighbor is " + current.getBoard().toString());
//      System.out.println("movie is " + current.getMoves() + " manhattan is " + current.getBoard().manhattan() + " priority is " + (current.getMoves() + current.getBoard().manhattan()));
//      System.out.println();
//      System.out.println();

      for (Board neighbor : current.getBoard().neighbors()) {
        step += 1;

        if (previousBoards.contains(neighbor)) {
          continue;
        }


        GameNode node = new GameNode(neighbor, current.getMoves() + 1, current);
        minPQ.insert(node);
      }
    }
  }

  // is the initial board solvable? (see below)
  public boolean isSolvable() {
    return this.isSolved;
  }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves() {
    return this.paths.size() - 1;
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution() {
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