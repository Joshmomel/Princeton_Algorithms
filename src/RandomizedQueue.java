import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

  private static final int INIT_CAPACITY = 1;
  private Item[] queue;
  private int n;

  // construct an empty randomized queue
  public RandomizedQueue() {
    queue = (Item[]) new Object[INIT_CAPACITY];
    n = 0;
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
    int index = StdRandom.uniformInt(n);
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
    return queue[StdRandom.uniformInt(n)];
  }

  // return an independent iterator over items in random order
  public Iterator<Item> iterator() {
    return new RandomizedQueueIterator();
  }

  private class RandomizedQueueIterator implements Iterator<Item> {

    private final Item[] copyQueue;
    private int copySize;

    public RandomizedQueueIterator() {
      copyQueue = (Item[]) new Object[n];
      System.arraycopy(queue, 0, copyQueue, 0, n);
      copySize = n;
    }

    @Override
    public boolean hasNext() {
      return copySize > 0;
    }

    @Override
    public Item next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      int index = StdRandom.uniformInt(copySize);
      Item item = copyQueue[index];
      copyQueue[index] = copyQueue[copySize - 1];
      copyQueue[n - 1] = null;
      copySize -= 1;
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
    q.dequeue();
    q.dequeue();
    q.dequeue();
    Integer dequeue = q.dequeue();
    Integer dequeue1 = q.dequeue();
    System.out.println(dequeue);
    System.out.println(dequeue1);

    System.out.println(StdRandom.uniformInt(1));


    for (Integer item : q) {
      System.out.println(item);
    }
  }

}