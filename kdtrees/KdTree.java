import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {


  // construct an empty set of points
  private static class Node {
    // the point
    private final Point2D p;
    // the axis-aligned rectangle corresponding to this node
    private RectHV rect;
    // the left/bottom subtree
    private Node lb;
    // the right/top subtree
    private Node rt;


    public Node(Point2D p) {
      this.p = p;
    }
  }

  private Node root;
  private int size = 0;

  public KdTree() {
  }

  // is the set empty?
  public boolean isEmpty() {
    return this.root == null;
  }

  // number of points in the set
  public int size() {
    return this.size;
  }

  private Node insert(Node x, Point2D p, boolean isXOrientation) {
    if (x == null) {
      size += 1;
      return new Node(p);
    }

    if (isXOrientation) {
      double cmp = x.p.x() - p.x();
      if (cmp < 0) {
        x.rt = insert(x.rt, p, false);
        if (x.rt.rect == null) {
          x.rt.rect = new RectHV(x.p.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax());
        }
      } else if (cmp >= 0 && x.p.distanceSquaredTo(p) != 0) {
        x.lb = insert(x.lb, p, false);
        if (x.lb.rect == null) {
          x.lb.rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
      }
    } else {
      double cmp = x.p.y() - p.y();
      if (cmp < 0) {
        x.rt = insert(x.rt, p, true);
        if (x.rt.rect == null) {
          x.rt.rect = new RectHV(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.rect.ymax());
        }
      } else if (cmp >= 0 && x.p.distanceSquaredTo(p) != 0) {
        x.lb = insert(x.lb, p, true);
        if (x.lb.rect == null) {
          x.lb.rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.p.y());
        }
      }
    }

    return x;
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    if (p == null) throw new IllegalArgumentException();
    root = insert(root, p, true);
    root.rect = new RectHV(0, 0, 1, 1);
  }


  private Node get(Node x, Point2D p, boolean isXOrientation) {
    if (p == null) throw new IllegalArgumentException();
    if (x == null) return null;

    double cmp;
    boolean isNextXOrientation;
    if (isXOrientation) {
      cmp = x.p.x() - p.x();
      isNextXOrientation = false;
    } else {
      cmp = x.p.y() - p.y();
      isNextXOrientation = true;
    }

    if (cmp < 0) {
      return get(x.rt, p, isNextXOrientation);
    } else {
      if (x.p.distanceSquaredTo(p) == 0) {
        return x;
      }
      return get(x.lb, p, isNextXOrientation);
    }
  }


  // does the set contain point p?
  public boolean contains(Point2D p) {
    if (p == null) throw new IllegalArgumentException();
    return get(root, p, true) != null;
  }


  private void draw(Node x, boolean isXOrientation) {

    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.01);
    x.p.draw();
    if (isXOrientation) {
      StdDraw.setPenColor(StdDraw.RED);
      StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
    } else {
      StdDraw.setPenColor(StdDraw.BLUE);
      StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
    }


    if (x.lb != null) {
      draw(x.lb, !isXOrientation);
    }
    if (x.rt != null) {
      draw(x.rt, !isXOrientation);
    }
  }

  // draw all points to standard draw
  public void draw() {
    draw(root, true);
  }

  private void range(Node x, RectHV rect, Queue<Point2D> queue) {

    if (x == null) return;

    boolean isIntersect = x.rect.intersects(rect);
    if (isIntersect) {
      if (rect.contains(x.p)) {
        queue.enqueue(x.p);
      }
      range(x.lb, rect, queue);
      range(x.rt, rect, queue);
    }

  }


  // all points that are inside the rectangle (or on the boundary)
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) throw new IllegalArgumentException();

    Queue<Point2D> queue = new Queue<>();

    range(root, rect, queue);

    return queue;
  }

  private Point2D nearest(Node x, Point2D p, Point2D minPoint, boolean isXOrientation) {

    if (x == null) {
      return minPoint;
    }

    if (x.p.distanceSquaredTo(p) < minPoint.distanceSquaredTo(p)) {
      minPoint = x.p;
    }

    double cmp;
    if (isXOrientation) {
      cmp = p.x() - x.p.x();
    } else {
      cmp = p.y() - x.p.y();
    }

    if (cmp < 0) {
      if (x.lb != null) {
        minPoint = nearest(x.lb, p, minPoint, !isXOrientation);
      }
      if (x.rt != null &&
        x.rt.rect.distanceSquaredTo(p) < minPoint.distanceSquaredTo(p)) {
        minPoint = nearest(x.rt, p, minPoint, !isXOrientation);
      }
    } else {
      if (x.rt != null) {
        minPoint = nearest(x.rt, p, minPoint, !isXOrientation);
      }
      if (x.lb != null &&
        x.lb.rect.distanceSquaredTo(p) < minPoint.distanceSquaredTo(p)) {
        minPoint = nearest(x.lb, p, minPoint, !isXOrientation);
      }
    }

    return minPoint;
  }

  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D p) {
    if (p == null) throw new IllegalArgumentException();
    if (size < 1) {
      return null;
    }

    Point2D minP = new Point2D(root.p.x(), root.p.y());
    return nearest(root, p, minP, true);
  }

  // unit testing of the methods (optional)
  public static void main(String[] args) {

    String filename = args[0];
    In in = new In(filename);
    KdTree kdtree = new KdTree();
    System.out.println("size after init the kdtree " + kdtree.size());
    System.out.println("isEmpty after init the kdtree " + kdtree.isEmpty());

    while (!in.isEmpty()) {
      double x = in.readDouble();
      double y = in.readDouble();
      Point2D p = new Point2D(x, y);
      kdtree.insert(p);
    }

    System.out.println("isEmpty is " + kdtree.isEmpty());
    System.out.println("kdtree size is " + kdtree.size());

    boolean contains = kdtree.contains(new Point2D(1.0, 0.25));
    System.out.println("contains points? " + contains);

    kdtree.draw();

    Point2D findPoint = new Point2D(0.31, 0.29);
    StdDraw.setPenColor(StdDraw.GREEN);
    StdDraw.setPenRadius(0.02);
    findPoint.draw();

    Point2D p = kdtree.nearest(findPoint);
    System.out.println("nearest point is " + p);

  }
}