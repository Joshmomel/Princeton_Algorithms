import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {

  private final SET<Point2D> tree = new SET<>();

  // construct an empty set of points
  public PointSET() {
  }

  // is the set empty?
  public boolean isEmpty() {
    return tree.isEmpty();
  }

  // number of points in the set
  public int size() {
    return tree.size();
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    if (p == null) throw new IllegalArgumentException();
    this.tree.add(p);
  }

  // does the set contain point p?
  public boolean contains(Point2D p) {
    if (p == null) throw new IllegalArgumentException();
    return this.tree.contains(p);
  }

  // draw all points to standard draw
  public void draw() {
    for (Point2D point2D : tree) {
      point2D.draw();
    }
  }

  // all points that are inside the rectangle (or on the boundary)
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) throw new IllegalArgumentException();

    ArrayList<Point2D> pointsInRect = new ArrayList<>();
    for (Point2D point2D : tree) {
      if (rect.contains(point2D)) {
        pointsInRect.add(point2D);
      }
    }

    return pointsInRect;
  }

  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D p) {
    if (p == null) throw new IllegalArgumentException();

    Point2D minPoint = null;
    double minDistance = Double.POSITIVE_INFINITY;

    for (Point2D point2D : tree) {
      if (minPoint == null) {
        minPoint = point2D;
        minDistance = minPoint.distanceSquaredTo(p);
        continue;
      }

      var distanceSquaredTo = point2D.distanceSquaredTo(p);
      if (distanceSquaredTo < minDistance) {
        minDistance = distanceSquaredTo;
        minPoint = point2D;
      }
    }

    return minPoint;
  }

  // unit testing of the methods (optional)
  public static void main(String[] args) {
    // initialize the two data structures with point from file
    String filename = args[0];
    In in = new In(filename);
    PointSET brute = new PointSET();
    while (!in.isEmpty()) {
      double x = in.readDouble();
      double y = in.readDouble();
      Point2D p = new Point2D(x, y);
      brute.insert(p);
    }

    Point2D nearest = brute.nearest(new Point2D(0.0, 0.0));
    System.out.println("nearest point is" + nearest);
  }
}