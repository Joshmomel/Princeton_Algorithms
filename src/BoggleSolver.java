import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;

import java.io.File;

public class BoggleSolver {

  private final Trie trie;
  private BoggleBoard board;
  private SET<String> words;
  private boolean[][] marked;
  private final int[] drow = {-1, 1, 0, 0, -1, -1, 1, 1};
  private final int[] dcol = {0, 0, -1, 1, -1, 1, -1, 1};
  private int row;
  private int col;


  // Initializes the data structure using the given array of strings as the dictionary.
  // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
  public BoggleSolver(String[] dictionary) {

    trie = new Trie();

    for (String word : dictionary) {
      trie.put(word);
    }
  }

  private boolean outOfBound(int row, int col) {
    return row < 0 || row >= this.row || col < 0 || col >= this.col;
  }


  private void dfs(int row, int col, String string, Trie.Node stringNode) {
    if (string.length() > 2 && stringNode.getVal() != null) {
      words.add(string);
    }

    for (int i = 0; i < 8; i++) {
      int neighbourRow = row + drow[i];
      int neighbourCol = col + dcol[i];
      if (!outOfBound(neighbourRow, neighbourCol) && !marked[neighbourRow][neighbourCol]) {
        String tempWord;
        String letter = Character.toString(board.getLetter(neighbourRow, neighbourCol));

        if (letter.equals("Q")) {
          letter = letter + 'U';
        }
        tempWord = string + letter;

        Trie.Node prefixNode = this.trie.hasPrefix(stringNode, string, letter, string.length());

        if (prefixNode != null) {
          marked[row][col] = true;
          dfs(neighbourRow, neighbourCol, tempWord, prefixNode);
          marked[row][col] = false;
        }
      }
    }
  }

  // Returns the set of all valid words in the given Boggle board, as an Iterable.
  public Iterable<String> getAllValidWords(BoggleBoard board) {

    this.row = board.rows();
    this.col = board.cols();
    this.board = board;

    this.words = new SET<>();

    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        String letter = Character.toString(board.getLetter(i, j));
        if (letter.equals("Q")) letter = letter + "U";

        this.marked = new boolean[row][col];

        Trie.Node stringNode = this.trie.hasPrefix(letter, "");
        dfs(i, j, letter, stringNode);
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

    System.out.println("done");
  }
}
