import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class SAP {

  private final Digraph G;

  private final HashMap<VwPair, VwData> db = new HashMap<>();
  private final HashMap<VwIterablePair, VwData> dbIterable = new HashMap<>();

  private static class VwPair {
    int v;
    int w;

    public VwPair(int v, int w) {
      this.v = v;
      this.w = w;
    }


    @Override
    public boolean equals(Object o) {

      if (o == this) {
        return true;
      }

      if (!(o instanceof VwPair)) {
        return false;
      }

      // typecast o to Complex so that we can compare data members
      VwPair c = (VwPair) o;

      return (this.v == c.v && this.w == c.w);
    }

    @Override
    public int hashCode() {
      return Integer.valueOf(v).hashCode() + Integer.valueOf(w).hashCode();
    }

  }

  private static class VwIterablePair {

    Iterable<Integer> iterableV;

    Iterable<Integer> iterableW;


    public VwIterablePair(Iterable<Integer> iterableV, Iterable<Integer> iterableW) {
      this.iterableV = iterableV;
      this.iterableW = iterableW;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      VwIterablePair that = (VwIterablePair) o;
      return this.iterableV.equals(that.iterableV) && this.iterableW.equals(that.iterableW);
    }

    @Override
    public int hashCode() {
      int hashNumber = 0;
      for (Integer i : iterableV) {
        hashNumber += i.hashCode();
      }
      for (Integer i : iterableW) {
        hashNumber += i.hashCode();
      }
      return hashNumber;
    }
  }

  private static class VwData {
    int length;
    int ancestor;

    public VwData(int length, int ancestor) {
      this.length = length;
      this.ancestor = ancestor;
    }
  }

  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph G) {
    if (G == null) {
      throw new IllegalArgumentException();
    }
    this.G = new Digraph(G);
  }


  private VwData getSAP(int v, int w) {
    VwPair vwPair = new VwPair(v, w);
    VwData vwData = db.get(vwPair);
    if (vwData == null) {
      setSAP(v, w);
    }
    return db.get(vwPair);
  }


  private VwData getVwData(BreadthFirstDirectedPaths bfsFromV, BreadthFirstDirectedPaths bfsFromW) {
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
    return new VwData(count, node);
  }

  private void setSAP(int v, int w) {
    BreadthFirstDirectedPaths bfsFromV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfsFromW = new BreadthFirstDirectedPaths(G, w);

    VwData vwData = getVwData(bfsFromV, bfsFromW);

    VwPair vwPair = new VwPair(v, w);
    db.put(vwPair, vwData);
  }

  private void validateVertices(int v) {
    if (v < 0 || v >= G.V()) {
      throw new IllegalArgumentException();
    }
  }

  // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {
    validateVertices(v);
    validateVertices(w);

    VwData vwData = getSAP(v, w);

    return vwData.length;
  }

  // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
  public int ancestor(int v, int w) {
    validateVertices(v);
    validateVertices(w);

    VwData vwData = getSAP(v, w);

    return vwData.ancestor;
  }


  private int validateVertices(Iterable<Integer> vertices) {
    if (vertices == null) {
      throw new IllegalArgumentException();
    }
    int count = 0;
    for (Integer v : vertices) {
      count += 1;
      if (v == null) {
        throw new IllegalArgumentException();
      }
      validateVertices(v);
    }
    if (count == 0) {
      System.out.println("vertices is " + vertices);
      return -1;
    }
    return 1;
  }

  private VwData getSAP(Iterable<Integer> v, Iterable<Integer> w) {
    VwIterablePair vwPair = new VwIterablePair(v, w);
    VwData vwData = dbIterable.get(vwPair);
    if (vwData == null) {
      setSAP(v, w);
    }
    return dbIterable.get(vwPair);
  }


  private void setSAP(Iterable<Integer> v, Iterable<Integer> w) {
    int v1 = validateVertices(v);
    int v2 = validateVertices(w);

    if (v1 < 0 || v2 < 0) {
      VwData vwData = new VwData(-1, -1);
      VwIterablePair vwPair = new VwIterablePair(v, w);
      dbIterable.put(vwPair, vwData);
      return;
    }

    BreadthFirstDirectedPaths bfsFromV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfsFromW = new BreadthFirstDirectedPaths(G, w);

    VwData vwData = getVwData(bfsFromV, bfsFromW);

    VwIterablePair vwPair = new VwIterablePair(v, w);
    dbIterable.put(vwPair, vwData);
  }


  // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    validateVertices(v);
    validateVertices(w);

    VwData vwData = getSAP(v, w);
    return vwData.length;
  }

  // a common ancestor that participates in shortest ancestral path; -1 if no such path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    validateVertices(v);
    validateVertices(w);
    VwData vwData = getSAP(v, w);

    return vwData.ancestor;
  }

  // do unit testing of this class
  public static void main(String[] args) {
    In in = new In(args[0]);
    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);

//    List<Integer> v = Arrays.asList(13, 23, 24);
//    List<Integer> w = Arrays.asList(6, 16, 17);

    int v = 9;
    int w = 12;


    int length = sap.length(v, w);
    int ancestor = sap.ancestor(v, w);
    StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
  }
}
