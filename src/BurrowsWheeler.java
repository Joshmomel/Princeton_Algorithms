import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

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