import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {

  private static final double CONFIDENCE_95 = 1.96;
  private final double[] x;
  private final int trials;


  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException("incorrect args");
    }
    int totalSize = n * n;
    this.trials = trials;
    x = new double[trials];


    for (int i = 0; i < trials; i++) {
      Percolation percolation = new Percolation(n);
      while (!percolation.percolates()) {
        int numberToOpen = StdRandom.uniform(totalSize);
        percolation.open((numberToOpen / n) + 1, (numberToOpen % n) + 1);
      }
      x[i] = percolation.numberOfOpenSites() * 1.0 / totalSize;
//      System.out.println("percolation.numberOfOpenSites " + percolation.numberOfOpenSites());
//      System.out.println("x[i] " + x[i]);
    }
  }


  // sample mean of percolation threshold
  public double mean() {
    return StdStats.mean(x);
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return StdStats.stddev(x);
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(trials);
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(trials);
  }

  // test client (see below)
  public static void main(String[] args) {

    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);

    PercolationStats percolationStats = new PercolationStats(n, trials);
    System.out.println("mean                    = " + percolationStats.mean());
    System.out.println("stddev                  = " + percolationStats.stddev());
    System.out.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
  }

}