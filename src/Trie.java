public class Trie {
  private static final int R = 26;        // extended ASCII
  private Node root;      // root of trie

  public static class Node {
    private String val;
    private final Node[] next = new Node[R];

    public String getVal() {
      return val;
    }
  }

  private Node get(Node x, String key, int d) {
    if (x == null) return null;
    if (d == key.length()) return x;
    char c = key.charAt(d);
    return get(x.next[c - 'A'], key, d + 1);
  }

  public String get(String key) {
    Node x = get(root, key, 0);
    if (x == null) return null;
    return x.val;
  }

  public boolean contains(String key) {
    if (key == null) throw new IllegalArgumentException("");
    return get(key) != null;
  }

  private Node put(Node x, String key, int d) {
    if (x == null) x = new Node();
    if (d == key.length()) {
      x.val = key;
      return x;
    }
    char c = key.charAt(d);
    x.next[c - 'A'] = put(x.next[c - 'A'], key, d + 1);
    return x;
  }


  public void put(String key) {
    if (key == null) throw new IllegalArgumentException("");
    root = put(root, key, 0);
  }

  public Node hasPrefix(Node node, String prefix, String next, int d) {
    String newPrefix = prefix + next;
    for (int i = d; i < newPrefix.length(); i++) {
      if (node == null) return null;
      char c = newPrefix.charAt(i);
      node = node.next[c - 'A'];
    }

    return node;
  }

  public Node hasPrefix(String prefix, String next) {
    return hasPrefix(root, prefix, next, 0);
  }


  public static void main(String[] args) {
    Trie trie = new Trie();
    trie.put("HELLO");
    String v = trie.get("HELLO");
    System.out.println("value is " + v);
    boolean isInTrie = trie.contains("HELLO");
    System.out.println("isInTrie " + isInTrie);
    Node hasKey = trie.hasPrefix("HE", "L");
    System.out.println("hasPrefix is " + (hasKey != null));


  }


}
