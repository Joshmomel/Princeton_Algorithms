import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

  private Node first;
  private Node last;
  private int n;

  private class Node {
    Item item;
    Node next;
    Node prev;
  }

  // construct an empty deque
  public Deque() {
    n = 0;
    first = new Node();
    last = new Node();
  }

  // is the deque empty?
  public boolean isEmpty() {
    return n == 0;
  }

  // return the number of items on the deque
  public int size() {
    return n;
  }

  // add the item to the front
  public void addFirst(Item item) {
    if (item == null) {
      throw new java.lang.IllegalArgumentException();
    }
    Node node = new Node();
    node.item = item;
    node.next = first;
    node.prev = null;
    if (n == 0) {
      first = node;
      last = node;
    } else {
      first.prev = node;
      first = node;
    }

    n += 1;
  }

  // add the item to the back
  public void addLast(Item item) {
    if (item == null) {
      throw new java.lang.IllegalArgumentException();
    }
    Node node = new Node();
    node.item = item;
    node.prev = last;
    node.next = null;
    if (n == 0) {
      first = node;
    } else {
      last.next = node;
    }
    last = node;
    n += 1;
  }

  // remove and return the item from the front
  public Item removeFirst() {
    if (n == 0) {
      throw new NoSuchElementException();
    }
    Node node = first;
    Item item = node.item;
    if (n == 1) {
      last = null;
      first = null;
      n -= 1;
      return item;
    }

    first = first.next;
    first.prev = null;
    n -= 1;

    return item;
  }

  // remove and return the item from the back
  public Item removeLast() {
    if (n == 0) {
      throw new NoSuchElementException();
    }

    Node oldlast = last;
    Item item = oldlast.item;
    if (n == 1) {
      first = null;
      last = null;
      n -= 1;
      return item;
    }

    last = last.prev;
    last.next = null;
    n -= 1;

    return item;
  }

  // return an iterator over items in order from front to back
  public Iterator<Item> iterator() {
    return new DequeIterator();
  }

  private class DequeIterator implements Iterator<Item> {

    private int index = 0;
    private Node current = first;

    @Override
    public boolean hasNext() {
      return index < n && current != null;
    }

    @Override
    public Item next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      Item item = current.item;
      current = current.next;
      index += 1;
      return item;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }


  // unit testing (required)
  public static void main(String[] args) {
    Deque<Integer> queue = new Deque<>();
    queue.addFirst(1);
    queue.addFirst(2);
    queue.addFirst(3);
    queue.addLast(4);
    queue.addLast(5);
    queue.addLast(6);


    System.out.println(queue.removeLast());
    System.out.println(queue.removeLast());
    System.out.println(queue.removeLast());
    System.out.println(queue.removeFirst());
    System.out.println(queue.removeFirst());
    System.out.println(queue.removeFirst());

    System.out.println("size is " + queue.size());
  }
}