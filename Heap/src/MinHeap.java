import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Your implementation of a MinHeap.
 *
 * @author JADE LAW
 * @version 1.0
 * @userid jlaw39 (i.e. gburdell3)
 * @GTID 903437907 (i.e. 900000000)
 */
public class MinHeap<T extends Comparable<? super T>> {

    /**
     * The initial capacity of the MinHeap when created with the default
     * constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    // Do not add new instance variables or modify existing ones.
    private T[] backingArray;
    private int size;

    /**
     * Constructs a new MinHeap.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     */
    public MinHeap() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Creates a properly ordered heap from a set of initial values.
     *
     * You must use the BuildHeap algorithm that was taught in lecture! Simply
     * adding the data one by one using the add method will not get any credit.
     * As a reminder, this is the algorithm that involves building the heap
     * from the bottom up by repeated use of downHeap operations.
     *
     * Before doing the algorithm, first copy over the data from the
     * ArrayList to the backingArray (leaving index 0 of the backingArray
     * empty). The data in the backingArray should be in the same order as it
     * appears in the passed in ArrayList before you start the BuildHeap
     * algorithm.
     *
     * The backingArray should have capacity 2n + 1 where n is the
     * number of data in the passed in ArrayList (not INITIAL_CAPACITY).
     * Index 0 should remain empty, indices 1 to n should contain the data in
     * proper order, and the rest of the indices should be empty.
     *
     * @param data a list of data to initialize the heap with
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public MinHeap(ArrayList<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot build from a null ArrayList.");
        }
        size = data.size();
        int cap = (2 * size) + 1;
        backingArray = (T[]) new Comparable[cap];
        for (int i = 0; i < size; i++) {
            if (data.get(i) == null) {
                throw new IllegalArgumentException("Cannot add null values");
            } else {
                backingArray[i + 1] = data.get(i);
            }
        }
        for (int i = size / 2; i > 0; i--) {
            heapRec(i);
        }
    }

    private void heapRec(int curr) {
    	int right = (2 * curr) + 1;
    	int left = (2 * curr);
        int min = curr;
        if (left <= size && backingArray[left].compareTo(backingArray[min]) < 0) {
            min = left;
        }
        if (right <= size && backingArray[right].compareTo(backingArray[min]) < 0) {
            min = right;
        }
        if (min <= size && backingArray[min].compareTo(backingArray[curr]) < 0) {
            T temp = backingArray[curr];
            backingArray[curr] = backingArray[min];
            backingArray[min] = temp;
            heapRec(min);
            heapRec(curr);
        }
    }

    /**
     * Adds an item to the heap. If the backing array is full (except for
     * index 0) and you're trying to add a new item, then double its capacity.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot add null data.");
        } else if (size == backingArray.length - 1) {
            resize();
            backingArray[size + 1] = data;
            size++;
            upheap(size);
        } else {
            backingArray[size + 1] = data;
            size++;
            upheap(size);
        }
    }

    private void resize() {
        int newCap = (2 * backingArray.length);
        T[] newArray = (T[]) new Comparable[newCap];
        for (int i = 1; i <= size; i++) {
            newArray[i] = backingArray[i];
        }
        backingArray = newArray;
    }

    private void upheap(int i) {
        while (i > 1) {
            int parent = i / 2;
            if (backingArray[i].compareTo(backingArray[parent]) < 0) {
                T temp = backingArray[parent];
                backingArray[parent] = backingArray[i];
                backingArray[i] = temp;
            }
            i = parent;
        }
    }

    /**
     * Removes and returns the min item of the heap. As usual for array-backed
     * structures, be sure to null out spots as you remove. Do not decrease the
     * capacity of the backing array.
     *
     * @return the data that was removed
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        } else {
            T removed = backingArray[1];
            backingArray[1] = backingArray[size];
            backingArray[size] = null;
            size--;
            heapRec(1);
            return removed;
        }
    }

    /**
     * Returns the minimum element in the heap.
     *
     * @return the minimum element
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T getMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        } else {
            return backingArray[1];
        }
    }

    /**
     * Returns whether or not the heap is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Clears the heap.
     *
     * Resets the backing array to a new array of the initial capacity and
     * resets the size.
     */
    public void clear() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the backing array of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the backing array of the list
     */
    public T[] getBackingArray() {
        // DO NOT MODIFY THIS METHOD!
        return backingArray;
    }

    /**
     * Returns the size of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the list
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
