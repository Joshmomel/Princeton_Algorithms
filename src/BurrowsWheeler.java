import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.SET;

import java.util.Arrays;
import java.util.TreeMap;

public class BurrowsWheeler {


  private static String sortString(String inputString) {
    // Converting input string to character array
    char[] tempArray = inputString.toCharArray();

    // Sorting temp array using
    Arrays.sort(tempArray);

    // Returning new sorted string
    return new String(tempArray);
  }

  // apply Burrows-Wheeler transform,
  // reading from standard input and writing to standard output
  public static void transform() {
    String s = "";
    while (!BinaryStdIn.isEmpty()) {
      s = BinaryStdIn.readString();
    }

    int stringLength = s.length();
    CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);

    int startIndex = -1;
    for (int i = 0; i < circularSuffixArray.length(); i++) {
      if (circularSuffixArray.index(i) == 0) {
        startIndex = i;
        break;
      }
    }
    BinaryStdOut.write(startIndex);

    for (int i = 0; i < circularSuffixArray.length(); i++) {
      BinaryStdOut.write(s.charAt((circularSuffixArray.index(i) - 1 + stringLength) % stringLength));
    }

    BinaryStdOut.close();
  }

  // apply Burrows-Wheeler inverse transform,
  // reading from standard input and writing to standard output
  public static void inverseTransform() {
    String s = "";
    BinaryStdIn.readInt();

    while (!BinaryStdIn.isEmpty()) {
      s = BinaryStdIn.readString();
    }

    String sortedString = sortString(s);

    int N = s.length();
    SET<Character> uniqueChars = new SET<>();
    for (int i = 0; i < N; i++) {
      uniqueChars.add(sortedString.charAt(i));
    }

    TreeMap<Character, Integer> count = new TreeMap<>();
    int num = 0;
    for (int i = 0; i < N; i++) {
      char c = sortedString.charAt(i);
      count.putIfAbsent(c, num);
      num += 1;
    }

    int[] next = new int[N];
    Arrays.fill(next, -1);

    for (int i = 0; i < N; i++) {
      char c = s.charAt(i);
      int position = count.get(c);
      while (next[position] != -1) {
        position += 1;
      }
      next[position] = i;
    }

    int p = next[0];
    for (int i = 0; i < N; i++) {
      BinaryStdOut.write(sortedString.charAt(p));
      p = next[p];
    }


    BinaryStdOut.close();
  }

  // if args[0] is "-", apply Burrows-Wheeler transform
  // if args[0] is "+", apply Burrows-Wheeler inverse transform
  public static void main(String[] args) {
    if (args[0].equals("-")) {
      BurrowsWheeler.transform();
    } else if (args[0].equals("+")) {
      BurrowsWheeler.inverseTransform();
    }
  }

}