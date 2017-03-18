import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

// Randomized queue
// A randomized queue is similar to a stack or queue, except that the item removed is chosen uniformly 
// at random from items in the data structure. 
// Create a generic data type RandomizedQueue that implements the following API:

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[0];
        n = 0;
    }

    // is the queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the queue
    public int size() {
        return n;
    }

    private void resize(int nSize) {
        // new array 
        Item[] temp = (Item[]) new Object[nSize];
        // copy over
        for (int i = 0; i < n; i++) {
            temp[i] = items[i];
        }
        items = temp;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (n == 0) {
            resize(1);
        } 
        items[n++] = item;
        if (n == items.length) {
            resize(2*n);
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();

        // Pick a random index
        int idx = StdRandom.uniform(n);
        
        // get the random item
        Item rtn = items[idx];
        // put the last item where the random one was
        items[idx] = items[--n];
        // put null in the last spot
        items[n] = null;

        if (n <= items.length/4) {
            resize(items.length/2);
        }
        return rtn;
    }

    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        // return the randome nth eement in our list of nodes
        // Pick a random index
        int idx = (int) (StdRandom.uniform() * n);
        Item rtn = items[idx];
        return rtn;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }
    
    // an iterator, doesn't implement remove() since it's optional
    private class RandomizedQueueIterator implements Iterator<Item> {

        private RandomizedQueue<Item> temp = new RandomizedQueue<Item>();

        public RandomizedQueueIterator() {
            // make a copy of the queue so we can randomize 
            for (Object item : items) {
                if (item == null) break;
                temp.enqueue((Item) item);
            }
        }

        public boolean hasNext() { 
            return !temp.isEmpty();
        }

        public void remove() { 
            throw new UnsupportedOperationException();  
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return temp.dequeue();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Item item : this) {  // iterator
            sb.append(item);
        }
        return sb.toString();
    }

    // unit testing (optional)
    public static void main(String[] args) {

        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();

        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        rq.enqueue(4);

        StdOut.println(rq);

        StdOut.println(rq.sample());
        StdOut.println(rq.sample());
        StdOut.println(rq.sample());
        StdOut.println(rq.sample());
        StdOut.println(rq.sample());
        StdOut.println(rq.sample());
        StdOut.println(rq.sample());


    }
}
