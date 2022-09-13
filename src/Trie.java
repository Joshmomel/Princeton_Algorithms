public class Trie {
  private static final int R = 26;        // extended ASCII
  private Node root;      // root of trie

  private static class Node {
    private String val;
    private final Node[] next = new Node[R];
  }

  private Node get(Node x, String key, int d) {
    if (x == null) return null;
    if (d == key.length()) return x;
    char c = key.charAt(d);
    return get(x.next[c - 65], key, d + 1);
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
    x.next[c - 65] = put(x.next[c - 65], key, d + 1);
    return x;
  }


  public void put(String key) {
    if (key == null) throw new IllegalArgumentException("");
    root = put(root, key, 0);
  }

  private Node hasPrefix(Node x, String prefix, int d) {
    if (x == null) return null;
    if (d == prefix.length()) return x;
    char c = prefix.charAt(d);
    return hasPrefix(x.next[c - 65], prefix, d + 1);
  }

  public boolean hasPrefix(String prefix) {
    Node x = hasPrefix(root, prefix, 0);
    return x != null;
  }



  public static void main(String[] args) {
    Trie trie = new Trie();
    trie.put("HELLO");
    String v = trie.get("HELLO");
    System.out.println("value is " + v);
    boolean isInTrie = trie.contains("HELLO");
    System.out.println("isInTrie " + isInTrie);
    boolean hasKey = trie.hasPrefix("HE");
    System.out.println("hasPrefix is " + hasKey);


  }


}
