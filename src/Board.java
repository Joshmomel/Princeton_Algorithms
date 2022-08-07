import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class Board {

  private final int[][] board;

  private final int n;

  private int hamming = 0;

  private int manhattan = 0;

  private int blank;


  private int[][] copyBoard(int[][] board) {
    int size = board.length;

    int[][] copyBoard = new int[size][size];
    for (int i = 0; i < size; i++) {
      System.arraycopy(board[i], 0, copyBoard[i], 0, size);
    }

    return copyBoard;
  }


  private int getNumberRepresentation(int row, int col) {
    if (row == this.n - 1 && col == this.n - 1) {
      return 0;
    }
    return row * this.n + 1 + col;
  }


  private int[] getCorrectRowColByNumber(int number) {
    int[] point = new int[2];
    if (number == 0) {
      point[0] = this.n - 1;
      point[1] = this.n - 1;
    } else {
      point[0] = (number - 1) / this.n;
      point[1] = (number - 1) % this.n;
    }

    return point;
  }

  private int calculateDistance(int number, int row, int col) {
    int[] point = getCorrectRowColByNumber(number);
//    System.out.println("row - point[0] is " + (row - point[0]));
//    System.out.println("col - point[1] is " + (col - point[1]));

    return Math.abs(row - point[0]) + Math.abs(col - point[1]);
  }

  // create a board from an n-by-n array of tiles,
  // where tiles[row][col] = tile at (row, col)
  public Board(int[][] tiles) {
    this.board = copyBoard(tiles);
    this.n = tiles.length;

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        int correctNumber = getNumberRepresentation(i, j);
        if (this.board[i][j] == 0) {
          this.blank = getNumberRepresentation(i, j);
        } else if (this.board[i][j] != correctNumber) {
          this.hamming += 1;
          this.manhattan += calculateDistance(this.board[i][j], i, j);
        }
      }
    }
  }


  // string representation of this board
  public String toString() {
    StringBuilder builder = new StringBuilder();

    builder.append(this.n);
    builder.append("\n");

    for (int i = 0; i < this.n; i++) {
      builder.append(" ");
      for (int j = 0; j < this.n; j++) {
        builder.append(this.board[i][j]);
        builder.append("  ");
      }
      builder.append("\n");
    }

    return builder.toString();
  }

  // board dimension n
  public int dimension() {
    return this.n;
  }

  // number of tiles out of place
  public int hamming() {
    return this.hamming;
  }

  // sum of Manhattan distances between tiles and goal
  public int manhattan() {
    return this.manhattan;
  }

  // is this board the goal board?
  public boolean isGoal() {
    return this.hamming == 0;
  }

  // does this board equal y?
  public boolean equals(Object y) {
    if (y == null) {
      return false;
    }
    if (y == this) {
      return true;
    }
    if (getClass() != y.getClass()) {
      return false;
    }

    Board compareBoard = (Board) y;
    if (this.n != compareBoard.n) {
      return false;
    }

    if (this.hamming() != compareBoard.hamming()) {
      return false;
    }

    if (this.manhattan() != compareBoard.manhattan()) {
      return false;
    }

    for (int i = 0; i < this.n; i++) {
      for (int j = 0; j < this.n; j++) {
        if (this.board[i][j] != compareBoard.board[i][j]) {
          return false;
        }
      }
    }

    return true;
  }

  // all neighboring boards
  public Iterable<Board> neighbors() {
    ArrayList<Board> neighborList = new ArrayList<>();

    int[] blankPoint = getCorrectRowColByNumber(this.blank);
    int blankPointRow = blankPoint[0];
    int blankPointCol = blankPoint[1];

    if (blankPointRow > 0) {
      int[][] neighbor = copyBoard(this.board);
      neighbor[blankPointRow][blankPointCol] = neighbor[blankPointRow - 1][blankPointCol];
      neighbor[blankPointRow - 1][blankPointCol] = 0;

      neighborList.add(new Board(neighbor));
    }

    if (blankPointRow < this.n - 1) {
      int[][] neighbor = copyBoard(this.board);
      neighbor[blankPointRow][blankPointCol] = neighbor[blankPointRow + 1][blankPointCol];
      neighbor[blankPointRow + 1][blankPointCol] = 0;

      neighborList.add(new Board(neighbor));
    }

    if (blankPointCol > 0) {
      int[][] neighbor = copyBoard(this.board);
      neighbor[blankPointRow][blankPointCol] = neighbor[blankPointRow][blankPointCol - 1];
      neighbor[blankPointRow][blankPointCol - 1] = 0;

      neighborList.add(new Board(neighbor));
    }

    if (blankPointCol < this.n - 1) {
      int[][] neighbor = copyBoard(this.board);
      neighbor[blankPointRow][blankPointCol] = neighbor[blankPointRow][blankPointCol + 1];
      neighbor[blankPointRow][blankPointCol + 1] = 0;

      neighborList.add(new Board(neighbor));
    }

    return neighborList;
  }

  // a board that is obtained by exchanging any pair of tiles
  public Board twin() {
    int[][] twin = copyBoard(this.board);
    if (twin[0][0] != 0 && twin[0][1] != 0) {
      twin[0][0] = this.board[0][1];
      twin[0][1] = this.board[0][0];
    } else {
      twin[1][0] = this.board[1][1];
      twin[1][1] = this.board[1][0];
    }

    return new Board(twin);
  }

  // unit testing (not graded)
  public static void main(String[] args) {
// create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);
    System.out.println(initial);
    System.out.println("harming is " + initial.hamming());
    System.out.println("Manhattan is " + initial.manhattan);
    Iterable<Board> neighbors = initial.neighbors();
//    for (Board neighbor : neighbors) {
//      System.out.println(neighbor.toString());
//    }

    Board twin = initial.twin();
    System.out.println(twin.toString());

  }

}