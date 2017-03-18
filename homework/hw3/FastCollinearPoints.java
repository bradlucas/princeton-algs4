import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Collections;

public class FastCollinearPoints {

    private static final double EPSILON = 0.0000001;

    private HashMap<Double, List<Point>> foundSegments = new HashMap<>();
    private List<LineSegment> segments = new ArrayList<>();

    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] pointsParam) {
        if (pointsParam == null) throw new NullPointerException();
        checkForNullPoints(pointsParam);
        checkForDuplicatePoints(pointsParam);

        // copy the input
        Point[] points = Arrays.copyOf(pointsParam, pointsParam.length);

        // for each point, 

        for (Point pt : pointsParam) {

            List<Point> slopePoints = new ArrayList<>();

            double slope = 0;
            double previousSlope = Double.NEGATIVE_INFINITY;

            // Sort our working array of points by the function pt.SlopeOrder()
            Arrays.sort(points, pt.slopeOrder());

            // search for 3 other points which have the same slope with respect to pt

            for (int i = 1; i < points.length; i++) {
                slope = pt.slopeTo(points[i]);
                if (sameFloats(slope, previousSlope)) {
                    slopePoints.add(points[i]);
                } 
                else {
                    if (slopePoints.size() >= 3) {
                        slopePoints.add(pt);
                        addSegment(slopePoints, previousSlope);
                    }
                    slopePoints.clear();
                    slopePoints.add(points[i]);
                }
                previousSlope = slope;
            }

            if (slopePoints.size() >= 3) {
                slopePoints.add(pt);
                addSegment(slopePoints, previousSlope);
            }
        }
    }

    private void addSegment(List<Point> slopePoints, double slope) {
        List<Point> endPoints = foundSegments.get(slope);
        Collections.sort(slopePoints);

        Point startPoint = slopePoints.get(0);
        Point endPoint = slopePoints.get(slopePoints.size() - 1);

        if (endPoints == null) {
            endPoints = new ArrayList<>();
            endPoints.add(endPoint);
            foundSegments.put(slope, endPoints);
            segments.add(new LineSegment(startPoint, endPoint));
        } 
        else {
            for (Point currentEndPoint : endPoints) {
                if (currentEndPoint.compareTo(endPoint) == 0) {
                    return;
                }
            }
            endPoints.add(endPoint);
            segments.add(new LineSegment(startPoint, endPoint));
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }

    private void checkForNullPoints(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Found a null point in given points");
            }
        }
    }

    private void checkForDuplicatePoints(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = 1 + i; j < points.length; j++) {
                // if points[i] == points[j] then throw the IllegalArgumentException
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Found duplicate point in given points");
                }
            }
        }
    }

    private boolean sameFloats(double a, double b) {
        // return (Math.abs(a - b) < EPSILON);
        return a == b;
    }
    
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
