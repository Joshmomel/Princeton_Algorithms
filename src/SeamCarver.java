import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.ArrayList;

public class SeamCarver {

  private Picture picture;
  private double[][] energy;

  private double[][] transposeEnergy;
  private int size;

  private int width;

  private int height;

  private boolean hasSetEnergy = false;

  private boolean isInTranspose = false;

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
    if (picture == null) {
      throw new IllegalArgumentException();
    }
    this.picture = new Picture(picture);
    this.width = picture.width();
    this.height = picture.height();
    update();
  }

  private void update() {
    this.energy = new double[this.width][this.height];
    this.transposeEnergy = new double[this.height][this.width];
    this.size = this.width * this.height;
  }

  // current picture
  public Picture picture() {
    return new Picture(this.picture);
  }

  // width of current picture
  public int width() {
    if (isInTranspose) return this.height;
    return this.width;
  }

  // height of current picture
  public int height() {
    if (isInTranspose) return this.width;
    return this.height;
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

  private int getRed(int rgb) {
    return (rgb >> 16) & 0xFF;
  }

  private int getGreen(int rgb) {
    return (rgb >> 8) & 0xFF;
  }

  private int getBlue(int rgb) {
    return (rgb) & 0xFF;
  }


  private int getDxSquare(int x, int y) {
    if (isInTranspose) {
      int temp = x;
      x = y;
      y = temp;
    }

    int leftColor = this.picture.getRGB(x - 1, y);
    int rightColor = this.picture.getRGB(x + 1, y);

    int rx = Math.abs(getRed(leftColor) - getRed(rightColor));
    int gx = Math.abs(getGreen(leftColor) - getGreen(rightColor));
    int bx = Math.abs(getBlue(leftColor) - getBlue(rightColor));

    return rx * rx + gx * gx + bx * bx;
  }

  private int getDySquare(int x, int y) {
    if (isInTranspose) {
      int temp = x;
      x = y;
      y = temp;
    }

    int upColor = this.picture.getRGB(x, y - 1);
    int downColor = this.picture.getRGB(x, y + 1);

    int ry = Math.abs(getRed(upColor) - getRed(downColor));
    int gy = Math.abs(getGreen(upColor) - getGreen(downColor));
    int by = Math.abs(getBlue(upColor) - getBlue(downColor));


    return ry * ry + gy * gy + by * by;
  }

  // energy of pixel at column x and row y
  public double energy(int x, int y) {
    validateColumnIndex(x);
    validateRowIndex(y);

    double energy;
    if (isInTranspose) {
      energy = this.transposeEnergy[x][y];
    } else {
      energy = this.energy[x][y];
    }

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
    for (int y = 0; y < this.height; y++) {
      for (int x = 0; x < this.width; x++) {
        energy(x, y);
        transposeEnergy[y][x] = energy[x][y];
      }
    }

    this.hasSetEnergy = true;
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
    if (!this.hasSetEnergy) {
      setEnergy();
    }

    int w = this.picture.width();
    int h = this.picture.height();

    double[][] tempEnergy = new double[w][h];
    for (int i = 0; i < w; i++) {
      System.arraycopy(this.energy[i], 0, tempEnergy[i], 0, h);
    }

    this.isInTranspose = true;

    this.energy = this.transposeEnergy;
    int[] horizontalSeam = findVerticalSeam();

    this.energy = tempEnergy;
    this.isInTranspose = false;

    return horizontalSeam;
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    if (!this.hasSetEnergy) {
      setEnergy();
    }

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
    this.isInTranspose = true;

    if (seam == null || !isSeamValid(seam)) {
      this.isInTranspose = false;
      throw new IllegalArgumentException();
    }

    removeVerticalSeam(seam);
    transformPicture();
  }

  private void transformPicture() {
    int tw = this.height;
    int th = this.width;
    Picture newPicture = new Picture(tw, th);
    for (int y = 0; y < th; y++) {
      for (int x = 0; x < tw; x++) {
        Color color = this.picture.get(y, x);
        newPicture.set(x, y, color);
      }
    }

    this.picture = newPicture;
    this.width = newPicture.width();
    this.height = newPicture.height();
    update();
    this.isInTranspose = false;
  }

  private boolean isSeamValid(int[] seam) {
    if (seam.length != this.height()) {
      return false;
    }
    int last = -1;
    for (int i : seam) {
      if (i < 0 || i > this.width() - 1) {
        return false;
      }
      if (last != -1) {
        if (last - 1 != i && last + 1 != i && last != i) {
          return false;
        }
      }
      last = i;
    }
    return true;
  }

  // remove vertical seam from current picture
  public void removeVerticalSeam(int[] seam) {
    if (!isInTranspose) {
      if (seam == null || !isSeamValid(seam)) {
        throw new IllegalArgumentException();
      }
    }

    Picture newPicture = new Picture(this.width() - 1, this.height());
    for (int y = 0; y < this.height(); y++) {
      for (int x = 0; x < this.width(); x++) {
        Color color;
        if (isInTranspose) {
          color = this.picture.get(y, x);
        } else {
          color = this.picture.get(x, y);
        }
        if (x < seam[y]) {
          newPicture.set(x, y, color);
        }
        if (x > seam[y]) {
          newPicture.set(x - 1, y, color);
        }
      }
    }
    this.picture = newPicture;
    this.width = newPicture.width();
    this.height = newPicture.height();
    update();
    this.hasSetEnergy = false;
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