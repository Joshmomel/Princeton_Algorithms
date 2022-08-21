import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordNet {

  private int v = 0;

  private final Digraph G;

  private final SAP sap;

  private final Map<String, List<Integer>> nounMap = new HashMap<>();
  private final Map<Integer, String> idMap = new HashMap<>();

  // constructor takes the name of the two input files
  public WordNet(String synsets, String hypernyms) {
    readSynsets(synsets);

    G = new Digraph(this.v);

    buildDigraph(hypernyms);

    if (!isRootedDAG()) {
      throw new IllegalArgumentException();
    }

    this.sap = new SAP(this.G);
  }


  private void readSynsets(String synsets) {
    In in = new In(synsets);
    while (!in.isEmpty()) {
      String s = in.readLine();
      var fields = s.split(",");
      var nouns = fields[1].split(" ");
      int id = Integer.parseInt(fields[0]);

      List<String> nounList = new ArrayList<>();
      Collections.addAll(nounList, nouns);

      for (String noun : nounList) {
        List<Integer> idList = nounMap.get(noun);
        if (idList == null) {
          ArrayList<Integer> ids = new ArrayList<>();
          ids.add(id);
          nounMap.put(noun, ids);
        } else {
          idList.add(id);
        }
      }
      idMap.put(Integer.valueOf(fields[0]), fields[1]);
      v += 1;
    }
  }

  private boolean isRootedDAG() {
    int count = 0;
    for (int i = 0; i < G.V(); i++) {
      if (G.outdegree(i) == 0)
        count++;
    }
    return count == 1;
  }


  private void buildDigraph(String hypernyms) {
    In in = new In(hypernyms);
    while (!in.isEmpty()) {
      String s = in.readLine();
      var fields = s.split(",");
      for (int i = 1; i < fields.length; i++) {
        G.addEdge(Integer.parseInt(fields[0]), Integer.parseInt(fields[i]));
      }
    }
  }


  // returns all WordNet nouns
  public Iterable<String> nouns() {
    return nounMap.keySet();
  }

  // is the word a WordNet noun?
  public boolean isNoun(String word) {
    if (word == null) {
      throw new IllegalArgumentException();
    }
    return nounMap.get(word) != null;
  }

  // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) {
    if (!isNoun(nounA) || !isNoun(nounB)) {
      throw new IllegalArgumentException();
    }

    return this.sap.length(nounMap.get(nounA), nounMap.get(nounB));
  }

  // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
  // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
    if (!isNoun(nounA) || !isNoun(nounB)) {
      throw new IllegalArgumentException();
    }

    int id = this.sap.ancestor(nounMap.get(nounA), nounMap.get(nounB));

    return idMap.get(id);
  }

  // do unit testing of this class
  public static void main(String[] args) {
    WordNet wordNet = new WordNet("util/synsets.txt", "util/hypernyms.txt");

    System.out.println("v is " + wordNet.v);
    System.out.println("is noun " + wordNet.isNoun("worm"));
    System.out.println("is noun " + wordNet.isNoun("bird"));


    int distance1 = wordNet.distance("white_marlin", "mileage");
    int distance2 = wordNet.distance("Black_Plague", "black_marlin");
    int distance3 = wordNet.distance("American_water_spaniel", "histology");
    int distance4 = wordNet.distance("Brown_Swiss", "barrel_roll");

    System.out.println("dist is " + distance1);
    System.out.println("dist is " + distance2);
    System.out.println("dist is " + distance3);
    System.out.println("dist is " + distance4);


    System.out.println("done");

  }
}