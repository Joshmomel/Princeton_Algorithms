import edu.princeton.cs.algs4.StdIn;

public class Permutation {
  public static void main(String[] args) {
    int k = Integer.parseInt(args[0]);
    RandomizedQueue<String> rqueue = new RandomizedQueue<>();
    while (!StdIn.isEmpty()) {
      rqueue.enqueue(StdIn.readString());
    }
    for (int i = 0; i < k; ++i) {
      System.out.println(rqueue.dequeue());
    }
  }
}