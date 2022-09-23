import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

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
    int first = BinaryStdIn.readInt();
    String s = BinaryStdIn.readString();

    String sortedString = sortString(s);
//    System.out.println("sortedString is " + sortedString);

    int N = s.length();

    int R = 256;
    int[] cs = new int[R + 1];
    for (int i = 0; i < N; i++) {
      cs[s.charAt(i) + 1]++;
    }

    for (int r = 0; r < R; r++) {
      cs[r + 1] += cs[r];
    }

    int[] next = new int[N];
    for (int i = 0; i < N; i++) {
      next[cs[s.charAt(i)]++] = i;
    }

    int p = first;
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