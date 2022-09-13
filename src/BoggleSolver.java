import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BoggleSolver {

  private final Trie trie;
  private int row;
  private int col;


  //Point represent 4 * 4 board
  private static class Point {
    int row;
    int col;

    public Point(int row, int col) {
      this.row = row;
      this.col = col;
    }
  }

  // Initializes the data structure using the given array of strings as the dictionary.
  // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
  public BoggleSolver(String[] dictionary) {

    trie = new Trie();

    for (String word : dictionary) {
      trie.put(word);
    }
  }

  private boolean isValidPoint(int row, int col) {
    if (row < 0) return false;
    if (row > this.row - 1) return false;
    if (col < 0) return false;
    if (col > this.col - 1) return false;

    return true;
  }

  private List<Point> getNeighbour(int row, int col) {
    ArrayList<Point> neighbours = new ArrayList<>();
    if (isValidPoint(row - 1, col)) {
      neighbours.add(new Point(row - 1, col));
    }
    if (isValidPoint(row + 1, col)) {
      neighbours.add(new Point(row + 1, col));
    }
    if (isValidPoint(row, col - 1)) {
      neighbours.add(new Point(row, col - 1));
    }
    if (isValidPoint(row, col + 1)) {
      neighbours.add(new Point(row, col + 1));
    }
    if (isValidPoint(row - 1, col - 1)) {
      neighbours.add(new Point(row - 1, col - 1));
    }
    if (isValidPoint(row - 1, col + 1)) {
      neighbours.add(new Point(row - 1, col + 1));
    }
    if (isValidPoint(row + 1, col - 1)) {
      neighbours.add(new Point(row + 1, col - 1));
    }
    if (isValidPoint(row + 1, col + 1)) {
      neighbours.add(new Point(row + 1, col + 1));
    }

    return neighbours;
  }


  private void dfs(BoggleBoard board, Point point, boolean[][] marked, String string, SET<String> words) {
    for (Point neighbour : getNeighbour(point.row, point.col)) {
      if (!marked[neighbour.row][neighbour.col]) {
        char letter = board.getLetter(neighbour.row, neighbour.col);
        String tempWord;

        if (letter == 'Q') {
          tempWord = string + letter + 'U';
        } else {
          tempWord = string + letter;
        }
        boolean hasKeys = this.trie.hasPrefix(tempWord);

        if (hasKeys) {
          if (this.trie.contains(tempWord) && tempWord.length() > 2) {
            words.add(tempWord);
          }

          boolean[][] markedCopy = new boolean[this.row][this.col];
          for (int i = 0; i < marked.length; i++) {
            System.arraycopy(marked[i], 0, markedCopy[i], 0, marked[0].length);
          }
          markedCopy[point.row][point.col] = true;
          dfs(board, neighbour, markedCopy, tempWord, words);
        }
      }
    }

  }

  // Returns the set of all valid words in the given Boggle board, as an Iterable.
  public Iterable<String> getAllValidWords(BoggleBoard board) {

    this.row = board.rows();
    this.col = board.cols();

    SET<String> words = new SET<>();

    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        String string;
        char letter = board.getLetter(i, j);
        if (letter == 'Q') {
          string = letter + "U";
        } else {
          string = letter + "";
        }

        boolean[][] marked = new boolean[row][col];
        marked[i][j] = true;


        dfs(board, new Point(i, j), marked, string, words);
      }
    }

    return words;
  }

  // Returns the score of the given word if it is in the dictionary, zero otherwise.
  // (You can assume the word contains only the uppercase letters A through Z.)
  public int scoreOf(String word) {
    if (word == null) throw new IllegalArgumentException();
    if (!this.trie.contains(word)) {
      return 0;
    }

    int length = word.length();
    if (length == 3 || length == 4) return 1;
    if (length == 5) return 2;
    if (length == 6) return 3;
    if (length == 7) return 5;
    if (length >= 8) return 11;

    return 0;
  }

  public static void main(String[] args) {
    In in = new In(new File("src/utils/dictionary-yawl.txt"));
    String[] dictionary = in.readAllStrings();

    // create the Boggle solver with the given dictionary
    BoggleSolver solver = new BoggleSolver(dictionary);

    BoggleBoard boggleBoard = new BoggleBoard("src/utils/board-antidisestablishmentarianisms.txt");
    System.out.println(boggleBoard);

    System.out.println("solver boggleBoard ");

    Iterable<String> validWords = solver.getAllValidWords(boggleBoard);
    System.out.println(validWords);
    int score = 0;
    for (String validWord : validWords) {
      score += solver.scoreOf(validWord);
    }
    System.out.println("score is " + score);

    System.out.println("score of EM " + solver.scoreOf("EM"));


    System.out.println("done");
  }
}
