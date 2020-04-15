import java.util.NoSuchElementException;

/**
 * Your implementation of a non-circular DoublyLinkedList with a tail pointer.
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
public class DoublyLinkedList<T> {

    // Do not add new instance variables or modify existing ones.
    private DoublyLinkedListNode<T> head;
    private DoublyLinkedListNode<T> tail;
    private int size;

    // Do not add a constructor.

    /**
     * Adds the element to the specified index.
     *
     * Must be O(1) for indices 0 and size and O(n) for all other cases.
     *
     * @param index the index at which to add the new element
     * @param data  the data to add at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index > size
     * @throws java.lang.IllegalArgumentException  if data is null
     */
    public void addAtIndex(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                "Index is not within the length of list.");
        } else if (data == null) {
            throw new IllegalArgumentException("Cannot input null data.");
        } else if (index == 0) {
            addToFront(data);
        } else if (index == size) {
            addToBack(data);
        } else if (isEmpty()) {
            addFirst(data);
        } else {
            int i = 0;
            DoublyLinkedListNode<T> curr = head;
            DoublyLinkedListNode<T> prev;
            DoublyLinkedListNode<T> node = new DoublyLinkedListNode<>(data);
            while (i != index) {
                curr = curr.getNext();
                i++;
            }
            prev = curr.getPrevious();
            node.setPrevious(prev);
            node.setNext(curr);
            prev.setNext(node);
            curr.setPrevious(node);
            size++;
        }
    }

    /**
     * Adds the element to the front of the list.
     *
     * Must be O(1).
     *
     * @param data the data to add to the front of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToFront(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot input null data.");
        } else if (isEmpty()) {
            addFirst(data);
        } else {
            DoublyLinkedListNode<T> node =
                    new DoublyLinkedListNode<>(data, null, head);
            head.setPrevious(node);
            head = node;
            size++;
        }
    }

    /**
     * Adds the element to the back of the list.
     *
     * Must be O(1).
     *
     * @param data the data to add to the back of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToBack(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot input null data.");
        } else if (isEmpty()) {
            addFirst(data);
        } else {
            DoublyLinkedListNode<T> node =
                    new DoublyLinkedListNode<>(data, tail, null);
            tail.setNext(node);
            tail = node;
            size++;
        }
    }

    private void addFirst(T data) {
        DoublyLinkedListNode<T> node = new DoublyLinkedListNode<>(data);
        head = node;
        tail = node;
        size = 1;
    }

    /**
     * Removes and returns the element at the specified index.
     *
     * Must be O(1) for indices 0 and size - 1 and O(n) for all other cases.
     *
     * @param index the index of the element to remove
     * @return the data formerly located at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T removeAtIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                "Index is not within length of list.");
        } else if (index == 0) {
            return removeFromFront();
        } else if (index == size - 1) {
            return removeFromBack();
        } else {
            int i = 0;
            DoublyLinkedListNode<T> curr = head;
            while (i != index) {
                curr = curr.getNext();
                i++;
            }
            curr.getPrevious().setNext(curr.getNext());
            curr.getNext().setPrevious(curr.getPrevious());
            size--;
            return curr.getData();
        }
    }

    /**
     * Removes and returns the first element of the list.
     *
     * Must be O(1).
     *
     * @return the data formerly located at the front of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T removeFromFront() {
        if (head == null) {
            throw new NoSuchElementException("List is empty.");
        } else {
            DoublyLinkedListNode<T> removed = head;
            if (head == tail) {
                head = null;
                tail = null;
                size--;
                return removed.getData();
            } else {
                head = head.getNext();
                head.setPrevious(null);
            }
            size--;
            return removed.getData();
        }
    }

    /**
     * Removes and returns the last element of the list.
     *
     * Must be O(1).
     *
     * @return the data formerly located at the back of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T removeFromBack() {
        if (head == null) {
            throw new NoSuchElementException("List is empty.");
        } else {
            DoublyLinkedListNode<T> removed = tail;
            if (head == tail) {
                head = null;
                tail = null;
                size--;
                return removed.getData();
            } else {
                tail = tail.getPrevious();
                tail.setNext(null);
            }
            size--;
            return removed.getData();
        }
    }

    /**
     * Returns the element at the specified index.
     *
     * Must be O(1) for indices 0 and size - 1 and O(n) for all other cases.
     *
     * @param index the index of the element to get
     * @return the data stored at the index in the list
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                "Index is not within length of list.");
        } else if (index == 0) {
            return head.getData();
        } else if (index == size - 1) {
            return tail.getData();
        } else {
            int i = 0;
            DoublyLinkedListNode<T> node = head;
            while (i != index) {
                node = node.getNext();
                i++;
            }
            return node.getData();
        }
    }

    /**
     * Returns whether or not the list is empty.
     *
     * Must be O(1).
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return (head == null);
    }

    /**
     * Clears the list.
     *
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Removes and returns the last copy of the given data from the list.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the list.
     *
     * Must be O(1) if data is in the tail and O(n) for all other cases.
     *
     * @param data the data to be removed from the list
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if data is not found
     */
    public T removeLastOccurrence(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot remove null data.");
        }
        if (tail != null && tail.getData().equals(data)) {
            return removeFromBack();
        }
        DoublyLinkedListNode<T> curr = tail;
        while (curr != null && !(curr.getData().equals(data))) {
            curr = curr.getPrevious();
        }
        if (curr == null) {
            throw new NoSuchElementException("Data not in list.");
        }
        T foundData = curr.getData();
        DoublyLinkedListNode<T> prev = curr.getPrevious();
        DoublyLinkedListNode<T> next = curr.getNext();
        if (curr == head) {
            next.setPrevious(prev);
            head = next;
        } else {
            prev.setNext(next);
            next.setPrevious(prev);
        }
        size--;
        return foundData;
    }

    /**
     * Returns an array representation of the linked list.
     *
     * Must be O(n) for all cases.
     *
     * @return an array of length size holding all of the objects in the
     * list in the same order
     */
    public Object[] toArray() {
        int i = 0;
        DoublyLinkedListNode<T> curr = head;
        Object[] arr = new Object[size];
        while (i != size) {
            arr[i] = curr.getData();
            curr = curr.getNext();
            i++;
        }
        return arr;
    }

    /**
     * Returns the head node of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the node at the head of the list
     */
    public DoublyLinkedListNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }

    /**
     * Returns the tail node of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the node at the tail of the list
     */
    public DoublyLinkedListNode<T> getTail() {
        // DO NOT MODIFY!
        return tail;
    }

    /**
     * Returns the size of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the list
     */
    public int size() {
        // DO NOT MODIFY!
        return size;
    }
}
