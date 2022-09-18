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

//    for (CircularSuffix circularSuffix : circularSuffixesList) {
//      for (int i = 0; i < s.length(); i++) {
//        System.out.print(circularSuffix.character() + " ");
//        circularSuffix = circularSuffix.next();
//      }
//      System.out.println();
//    }
//    System.out.println();
    circularSuffixesList.sort(CircularSuffix::compareTo);

//    for (CircularSuffix circularSuffix : circularSuffixesList) {
//      for (int i = 0; i < s.length(); i++) {
//        System.out.print(circularSuffix.character() + " ");
//        circularSuffix = circularSuffix.next();
//      }
//      System.out.println();
//    }
//
//    System.out.println();
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
    System.out.println(0 % 6);
//    System.out.println("ABRACADABRA!".charAt(0));
//    System.out.println("ABRACADABRA!".charAt(1));
//    System.out.println("ABRACADABRA!".charAt(5));

    CircularSuffixArray circularSuffixArray = new CircularSuffixArray("ABRACADABRA!");

    for (int i = 0; i < circularSuffixArray.length(); i++) {
      System.out.println(circularSuffixArray.index(i));
    }

  }

}