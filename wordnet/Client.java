import edu.princeton.cs.algs4.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Client {
  public static void main(String[] args) {
    In in = new In(args[0]);
    Digraph G = new Digraph(in);

    List<Integer> v = Arrays.asList(13, 23, 24);
    List<Integer> w = Arrays.asList(6, 16, 17);


    BreadthFirstDirectedPaths bfsFromV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfsFromW = new BreadthFirstDirectedPaths(G, w);

    HashMap<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < G.V(); i++) {
      if (bfsFromW.hasPathTo(i)) {
        map.put(i, bfsFromW.distTo(i));
      }
    }

    int node = -1;
    int count = -1;
    for (int i = 0; i < G.V(); i++) {
      var find = map.get(i);
      if (find != null && bfsFromV.hasPathTo(i)) {
        var total = find + bfsFromV.distTo(i);
        if (count == -1 || total < count) {
          count = total;
          node = i;
        }
      }
    }


    System.out.println("done");

  }
}
