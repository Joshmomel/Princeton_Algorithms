import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

  private final Picture picture;
  private final double[][] energy;


  // create a seam carver object based on the given picture
  public SeamCarver(Picture picture) {
    this.picture = new Picture(picture);
    this.energy = new double[picture.width()][picture.height()];
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

  // sequence of indices for horizontal seam
  public int[] findHorizontalSeam() {
    return null;
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    return null;
  }

  // remove horizontal seam from current picture
  public void removeHorizontalSeam(int[] seam) {
  }

  // remove vertical seam from current picture
  public void removeVerticalSeam(int[] seam) {
  }

  //  unit testing (optional)
  public static void main(String[] args) {
    System.out.println(Math.sqrt(52024));
  }

}