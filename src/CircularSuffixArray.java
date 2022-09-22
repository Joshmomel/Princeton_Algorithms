import java.util.ArrayList;

public class CircularSuffixArray {


  private final String s;
  private final ArrayList<CircularSuffix> circularSuffixesList;

  private static class CircularSuffix implements Comparable<CircularSuffix> {
    private final String s;
    private final int length;
    private final int position;

    public CircularSuffix(String s, int position) {
      this.s = s;
      this.length = s.length();
      this.position = position;
    }

    @Override
    public int compareTo(CircularSuffix o) {

      int c1Pointer = this.position;
      int c2Pointer = o.position;

      while (this.s.charAt(c1Pointer) == o.s.charAt(c2Pointer)) {
        c1Pointer = (c1Pointer + 1) % this.length;
        c2Pointer = (c2Pointer + 1) % o.length;
      }

      return this.s.charAt(c1Pointer) - o.s.charAt(c2Pointer);
    }
  }

  // circular suffix array of s
  public CircularSuffixArray(String s) {
    if (s == null) throw new IllegalArgumentException();

    this.s = s;
    this.circularSuffixesList = new ArrayList<>();
    for (int i = 0; i < s.length(); i++) {
      CircularSuffix circularSuffix = new CircularSuffix(s, i);
      circularSuffixesList.add(circularSuffix);
    }

    circularSuffixesList.sort(CircularSuffix::compareTo);
  }

  // length of s
  public int length() {
    return this.s.length();
  }

  // returns index of ith sorted suffix
  public int index(int i) {
    if (i < 0 || i > this.length() - 1) throw new IllegalArgumentException();
    return this.circularSuffixesList.get(i).position;
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