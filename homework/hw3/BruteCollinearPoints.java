import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.ArrayList;


// Performance requirement. 
// The order of growth of the running time of your program should be n4 in the worst case and 
// it should use space proportional to n plus the number of line segments returned.
// Brute force. Write a program BruteCollinearPoints.java that examines 4 points at a time and 
// checks whether they all lie on the same line segment, returning all such line segments. 
// To check whether the 4 points p, q, r, and s are collinear, check whether the three slopes 
// between p and q, between p and r, and between p and s are all equal.


public class BruteCollinearPoints {

    private static final double EPSILON = 0.0000001;

    private LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new NullPointerException();

        checkForNullPoints(points);

        checkForDuplicatePoints(points);

        ArrayList<LineSegment> tmp = new ArrayList<LineSegment>();

        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);

        // check whether the 4 points p, q, r, and s are collinear

        for (int p = 0; p < pointsCopy.length - 3; p++) {
            for (int q = p + 1; q < pointsCopy.length - 2; q++) {
                for (int r = q + 1; r < pointsCopy.length -1; r++) {
                    for (int s = r + 1; s < pointsCopy.length; s++) {
                        if (sameFloats(pointsCopy[p].slopeTo(pointsCopy[q]), pointsCopy[p].slopeTo(pointsCopy[r])) &&
                            sameFloats(pointsCopy[p].slopeTo(pointsCopy[r]), pointsCopy[p].slopeTo(pointsCopy[s]))) {
                            tmp.add(new LineSegment(pointsCopy[p], pointsCopy[s]));
                        }
                    }
                }
            }
        }
        segments = tmp.toArray(new LineSegment[tmp.size()]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, numberOfSegments());
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
