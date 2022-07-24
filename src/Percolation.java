import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private final int top;
  private final int bottom;
  private final int size;
  private int openSites;
  private final WeightedQuickUnionUF uf;
  private final WeightedQuickUnionUF ufTop;
  private final boolean[] grid;


  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int n) {
    if (n < 1) {
      throw new java.lang.IllegalArgumentException();
    }

    size = n;
    uf = new WeightedQuickUnionUF(n * n + 2);
    ufTop = new WeightedQuickUnionUF(n * n + 1);
    top = n * n;
    bottom = n * n + 1;
    openSites = 0;

    grid = new boolean[n * n];

    for (int row = 1; row < n + 1; row++) {
      for (int col = 1; col < n + 1; col++) {
        grid[getUfPosition(row, col)] = false;
        if (row == 1) {
          uf.union(top, getUfPosition(row, col));
          ufTop.union(top, getUfPosition(row, col));
        }
        if (row == n) {
          uf.union(bottom, getUfPosition(row, col));
        }
      }
    }
  }

  private int getUfPosition(int row, int col) {
    if (row < 1 || col < 1 || row > size || col > size) {
      throw new java.lang.IllegalArgumentException();
    }

    int gridRow = row - 1;
    int gridCol = col - 1;

    return gridRow * size + gridCol;
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    if (row < 1 || col < 1 || row > size || col > size) {
      throw new java.lang.IllegalArgumentException();
    }

    int ufPosition = getUfPosition(row, col);

    if (!grid[ufPosition]) {
      grid[ufPosition] = true;
      openSites += 1;
    }

    if (row != 1) {
      if (isOpen(row - 1, col)) {
        uf.union(ufPosition, getUfPosition(row - 1, col));
        ufTop.union(ufPosition, getUfPosition(row - 1, col));
      }
    }
    if (col != 1) {
      if (isOpen(row, col - 1)) {
        uf.union(ufPosition, getUfPosition(row, col - 1));
        ufTop.union(ufPosition, getUfPosition(row, col - 1));
      }
    }
    if (row != size) {
      if (isOpen(row + 1, col)) {
        uf.union(ufPosition, getUfPosition(row + 1, col));
        ufTop.union(ufPosition, getUfPosition(row + 1, col));
      }
    }
    if (col != size) {
      if (isOpen(row, col + 1)) {
        uf.union(ufPosition, getUfPosition(row, col + 1));
        ufTop.union(ufPosition, getUfPosition(row, col + 1));
      }
    }
  }

  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    if (row < 1 || col < 1 || row > size || col > size) {
      throw new java.lang.IllegalArgumentException();
    }
    int ufPosition = getUfPosition(row, col);

    return grid[ufPosition];
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    if (row < 1 || col < 1 || row > size || col > size) {
      throw new java.lang.IllegalArgumentException();
    }

    if (!isOpen(row, col)) {
      return false;
    }

    if (percolates()) {
      return ufTop.find(top) == ufTop.find(getUfPosition(row, col));
    }

    return uf.find(top) == uf.find(getUfPosition(row, col));
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return openSites;
  }

  // does the system percolate?
  public boolean percolates() {
    if (size == 1 && numberOfOpenSites() == 0) {
      return false;
    }
    return uf.find(top) == uf.find(bottom);
  }

  // test client (optional)
  public static void main(String[] args) {
  }
}