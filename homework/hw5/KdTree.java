import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;

public class KdTree {

    private int size = 0;

    private class Node {
        private Point2D point;
        private RectHV rect;
        private Node left;
        private Node right;

        public Node(Point2D p) {
            this.point = p;
        }
    }

    private Node root;

    // construct an empty set of points
    public  KdTree() {
        this.size = 0;
        this.root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D point) {
        if (isEmpty()) {
            root = new Node(point);
            root.rect = new RectHV(0, 0, 1, 1);
            size++;
            return;
        }
        root = insert(root, point, true);
    }

    // does the set contain point p?
    public boolean contains(Point2D point) {
        return contains(root, point, true);
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, true);
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) { 
            throw new NullPointerException();
        }
        Queue<Point2D> queue = new Queue<Point2D>();
        if (!isEmpty()) {
            range(queue, root, rect);
        }
        return queue;
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D point) {
        if (point == null) return null;
        return nearest(root, point, root.point, true);
    }

    // ----------------------------------------------------------------------------------------------------
    // Private functions
    private int comparePoints(Point2D p1, Point2D p2, boolean vert) {
        if (vert) {
            return Double.compare(p1.x(), p2.x());
        } 
        else {
            return Double.compare(p1.y(), p2.y());
        }
    }
        
    private Node insert(Node node, Point2D point, boolean vert) {
        if (node == null) {
            size++;
            return new Node(point);              // Is this right?
        }
        if (node.point.equals(point)) {
            return node;
        }

        int cmp = comparePoints(point, node.point, vert);
        if (cmp < 0) {
            node.left = insert(node.left, point, !vert);
            if (node.left.rect == null) {
                if (vert) {
                    node.left.rect = new RectHV(node.rect.xmin(), node.rect.ymin(), 
                                                node.point.x(), node.rect.ymax());
                }
                else {
                    node.left.rect = new RectHV(node.rect.xmin(), node.rect.ymin(), 
                                                node.rect.xmax(), node.point.y());
                }
            }
        } 
        else {
            node.right = insert(node.right, point, !vert);
            if (node.right.rect == null) {
                if (vert) {
                    node.right.rect = new RectHV(node.point.x(), node.rect.ymin(), 
                                                 node.rect.xmax(), node.rect.ymax());
                }
                else {
                    node.right.rect = new RectHV(node.rect.xmin(), node.point.y(), 
                                                 node.rect.xmax(), node.rect.ymax());
                }
            }
        }
        return node;
    } 

    private boolean contains(Node node, Point2D point, boolean vert) {
        if (node == null) {
            return false;
        }

        if (node.point.equals(point)) {
            return true;
        }

        int cmp = comparePoints(point, node.point, vert);
        if (cmp < 0) {
            return contains(node.left, point, !vert);
        } 
        else {
            return contains(node.right, point, !vert);
        }
    }

    private void draw(Node node, boolean vert) {
        if (node == null) return;

        // Draw the point at node
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();

        // if vert?
        StdDraw.setPenRadius(0.001);
        if (vert) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
        } 
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.rect.ymin(), node.point.x(), node.rect.ymax());
        }
        // flip orientation
        boolean next = !vert;
        draw(node.left, next);
        draw(node.right, next);
    }

    private void range(Queue<Point2D> queue, Node node, RectHV rect) {
        if (!rect.intersects(node.rect)) {
            return;
        }

        if (rect.contains(node.point)) {
            queue.enqueue(node.point);
        }

        // any left?
        if (node.left != null) {
            range(queue, node.left, rect);
        }

        // any right?
        if (node.right != null) {
            range(queue, node.right, rect);
        }
    }

    private Point2D nearest(Node node, Point2D point, Point2D closestPoint, boolean vert) {
        Point2D closest = closestPoint;

        if (node == null) return closest;

        // If the current node is closer then update the closest point
        if (node.point.distanceSquaredTo(point) < closest.distanceSquaredTo(point)) {
            closest = node.point;
        }

        // if the current rectangle is closer to point then the closest point, check it's subtrees
        if (node.rect.distanceSquaredTo(point) < closest.distanceSquaredTo(point)) {
            // find the subtree point is in
            Node near;
            Node far;
            if ((vert && (point.x() < node.point.x())) || (!vert && (point.y() < node.point.y()))) {
                near = node.left;
                far = node.right;
            }
            else {
                near = node.right;
                far = node.left;
            }
        }
        return closest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }

}
