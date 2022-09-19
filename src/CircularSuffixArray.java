import java.util.ArrayList;

public class CircularSuffixArray {


  private final String s;
  private final ArrayList<CircularSuffix> circularSuffixesList;

  private static class CircularSuffix implements Comparable<CircularSuffix> {
    private final String s;
    private final int length;
    private final int first;
    private int position;

    public CircularSuffix(String s, int first) {
      this.s = s;
      this.length = s.length();
      this.first = first;
    }

    public CircularSuffix(String s, int first, int position) {
      this.s = s;
      this.length = s.length();
      this.first = first;
      this.position = position;
    }

    public char character() {
      return s.charAt(first);
    }

    public int getPosition() {
      return position;
    }

    public CircularSuffix next() {
      return new CircularSuffix(this.s, (first + 1) % this.length);
    }

    public CircularSuffix last() {
      return new CircularSuffix(this.s, (first - 1 + this.length) % this.length);
    }


    @Override
    public int compareTo(CircularSuffix o) {
      CircularSuffix c1 = this;
      CircularSuffix c2 = o;

      while (c1.character() == c2.character()) {
        c1 = c1.next();
        c2 = c2.next();
      }
      return c1.character() - c2.character();
    }
  }

  // circular suffix array of s
  public CircularSuffixArray(String s) {
    this.s = s;
    this.circularSuffixesList = new ArrayList<>();
    for (int i = 0; i < s.length(); i++) {
      CircularSuffix circularSuffix = new CircularSuffix(s, i, i);
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
    return this.circularSuffixesList.get(i).getPosition();
  }

  // unit testing (required)
  public static void main(String[] args) {
//    System.out.println((-1 + 12) % 12);
//    System.out.println("ABRACADABRA!".charAt(0));
//    System.out.println("ABRACADABRA!".charAt(1));
//    System.out.println("ABRACADABRA!".charAt(11));

    CircularSuffixArray circularSuffixArray = new CircularSuffixArray("ABRACADABRA!");

    for (int i = 0; i < circularSuffixArray.length(); i++) {
      System.out.print(circularSuffixArray.circularSuffixesList.get(i).last().character() + " ");
      System.out.print("ABRACADABRA!".charAt((circularSuffixArray.index(i) - 1 + 12) % 12) + " ");
      System.out.println(circularSuffixArray.index(i));
    }

  }

}