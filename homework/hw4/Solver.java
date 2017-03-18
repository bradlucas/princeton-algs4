import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Comparator;

public class Solver {
    
    private boolean solved = false;
    private List<Board> boards = new ArrayList<Board>();

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        // PriorityQueue

        MinPQ<SolverStep> priorizedSteps = new MinPQ<>(new SolverStepComparator());
        priorizedSteps.insert(new SolverStep(initial, 0, null));

        MinPQ<SolverStep> priorizedStepsTwin = new MinPQ<>(new SolverStepComparator());
        priorizedStepsTwin.insert(new SolverStep(initial.twin(), 0, null));

        SolverStep step;
        while (!priorizedSteps.min().getBoard().isGoal() && !priorizedStepsTwin.min().getBoard().isGoal()) {
            step = priorizedSteps.delMin();
            for (Board neighbor : step.getBoard().neighbors()) {
                if (!isAlreadyInSolutionPath(step, neighbor)) {
                    priorizedSteps.insert(new SolverStep(neighbor, step.getMoves() + 1, step));
                }
            }

            SolverStep stepTwin = priorizedStepsTwin.delMin();
            for (Board neighbor : stepTwin.getBoard().neighbors()) {
                if (!isAlreadyInSolutionPath(stepTwin, neighbor)) {
                    priorizedStepsTwin.insert(new SolverStep(neighbor, stepTwin.getMoves() + 1, stepTwin));
                }
            }
        }
        step = priorizedSteps.delMin();
        solved = step.getBoard().isGoal();

        boards.add(step.getBoard());
        while ((step = step.getPreviousStep()) != null) {
            boards.add(0, step.getBoard());
        }

    }

    private boolean isAlreadyInSolutionPath(SolverStep lastStep, Board board) {
        SolverStep previousStep = lastStep;
        while ((previousStep = previousStep.getPreviousStep()) != null) {
            if (previousStep.getBoard().equals(board)) {
                return true;
            }
        }
        return false;
    }


    private static class SolverStep {

        private int moves;
        private Board board;
        private SolverStep previousStep;

        private SolverStep(Board board, int moves, SolverStep previousStep) {
            this.board = board;
            this.moves = moves;
            this.previousStep = previousStep;
        }

        public int getMoves() {
            return moves;
        }

        public int getPriority() {
            return board.manhattan() + moves;
        }

        public Board getBoard() {
            return board;
        }

        public SolverStep getPreviousStep() {
            return previousStep;
        }
    }


    private static class SolverStepComparator implements Comparator<SolverStep> {

        @Override
        public int compare(SolverStep step1, SolverStep step2) {
            return step1.getPriority() - step2.getPriority();
        }
    }

    // ----------------------------------------------------------------------------------------------------



    // is the initial board solvable?
    public boolean isSolvable() {
        return solved;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        int num = 0;
        if (isSolvable()) {
            num = boards.size() -1;
        } 
        else {
            num = -1;
        }
        return num;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        Iterable<Board> it;
        if (isSolvable()) {
            it = new Iterable<Board>() {
                    @Override
                    public Iterator<Board> iterator() {
                        return new BoardsIterator();
                    }
                };
        } 
        else {
            it = null;
        }
        return it;
    }

    private class BoardsIterator implements Iterator<Board> {
        // see boards
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < boards.size();
        }

        @Override
        public Board next() {
            if (hasNext()) {
                return boards.get(index++);
            } 
            else {
                throw new NoSuchElementException("No more boards");
            }
        }
         
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Removal is not supported");
        }
    }


    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
