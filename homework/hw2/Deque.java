import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

// ----------------------------------------------------------------------------------------------------------
// A double-ended queue or deque (pronounced "deck") is a generalization of a stack and a queue that supports 
// adding and removing items from either the front or the back of the data structure. 
// Create a generic data type Deque that implements the following API:


// Corner cases
// Throw a java.lang.NullPointerException if the client attempts to add a null item; 
// throw a java.util.NoSuchElementException if the client attempts to remove an item from an empty deque;
// throw a java.lang.UnsupportedOperationException if the client calls the remove() method in the iterator; 
// throw a java.util.NoSuchElementException if the client calls the next() method in the iterator and 
// there are no more items to return.


// Performance requirements.   
// Your deque implementation must support each deque operation in constant worst-case time. 
// A deque containing n items must use at most 48n + 192 bytes of memory. 
// and use space proportional to the number of items currently in the deque. 
// Additionally, your iterator implementation must support each operation (including construction) in constant worst-case time.


public class Deque<Item> implements Iterable<Item> {

    private Node first;    // beginning of queue
    private Node last;     // end of queue
    private int n;         // number of elements on queue

    private class Node {
        private Item item;
        private Node next;
        private Node prev;

        Node(Item item) {
            this.item = item;
            next = null;
            prev = null;
        }
    }

    public Deque() {
        first = null;
        last  = null;
        n = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return n;     
    }

    public void addFirst(Item item) {
        if (item == null) { 
            throw new java.lang.NullPointerException(); 
        }

        if (isEmpty()) {
            first = new Node(item);
            last = first;
        } 
        else {
            Node currentFirst = first;
            first = new Node(item);
            first.item = item;
            first.next = currentFirst;
            first.prev = null;
            currentFirst.prev = first;
        }
        n++;
    }
    
    public Item removeFirst() {
        if (isEmpty()) { 
            throw new java.util.NoSuchElementException(); 
        }

        Node node = first;
        if (n == 1) {
            first = null;
            last = null;
        } 
        else {
            first.next.prev = null;
            first = first.next;
        }
        n--;
        return node.item;
    }

    public void addLast(Item item) {
        if (item == null) { 
            throw new java.lang.NullPointerException(); 
        }

        if (isEmpty()) {
            last = new Node(item);
            first = last;
        } 
        else {
            Node currentLast = last;
            last = new Node(item);
            last.item = item;
            last.prev = currentLast;
            last.next = null;
            currentLast.next = last;
        }
        n++;
    }

    public Item removeLast() {
        if (isEmpty()) { 
            throw new java.util.NoSuchElementException(); 
        }

        Item item = last.item;
        if (n == 1) {
            first = null;
            last = null;
        } 
        else {
            last.prev.next = null;
            last = last.prev;
        }
        n--;
        if (isEmpty()) last = null;   // to avoid loitering
        return item;
    }
 

    public Iterator<Item> iterator()  {
        return new ListIterator();  
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext()  { 
            return current != null;                     
        }

        public void remove()      { 
            throw new UnsupportedOperationException();  
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
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

        Deque<Integer> dq = new Deque<Integer>();
        dq.addFirst(1);
        StdOut.println("dq: " + dq);
        dq.removeFirst();
        StdOut.println("dq: " + dq);
        StdOut.println("-");

        dq = new Deque<Integer>();
        dq.addFirst(1);
        StdOut.println("dq: " + dq);
        dq.removeLast();
        StdOut.println("dq: " + dq);
        StdOut.println("-");

        dq = new Deque<Integer>();
        dq.addFirst(1);
        dq.addLast(2);
        StdOut.println("dq: " + dq);
        dq.removeLast();
        StdOut.println("dq: " + dq);
        StdOut.println("-");




        // dq.addFirst(2);
        // dq.addFirst(3);
        // StdOut.println("dq: " + dq);


        // StdOut.println("Remove last");
        // dq.removeLast();
        // StdOut.println("dq: " + dq);

        // StdOut.println("Add last");
        // dq.addLast(1);
        // StdOut.println("dq: " + dq);


        // StdOut.println("Remove first");
        // dq.removeFirst();
        // StdOut.println("dq: " + dq);

    }

}


