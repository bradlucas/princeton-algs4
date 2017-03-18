import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {

    public static void main(String[] args) {
        // Read command-line integer k
        int k = Integer.parseInt(args[0]);
        StdOut.println(k);

        // Read a sequence of strings from standard input
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            rq.enqueue(StdIn.readString());
        }

        // print k of them uniformly at random
        for (int i = 0; i < k-1; i++) {
            StdOut.println(rq.dequeue());
        }

        // print each item from the sequence at most once
    }

}

