import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {

  private final Integer[] indexList;

  // circular suffix array of s
  public CircularSuffixArray(String s) {
    if (s == null) throw new IllegalArgumentException("arg can not be null");

    indexList = new Integer[s.length()];
    for (int i = 0; i < s.length(); i++) {
      indexList[i] = i;
    }
    Comparator<Integer> cmp = (a1, a2) -> {
      for (int i = 0; i < s.length(); i++) {
        int i1 = (a1 + i) % s.length();
        int i2 = (a2 + i) % s.length();
        if (s.charAt(i1) == s.charAt(i2))
          continue;
        else
          return Character.compare(s.charAt(i1), s.charAt(i2));
      }
      return 0;
    };

    Arrays.sort(indexList, cmp);
  }

  // length of s
  public int length() {
    return this.indexList.length;
  }

  // returns index of ith sorted suffix
  public int index(int i) {
    if (i < 0 || i > this.length() - 1) throw new IllegalArgumentException();
    return indexList[i];
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