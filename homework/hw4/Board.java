import java.util.Iterator;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class Board {

    private static final int SPACE = 0;
    private int[][] blocks;
    private int size;
    private Board[] neighbors;

    // space is the blank square. Represented here as a 0

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.blocks = copy(blocks);
        this.size = this.blocks.length;
    }
    
    // board dimension n
    public int dimension() {
        return size;
    }
    
    // number of blocks out of place
    public int hamming() {
        int cnt = 0;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (get(r, c) != goalForPosition(r, c)  && !isLastPosition(r, c)) {
                    cnt++;
                }
            }
        }
        return cnt;
    }
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int sum = 0;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                sum += calcDistances(r, c);
            }
        }
        return sum;
    }

    private int goalRow(int val) {
        return (val - 1) / size;
    }
    
    private int goalCol(int val) {
        return (val -1) % size;
    }

    private int calcDistances(int row, int col) {
        int val = get(row, col);

        if (val == SPACE) {
            return 0;
        } 
        else {
            return Math.abs(row - goalRow(val)) + Math.abs(col - goalCol(val));
        }
    }
    
    // is this board the goal board?
    public boolean isGoal() {
        // return true if all the blocks are inPlace
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                // if (get(r,c) != goalForPosition(r, c)) return false;
                if (!inPlace(r, c)) return false;
            }
        }
        return true;
    }

    private boolean isLastPosition(int row, int col) {
        return (row == size-1 && col == size-1);
    }

    private int goalForPosition(int row, int col) {
        // size = 3
        // 0,0 -> 1
        // 0,1 -> 2
        // 0,2 -> 3
        // 1,0 ->
        // ....
        if (isLastPosition(row, col)) {
            return SPACE;
        } 
        return size*row + col + 1;
    }
    
    private boolean inPlace(int row, int col) {
        // not a space
        boolean rtn = false;
        rtn = get(row, col) == goalForPosition(row, col);
        return rtn;
    }
            
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        // Copy the current block
        Board tmpBoard = new Board(blocks);

        // Exchange the first two non-space items
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size - 1; c++) {
                if (!isSpace(r, c) && !isSpace(r, c + 1)) {
                    tmpBoard.swap(r, c, r, c + 1);
                    return tmpBoard;
                }
            }
        }
        return tmpBoard;
    }
    
    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (!y.getClass().equals(this.getClass())) return false;
        if (this.dimension() != ((Board) y).dimension()) return false;

        Board other = (Board) y;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (other.get(r, c) != this.get(r, c)) return false;
            }
        }
        return true;
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new Iterable<Board>() {
            @Override
                public Iterator<Board> iterator() {
                if (neighbors == null) {
                    findNeighbors();
                }
                return new NeighborIterator();
            }
        };                
    }

    private boolean swap(int r, int c, int r2, int c2) {
        // swap position (r,c) with (r2, c2)

        // If out of bounds, return can't/false
        if (r2 < 0 || c2 < 0 || r2 >= size || c2 >= size) {
            return false;
        }

        int tmp = blocks[r][c];
        blocks[r][c] = blocks[r2][c2];
        blocks[r2][c2] = tmp;
        return true;
    }

    private boolean isSpace(int row, int col) {
        return get(row, col) == SPACE;
    }

    private void findNeighbors() {
        // add to this.neighbors

        // Find the space
        int rs = 0, cs = 0;
        boolean found = false;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (isSpace(r, c)) {
                    rs = r;
                    cs = c;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        // StdOut.println("Space is at " + rs + ", " + cs);

        // Find the neighbor boards bu looking at the four possible moves
        ArrayList<Board> boards = new ArrayList<Board>();

        Board tmpBoard = new Board(blocks);
        if (tmpBoard.swap(rs, cs, rs - 1, cs)) {
            boards.add(tmpBoard);
        }
        tmpBoard = new Board(blocks);
        if (tmpBoard.swap(rs, cs, rs, cs - 1)) {
            boards.add(tmpBoard);
        }
        tmpBoard = new Board(blocks);
        if (tmpBoard.swap(rs, cs, rs + 1, cs)) {
            boards.add(tmpBoard);
        }
        tmpBoard = new Board(blocks);
        if (tmpBoard.swap(rs, cs, rs, cs + 1)) {
            boards.add(tmpBoard);
        }
        neighbors = boards.toArray(new Board[boards.size()]);

    }
    
    private class NeighborIterator implements Iterator<Board> {
        // see neighbors
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < neighbors.length;
        }

        @Override
        public Board next() {
            if (hasNext()) {
                return neighbors[index++];
            } 
            else {
                throw new NoSuchElementException("No more neighbors");
            }
        }
         
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Removal is not supported");
        }
    }

    private int get(int row, int col) {
        return blocks[row][col];
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension() + "\n");
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                sb.append(String.format("%2d ", this.get(r, c)));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private int[][] copy(int[][] blocksParam) {
        // create internal array
        int sizeParam = blocksParam.length;
        int[][] ary = new int[sizeParam][sizeParam]; // assuming square
        for (int r = 0; r < sizeParam; r++) {
            for (int c = 0; c < sizeParam; c++) {
                ary[r][c] = blocksParam[r][c];
            }
        }
        return ary;
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        Board b = new Board(new int[][] {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}});
        // StdOut.println(b);

        // StdOut.println("" + b.dimension());
        // StdOut.println("" + b.goalForPosition(1,1));
        // StdOut.println("" + b.goalForPosition(2,2));
        // StdOut.println("" + b.hamming());
        
        b.debug();
    }


    private void debug0() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                StdOut.println(String.format("%d, %d -> %d", r, c, this.get(r, c)));
            }
        }
        StdOut.println("");
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                StdOut.println(String.format("%d, %d -> %d", r, c, this.goalForPosition(r, c)));
            }
        }
        StdOut.println("" + this.manhattan());
        Board b = new Board(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}});
        StdOut.println("Is Goal: " + b.isGoal());
        StdOut.println("" + b.manhattan());

        StdOut.println("--------------------------------------------------");

    
    
    }

    private void debug() {
        // Board b = new Board(new int[][] {{1, 0}, {3, 2}});
        // StdOut.println(b);
        // StdOut.println("" + b.dimension());
        // StdOut.println("" + b.hamming());
        // StdOut.println("" + b.manhattan());
        // StdOut.println("" + b.goalRow(2));
        // StdOut.println("" + b.goalCol(2));
        // StdOut.println("" + b.calcDistances(0, 0));
        // StdOut.println("" + b.isGoal());
        // StdOut.println("" + b.goalForPosition(1,0));
        // StdOut.println("" + b.inPlace(0, 1));
        // StdOut.println("" + b.twin());
        // StdOut.println("" + b.swap(0, 0, 1, 0));
        // StdOut.println("" + b);

        
        // b = new Board(new int[][] {{1, 2}, {0, 3}});
        // b.swap(1, 0, 1, 1);
        // StdOut.println("" + b.isGoal());

        StdOut.println("--------------------------------------------------");
        Board b = new Board(new int[][] {{1, 2}, {0, 3}});
        StdOut.println("" + b);
        StdOut.println("--------------------------------------------------");
        Iterable<Board> iter = b.neighbors();
        for (Board t : iter) {
            StdOut.println("" + t);
        }
    }
}

