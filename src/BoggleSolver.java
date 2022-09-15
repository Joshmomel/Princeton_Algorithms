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


  private List<Point> getNeighbour(int row, int col) {
    ArrayList<Point> neighbours = new ArrayList<>();
    if (row - 1 >= 0) {
      neighbours.add(new Point(row - 1, col));
    }
    if (row + 1 < this.row) {
      neighbours.add(new Point(row + 1, col));
    }
    if (col - 1 >= 0) {
      neighbours.add(new Point(row, col - 1));
    }
    if (col + 1 < this.col) {
      neighbours.add(new Point(row, col + 1));
    }
    if (row - 1 >= 0 && col - 1 >= 0) {
      neighbours.add(new Point(row - 1, col - 1));
    }
    if (row - 1 >= 0 && col + 1 < this.col) {
      neighbours.add(new Point(row - 1, col + 1));
    }
    if (row + 1 < this.row && col - 1 >= 0) {
      neighbours.add(new Point(row + 1, col - 1));
    }
    if (row + 1 < this.row && col + 1 < this.col) {
      neighbours.add(new Point(row + 1, col + 1));
    }

    return neighbours;
  }


  private void dfs(BoggleBoard board, Point point, boolean[][] marked, String string, SET<String> words, Trie.Node stringNode) {
    for (Point neighbour : getNeighbour(point.row, point.col)) {
      if (!marked[neighbour.row][neighbour.col]) {
        String tempWord;
        String letter = Character.toString(board.getLetter(neighbour.row, neighbour.col));

        if (letter.equals("Q")) {
          letter = letter + 'U';
        }
        tempWord = string + letter;

        Trie.Node prefixNode = this.trie.hasPrefix(stringNode, string, letter, string.length());

        if (prefixNode != null) {
          if (tempWord.length() > 2 && prefixNode.getVal() != null) {
            words.add(tempWord);
          }

          marked[point.row][point.col] = true;
          dfs(board, neighbour, marked, tempWord, words, prefixNode);
        }
      }
    }
    marked[point.row][point.col] = false;

  }

  // Returns the set of all valid words in the given Boggle board, as an Iterable.
  public Iterable<String> getAllValidWords(BoggleBoard board) {

    this.row = board.rows();
    this.col = board.cols();

    SET<String> words = new SET<>();

    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        String letter = Character.toString(board.getLetter(i, j));
        if (letter.equals("Q")) letter = letter + "U";

        boolean[][] marked = new boolean[row][col];
        marked[i][j] = true;

        Trie.Node stringNode = this.trie.hasPrefix(letter, "");
        dfs(board, new Point(i, j), marked, letter, words, stringNode);
      }
    }

    return words;
  }

  // Returns the score of the given word if it is in the dictionary, zero otherwise.
  // (You can assume the word contains only the uppercase letters A through Z.)
  public int scoreOf(String word) {
    if (word == null) throw new IllegalArgumentException();

    int length = word.length();
    if (length <= 2 || !this.trie.contains(word)) {
      return 0;
    }

    if (length == 3 || length == 4) return 1;
    if (length == 5) return 2;
    if (length == 6) return 3;
    if (length == 7) return 5;
    return 11;

  }

  public static void main(String[] args) {
    In in = new In(new File("src/utils/dictionary-yawl.txt"));
    String[] dictionary = in.readAllStrings();

    // create the Boggle solver with the given dictionary
    BoggleSolver solver = new BoggleSolver(dictionary);

    BoggleBoard boggleBoard = new BoggleBoard("src/utils/board-points26539.txt");
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
