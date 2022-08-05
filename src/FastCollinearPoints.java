import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {

  private final ArrayList<LineSegment> lineSegments;

  private Point[] copyPointArray(Point[] points) {
    if (points == null) {
      throw new IllegalArgumentException();
    }

    Point[] pointsCopy = new Point[points.length];
    for (int i = 0; i < points.length; i++) {
      if (points[i] == null) {
        throw new IllegalArgumentException();
      }
      pointsCopy[i] = points[i];
    }
    return pointsCopy;
  }

  // finds all line segments containing 4 or more points
  public FastCollinearPoints(Point[] points) {
    Point[] pointsCopy = copyPointArray(points);
    int pointLength = pointsCopy.length;

    Arrays.sort(pointsCopy);
    for (int i = 0; i < pointLength - 1; i++) {
      if (pointsCopy[i].compareTo(pointsCopy[i + 1]) == 0) {
        throw new IllegalArgumentException();
      }
    }

    lineSegments = new ArrayList<>();
    for (int i = 0; i < pointLength - 3; i++) {
      Arrays.sort(pointsCopy);
      Comparator<Point> slopCompare = pointsCopy[i].slopeOrder();
      Arrays.sort(pointsCopy, slopCompare);

      Point p = pointsCopy[0];
      for (int first = 1, last = 2; last < pointLength; last++) {
        while (last < pointLength && Double.compare(p.slopeTo(pointsCopy[first]), p.slopeTo(pointsCopy[last])) == 0) {
          last += 1;
        }
        if (last - first >= 3 && p.compareTo(pointsCopy[first]) < 0) {
          lineSegments.add(new LineSegment(p, pointsCopy[last - 1]));
        }
        first = last;
      }
    }

  }

  // the number of line segments
  public int numberOfSegments() {
    return lineSegments.size();
  }

  // the line segments
  public LineSegment[] segments() {
    return lineSegments.toArray(new LineSegment[0]);

  }
}