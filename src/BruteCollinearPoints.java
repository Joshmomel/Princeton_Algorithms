import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {

  private int n = 0;
  private final LineSegment[] segments;

  private Point findMax(Point p1, Point p2, Point p3, Point p4) {
    Point max = p1;

    if (p2.compareTo(max) > 0) max = p2;
    if (p3.compareTo(max) > 0) max = p3;
    if (p4.compareTo(max) > 0) max = p4;

    return max;
  }

  private Point findMin(Point p1, Point p2, Point p3, Point p4) {
    Point min = p1;

    if (p2.compareTo(min) < 0) min = p2;
    if (p3.compareTo(min) < 0) min = p3;
    if (p4.compareTo(min) < 0) min = p4;

    return min;
  }

  // finds all line segments containing 4 points
  public BruteCollinearPoints(Point[] points) {
    if (points == null) {
      throw new IllegalArgumentException();
    }


    segments = new LineSegment[points.length - 1];

    int pointLength = points.length;
    for (int i = 0; i < pointLength; i++) {
      var p1 = points[i];
      if (p1 == null) {
        throw new IllegalArgumentException();
      }

      for (int j = i + 1; j < pointLength; j++) {
        var p2 = points[j];
        if (p2 == null || p1.compareTo(p2) == 0) {
          throw new IllegalArgumentException();
        }
        double s1 = p1.slopeTo(p2);

        for (int k = j + 1; k < pointLength; k++) {
          var p3 = points[k];
          if (p3 == null || p1.compareTo(p3) == 0 || p2.compareTo(p3) == 0) {
            throw new IllegalArgumentException();
          }
          double s2 = p2.slopeTo(p3);

          for (int l = k + 1; l < pointLength; l++) {
            var p4 = points[l];
            if (p4 == null || p1.compareTo(p4) == 0 || p2.compareTo(p4) == 0 || p3.compareTo(p4) == 0) {
              throw new IllegalArgumentException();
            }
            double s3 = p3.slopeTo(p4);

            if (s1 == s2 && s2 == s3) {

              Point max = findMax(p1, p2, p3, p4);
              Point min = findMin(p1, p2, p3, p4);
              LineSegment lineSegment = new LineSegment(min, max);
              segments[n] = lineSegment;
              n += 1;
            }
          }
        }
      }
    }
  }

  // the number of line segments
  public int numberOfSegments() {
    return this.n;
  }

  // the line segments
  public LineSegment[] segments() {
    LineSegment[] tempSegments = new LineSegment[n];
    System.arraycopy(segments, 0, tempSegments, 0, n);
    return tempSegments;
  }

}