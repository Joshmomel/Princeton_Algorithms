import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {

  // apply move-to-front encoding, reading from standard input and writing to standard output
  public static void encode() {
    LinkedList<Character> sequence = new LinkedList<>();
    for (int i = 0; i < 256; i++) {
      sequence.add((char) i);
    }

    while (!BinaryStdIn.isEmpty()) {
      char in = BinaryStdIn.readChar(8);
      int i = sequence.indexOf((in));
      BinaryStdOut.write(i, 8);
      Character c = sequence.remove(i);
      sequence.addFirst(c);
    }

    BinaryStdOut.close();
  }

  // apply move-to-front decoding, reading from standard input and writing to standard output
  public static void decode() {
    LinkedList<Character> sequence = new LinkedList<>();
    for (int i = 0; i < 256; i++) {
      sequence.add((char) i);
    }

    while (!BinaryStdIn.isEmpty()) {
      char in = BinaryStdIn.readChar(8);
      Character inChar = sequence.get((int) in);
      BinaryStdOut.write(inChar, 8);
      Character c = sequence.remove(in);
      sequence.addFirst(c);
    }

    BinaryStdOut.close();
  }

  // if args[0] is "-", apply move-to-front encoding
  // if args[0] is "+", apply move-to-front decoding
  public static void main(String[] args) {
    if (args[0].equals("-")) {
      MoveToFront.encode();
    } else if (args[0].equals("+")) {
      MoveToFront.decode();
    }
  }

}