import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

  private Item[] queue = (Item[]) new Object[1];
  private int n = 0;

  // construct an empty randomized queue
  public RandomizedQueue() {
  }

  private void resize(int max) {
    Item[] temp = (Item[]) new Object[max];
    if (n >= 0) System.arraycopy(queue, 0, temp, 0, n);
    queue = temp;
  }


  // is the randomized queue empty?
  public boolean isEmpty() {
    return n == 0;
  }

  // return the number of items on the randomized queue
  public int size() {
    return n;
  }

  // add the item
  public void enqueue(Item item) {
    if (item == null) {
      throw new IllegalArgumentException();
    }
    if (n == queue.length) {
      resize(2 * queue.length);
    }
    queue[n++] = item;
  }

  // remove and return a random item
  public Item dequeue() {
    if (n <= 0) {
      throw new NoSuchElementException();
    }
    if (n < queue.length / 4) {
      resize(queue.length / 2);
    }
    int index = StdRandom.uniform(queue.length);
    Item item = queue[index];
    queue[index] = queue[n - 1];
    queue[n - 1] = null;
    n -= 1;
    return item;
  }

  // return a random item (but do not remove it)
  public Item sample() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    return queue[StdRandom.uniform(n)];
  }

  // return an independent iterator over items in random order
  public Iterator<Item> iterator() {
    return new RandomizedQueueIterator();
  }

  private class RandomizedQueueIterator implements Iterator<Item> {

    private int index = 0;

    @Override
    public boolean hasNext() {
      return index < n;
    }

    @Override
    public Item next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      Item item = sample();
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
    RandomizedQueue<Integer> q = new RandomizedQueue<>();
    q.enqueue(1);
    q.enqueue(2);
    q.enqueue(3);
    q.enqueue(4);
    q.enqueue(5);
//    q.dequeue();
//    q.dequeue();
//    q.dequeue();

    for (Integer item : q) {
      System.out.println(item);
    }
  }

}