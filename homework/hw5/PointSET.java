import java.util.List;
import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;



public class PointSET {
    // use a red-black BST using SET from algs4.jar
    private SET<Point2D> points;


    // construct an empty set of points
    public PointSET() {
        points = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty()  {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException("null parameter passed to insert");
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("null parameter passed to contains");
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p: points) {
            StdDraw.point(p.x(), p.y());
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("null parameter passed to range");
        
        // @see RectHV.contains(Point2D p)
        List<Point2D> containsList = new ArrayList<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                containsList.add(p);
            }
        }
        return containsList;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("null parameter passed to nearest");

        // @see Point2D.distanceTo(Point2D);
        Point2D nearestPoint = null;
        for (Point2D pTmp : points) {
            if (nearestPoint == null || pTmp.distanceTo(p) < nearestPoint.distanceTo(p)) {
                nearestPoint = pTmp;
            }
        }
        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {


    }

}
