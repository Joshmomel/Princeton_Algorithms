import java.util.Arrays;

public class CircularSuffixArray {


  private final String s;
  private final CircularSuffix[] circularSuffixes;

  private static class CircularSuffix implements Comparable<CircularSuffix> {
    private final String s;
    private final int position;

    public CircularSuffix(String s, int position) {
      this.s = s;
      this.position = position;
    }

    public char charAt(int index) {
      return s.charAt((position + index) % s.length());
    }

    @Override
    public int compareTo(CircularSuffix that) {
      if (this == that) {
        return 0;
      } else {
        for (int i = 0; i < s.length(); i++) {
          if (this.charAt(i) < that.charAt(i)) {
            return -1;
          } else if (this.charAt(i) > that.charAt(i)) {
            return 1;
          }
        }
      }
      return 0;
    }
  }

  // circular suffix array of s
  public CircularSuffixArray(String s) {
    if (s == null) throw new IllegalArgumentException();

    this.s = s;
    this.circularSuffixes = new CircularSuffix[s.length()];

    for (int i = 0; i < s.length(); i++) {
      CircularSuffix circularSuffix = new CircularSuffix(s, i);
      circularSuffixes[i] = circularSuffix;
    }

    Arrays.sort(circularSuffixes);
  }

  // length of s
  public int length() {
    return this.s.length();
  }

  // returns index of ith sorted suffix
  public int index(int i) {
    if (i < 0 || i > this.length() - 1) throw new IllegalArgumentException();
    return circularSuffixes[i].position;
  }

  // unit testing (required)
  public static void main(String[] args) {
    CircularSuffixArray circularSuffixArray = new CircularSuffixArray("ABRACADABRA!");

    for (int i = 0; i < circularSuffixArray.length(); i++) {
      System.out.print("ABRACADABRA!".charAt((circularSuffixArray.index(i) - 1 + 12) % 12) + " ");
      System.out.println(circularSuffixArray.index(i));
    }

  }

}