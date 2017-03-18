
/******************************************************************************
 *  Name:    Brad Lucas
 * 
 ******************************************************************************/
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size;
    private int top = 0;
    private int bottom;
    private boolean[][] opened;
    private WeightedQuickUnionUF qf;

    /*
     * Create n-by-n grid, with all sites blocked
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException("Inputs for n must be positive and non-zero");
        }
        size = n;
        bottom = (size * size) + 1; // last node
        qf = new WeightedQuickUnionUF((size * size) + 2); // n * n plus to
                                                          // 'imaginary
                                                          // nodes for the top
                                                          // and
                                                          // bottom
        opened = new boolean[size][size];
    }

    /*
     * Open site (row, col) if it is not open already
     */
    public void open(int row, int col) {

        opened[row - 1][col - 1] = true;

        // If first row connect with top
        if (row == 1) {
            qf.union(getIndex(row, col), top);
        }
        // If bottom row connect with bottom
        if (row == size) {
            qf.union(getIndex(row, col), bottom);
        }

        // col > 1
        if (col > 1 && isOpen(row, col - 1)) {
            qf.union(getIndex(row, col), getIndex(row, col - 1));
        }
        // col < size
        if (col < size && isOpen(row, col + 1)) {
            qf.union(getIndex(row, col), getIndex(row, col + 1));
        }

        // row > 1
        if (row > 1 && isOpen(row - 1, col)) {
            qf.union(getIndex(row, col), getIndex(row - 1, col));
        }
        // row < size
        if (row < size && isOpen(row + 1, col)) {
            qf.union(getIndex(row, col), getIndex(row + 1, col));
        }

    }

    /*
     * Open site (row, col) if it is not open already
     */
    public boolean isOpen(int row, int col) {
        if (0 < row && row <= size && 0 < col && col <= size) {
            return opened[row - 1][col - 1];
        } 
        else {
            throw new IndexOutOfBoundsException();
        }
    }

    /*
     * matrix is array...
     */
    private int getIndex(int row, int col) {
        return size * (row - 1) + col;
    }

    /*
     * Is site (row, col) full?
     */
    public boolean isFull(int row, int col) {
        // ensure valid values
        if (0 < row && row <= size && 0 < col && col <= size) {
            return qf.connected(top, getIndex(row, col));
        } 
        else {
            throw new IndexOutOfBoundsException();
        }
    }

    /*
     * Number of open sites
     */
    public int numberOfOpenSites() {
        int counter = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
                if (opened[i][j]) {
                    counter++;
                }
        }
        return counter;
    }

    /*
     * Does the system percolate?
     */
    public boolean percolates() {
        return qf.connected(top, bottom);
    }

    public static void main(String[] args) {
        // test client (optional)
    }
}
