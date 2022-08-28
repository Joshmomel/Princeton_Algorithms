import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.ArrayList;

public class SeamCarver {

  private final Picture picture;
  private final double[][] energy;
  private final int size;

  private class Point {
    int number;
    int x;
    int y;

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
      this.number = getNumber(x, y);
    }
  }


  // create a seam carver object based on the given picture
  public SeamCarver(Picture picture) {
    this.picture = new Picture(picture);
    this.energy = new double[picture.width()][picture.height()];
    this.size = picture.width() * picture.height();
  }

  // current picture
  public Picture picture() {
    return this.picture;
  }

  // width of current picture
  public int width() {
    return this.picture.width();
  }

  // height of current picture
  public int height() {
    return this.picture.height();
  }

  private void validateRowIndex(int row) {
    if (row < 0 || row >= height())
      throw new IllegalArgumentException("row index must be between 0 and " + (height() - 1) + ": " + row);
  }

  private void validateColumnIndex(int col) {
    if (col < 0 || col >= width())
      throw new IllegalArgumentException("column index must be between 0 and " + (width() - 1) + ": " + col);
  }

  private boolean isBorder(int x, int y) {
    return x == 0 || x == this.width() - 1 || y == 0 || y == this.height() - 1;
  }

  private int getDxSquare(int x, int y) {
    Color leftColor = this.picture.get(x - 1, y);
    Color rightColor = this.picture.get(x + 1, y);

    int rx = Math.abs(leftColor.getRed() - rightColor.getRed());
    int gx = Math.abs(leftColor.getGreen() - rightColor.getGreen());
    int bx = Math.abs(leftColor.getBlue() - rightColor.getBlue());

    return rx * rx + gx * gx + bx * bx;
  }

  private int getDySquare(int x, int y) {
    Color upColor = this.picture.get(x, y - 1);
    Color downColor = this.picture.get(x, y + 1);

    int ry = Math.abs(upColor.getRed() - downColor.getRed());
    int gy = Math.abs(upColor.getGreen() - downColor.getGreen());
    int by = Math.abs(upColor.getBlue() - downColor.getBlue());


    return ry * ry + gy * gy + by * by;
  }

  // energy of pixel at column x and row y
  public double energy(int x, int y) {
    validateColumnIndex(x);
    validateRowIndex(y);

    double energy = this.energy[x][y];
    if (energy != 0.0d) {
      return energy;
    }


    if (isBorder(x, y)) {
      this.energy[x][y] = 1000;
      return 1000;
    }

    double energyPixel = Math.sqrt(getDxSquare(x, y) + getDySquare(x, y));
    this.energy[x][y] = energyPixel;

    return energyPixel;
  }

  private void setEnergy() {
    for (int i = 0; i < this.width(); i++) {
      for (int j = 0; j < this.height(); j++) {
        energy(i, j);
      }
    }
  }

  private int toX(int number) {
    return (number - 1) % this.width();
  }

  private int getNumber(int x, int y) {
    return (this.width() * y + x) + 1;
  }

  private ArrayList<Point> getNextNodes(int x, int y) {
    ArrayList<Point> nextNodes = new ArrayList<>();
    if (y >= this.height() - 1) {
      return nextNodes;
    }

    if (x - 1 >= 0) {
      nextNodes.add(new Point(x - 1, y + 1));
    }
    nextNodes.add(new Point(x, y + 1));

    if (x + 1 < this.width()) {
      nextNodes.add(new Point(x + 1, y + 1));
    }


    return nextNodes;
  }


  // sequence of indices for horizontal seam
  public int[] findHorizontalSeam() {
    int[] horizontalSeam = findVerticalSeam();

    return horizontalSeam;
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    setEnergy();

    int[] edgeTo = new int[this.size + 1];
    double[] distTo = new double[this.size + 1];

    for (int v = 1; v <= this.size; v++) {
      if (v <= this.width()) {
        distTo[v] = 1000;
        edgeTo[v] = 0;
      } else {
        distTo[v] = Double.POSITIVE_INFINITY;
      }
    }

    for (int y = 0; y < this.height(); y++) {
      for (int x = 0; x < this.width(); x++) {
        ArrayList<Point> nextNodes = getNextNodes(x, y);
        int currentPoint = getNumber(x, y);
        for (Point nextNode : nextNodes) {
          if (distTo[nextNode.number] > distTo[currentPoint] + this.energy(nextNode.x, nextNode.y)) {
            distTo[nextNode.number] = distTo[currentPoint] + this.energy(nextNode.x, nextNode.y);
            edgeTo[nextNode.number] = currentPoint;
          }
        }
      }
    }

    double min = distTo[this.size - width() + 1];
    int nodeNumber = this.size - width() + 1;
    for (int i = this.size - width() + 1; i <= this.size; i++) {
      if (distTo[i] < min) {
        min = distTo[i];
        nodeNumber = i;
      }
    }

    int[] minList = new int[this.height()];
    int i = this.height() - 1;
    while (i > -1) {
      minList[i] = toX(nodeNumber);
      nodeNumber = edgeTo[nodeNumber];
      i -= 1;
    }

    return minList;
  }

  // remove horizontal seam from current picture
  public void removeHorizontalSeam(int[] seam) {
  }

  // remove vertical seam from current picture
  public void removeVerticalSeam(int[] seam) {
  }

  //  unit testing (optional)
  public static void main(String[] args) {
    Picture picture = new Picture(args[0]);
    StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());

    SeamCarver sc = new SeamCarver(picture);
    sc.findHorizontalSeam();

    System.out.println("done");

  }
}