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
    if (n == 0) {
      Node n = new Node();
      n.item = item;
      first = n;
      last = n;
    } else {
      Node oldfirst = first;
      first = new Node();
      first.item = item;
      first.next = oldfirst;
      oldfirst.prev = first;
    }

    n += 1;
  }

  // add the item to the back
  public void addLast(Item item) {
    if (item == null) {
      throw new java.lang.IllegalArgumentException();
    }
    if (n == 0) {
      Node n = new Node();
      n.item = item;
      first = n;
      last = n;
    } else {
      Node oldlast = last;
      last = new Node();
      last.item = item;
      last.prev = oldlast;
      oldlast.next = last;
    }

    n += 1;
  }

  // remove and return the item from the front
  public Item removeFirst() {
    if (n == 0) {
      throw new java.util.NoSuchElementException();
    }
    if (n == 1) {
      Item item = first.item;
      first.item = null;
      first.next = null;
      last.item = null;
      last.prev = null;
      n -= 1;
      return item;
    }

    Node oldfirst = first;
    first = first.next;
    first.prev = null;
    oldfirst.next = null;
    n -= 1;

    return oldfirst.item;
  }

  // remove and return the item from the back
  public Item removeLast() {
    if (n == 0) {
      throw new java.util.NoSuchElementException();
    }
    if (n == 1) {
      Item item = last.item;
//      first.item = null;
      first.next = null;
//      last.item = null;
      last.prev = null;
      n -= 1;
      return item;
    }

    Node oldlast = last;
    last = last.prev;
    last.next = null;
    oldlast.prev = null;
    n -= 1;

    return oldlast.item;
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
      Item item = current.item;
      if (item == null) throw new NoSuchElementException();
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

    for (Integer q : queue) {
      System.out.println("queue is " + q);
    }

    System.out.println(queue.removeLast());
    System.out.println(queue.removeLast());
    System.out.println(queue.removeLast());
    System.out.println(queue.removeFirst());
    System.out.println(queue.removeFirst());
    System.out.println(queue.removeFirst());


    System.out.println("size is " + queue.size());
  }
}