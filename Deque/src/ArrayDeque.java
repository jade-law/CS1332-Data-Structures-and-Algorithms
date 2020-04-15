import java.util.NoSuchElementException;

/**
 * Your implementation of an ArrayDeque.
 *
 * @author JADE LAW
 * @version 1.0
 * @userid jlaw39 (i.e. gburdell3)
 * @GTID 903437907 (i.e. 900000000)
 *
 * Collaborators: NONE
 *
 * Resources: NONE
 */
public class ArrayDeque<T> {

    /**
     * The initial capacity of the ArrayDeque.
     *
     * DO NOT MODIFY THIS VARIABLE.
     */
    public static final int INITIAL_CAPACITY = 11;

    // Do not add new instance variables or modify existing ones.
    private T[] backingArray;
    private int front;
    private int size;

    /**
     * Constructs a new ArrayDeque.
     */
    public ArrayDeque() {
        backingArray = (T[]) new Object[INITIAL_CAPACITY];
        front = 0;
        size = 0;
    }

    /**
     * Adds the element to the front of the deque.
     *
     * If sufficient space is not available in the backing array, resize it to
     * double the current capacity. When resizing,, copy elements to the
     * beginning of the new array and reset front to 0. After the resize, the
     * new data should be at index 0 of the array.
     *
     * Must be amortized O(1).
     *
     * @param data the data to add to the front of the deque
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addFirst(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot add null data.");
        }
        if (size == 0) {
            backingArray[mod(front - 1, backingArray.length)] = data;
            front = mod(front - 1, backingArray.length);
            size++;
        } else if (size == backingArray.length) {
            resize(data, true);
        } else {
            int i = mod(front - 1, backingArray.length);
            backingArray[i] = data;
            front = i;
            size++;
        }
//        } else if (size == 0) {
//            backingArray[backingArray.length - 1] = data;
//            front = backingArray.length - 1;
//            size++;
//        } else if (size == backingArray.length) {
//            backingArray = resize(backingArray);
//            int i = mod(front - 1, backingArray.length);
//            backingArray[i] = data;
//            front = i;
//            size++;
//        } else {
//            int i = mod(front - 1, backingArray.length);
//            backingArray[i] = data;
//            front = i;
//            size++;
//        }
    }

    /**
     * Adds the element to the back of the deque.
     *
     * If sufficient space is not available in the backing array, resize it to
     * double the current capacity. When resizing,, copy elements to the
     * beginning of the new array and reset front to 0.
     *
     * Must be amortized O(1).
     *
     * @param data the data to add to the back of the deque
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addLast(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot add null data.");
        } else if (size == 0) {
            backingArray[front] = data;
            size++;
        } else if (size == backingArray.length) {
            resize(data, false);
        } else {
            int i = mod(front + size, backingArray.length);
            backingArray[i] = data;
            size++;
        }
    }

    private void resize(T data, boolean atFront) {
        int newLength = (backingArray.length) * 2;
        T[] newArray = (T[]) new Object[newLength];
        int start = front;
        if (atFront) {
            newArray[0] = data;
            for (int i = 1; i <= size; i++) {
                newArray[i] = backingArray[start];
                start = mod(start + 1, size);
            }
        } else {
            for (int i = 0; i < size; i++) {
                newArray[i] = backingArray[start];
                start = mod(start + 1, size);
            }
            newArray[size] = data;
        }
        front = 0;
        size++;
        backingArray = newArray;
    }

    /**
     * Removes and returns the first element of the deque.
     *
     * Do not grow or shrink the backing array.
     *
     * If the deque becomes empty as a result of this call, do not reset
     * front to 0.
     *
     * Replace any spots that you remove from with null. Failure to do so can
     * result in loss of points.
     *
     * Must be O(1).
     *
     * @return the data formerly located at the front of the deque
     * @throws java.util.NoSuchElementException if the deque is empty
     */
    public T removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty.");
        } else {
            T removed = backingArray[front];
            backingArray[front] = null;
            front = mod(front + 1, backingArray.length);
            size--;
            return removed;
        }
    }

    /**
     * Removes and returns the last element of the deque.
     *
     * Do not grow or shrink the backing array.
     *
     * If the deque becomes empty as a result of this call, do not reset
     * front to 0.
     *
     * Replace any spots that you remove from with null. Failure to do so can
     * result in loss of points.
     *
     * Must be O(1).
     *
     * @return the data formerly located at the back of the deque
     * @throws java.util.NoSuchElementException if the deque is empty
     */
    public T removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty.");
        } else {
            T removed = backingArray[mod(
                    front + size - 1, backingArray.length)];
            backingArray[mod(front + size - 1, backingArray.length)] = null;
            size--;
            return removed;
        }
    }

    /**
     * Returns the first data of the deque without removing it.
     *
     * Must be O(1).
     *
     * @return the first data
     * @throws java.util.NoSuchElementException if the deque is empty
     */
    public T getFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty.");
        } else {
            return backingArray[mod(front, backingArray.length)];
        }
    }

    /**
     * Returns the last data of the deque without removing it.
     *
     * Must be O(1).
     *
     * @return the last data
     * @throws java.util.NoSuchElementException if the deque is empty
     */
    public T getLast() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty.");
        } else {
            int back = front + size - 1;
            return backingArray[mod(back, backingArray.length)];
        }
    }

    /**
     * Returns the backing array of the deque.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the backing array of the deque
     */
    public T[] getBackingArray() {
        // DO NOT MODIFY THIS METHOD!
        return backingArray;
    }

    /**
     * Returns the size of the deque.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the deque
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }

    /**
     * Returns the smallest non-negative remainder when dividing index by
     * modulo. So, for example, if modulo is 5, then this method will return
     * either 0, 1, 2, 3, or 4, depending on what the remainder is.
     *
     * This differs from using the % operator in that the % operator returns
     * the smallest answer with the same sign as the dividend. So, for example,
     * (-5) % 6 => -5, but with this method, mod(-5, 6) = 1.
     *
     * Examples:
     * mod(-3, 5) => 2
     * mod(11, 6) => 5
     * mod(-7, 7) => 0
     *
     * This helper method is here to make the math part of the circular
     * behavior easier to work with.
     *
     * @param index  the number to take the remainder of
     * @param modulo the divisor to divide by
     * @return the remainder in its smallest non-negative form
     * @throws java.lang.IllegalArgumentException if the modulo is non-positive
     */
    private static int mod(int index, int modulo) {
        // DO NOT MODIFY THIS METHOD!
        if (modulo <= 0) {
            throw new IllegalArgumentException("The modulo must be positive");
        }
        int newIndex = index % modulo;
        return newIndex >= 0 ? newIndex : newIndex + modulo;
    }
}
