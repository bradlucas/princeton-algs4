/******************************************************************************
 *  Name:    Brad Lucas
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int numTrials;
    private double[] results;

    /*
     * Perform trials/independent experiments on an n-by-n grid
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Inputs for n and trials must be positive and non-zero");
        }
        this.numTrials = trials;
        results = new double[trials];

        for (int trial = 0; trial < this.numTrials; trial++) {
            Percolation p = new Percolation(n);
            int openedSites = 0;

            // while it doesn't percolate, try opening random cells
            while (!p.percolates()) {
                // pick a new i, y
                int i = StdRandom.uniform(1, n + 1);
                int j = StdRandom.uniform(1, n + 1);
                if (!p.isOpen(i, j)) {
                    p.open(i, j);
                    openedSites += 1;
                }
            }
            // The fraction of sites that are opened when the system percolates
            // provides an estimate of the percolation threshold.s
            // result is number of opened sites / size * size
            double result = (double) openedSites / (n * n);
            results[trial] = result;
        }
    }

    /*
     * sample mean of percolation threshold
     */
    public double mean() {
        return StdStats.mean(results);
    }

    /*
     * sample standard deviation of percolation threshold
     */
    public double stddev() {
        return StdStats.stddev(results);
    }

    /*
     * Low endpoint of 95% confidence interval = mean - 95% of stddev /
     * sqrt(trials)
     */
    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(numTrials));
    }

    /*
     * High endpoint of 95% confidence interval = mean + 95% of stddev /
     * sqrt(trials)
     */
    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(numTrials));
    }

    public static void main(String[] args) {
        // ensure two arguments
        if (args.length != 2) {
            StdOut.println("Usage: PercoluationStats N T");
            return;
        }

        // two command-line arguments N and T
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + "[" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }

}
