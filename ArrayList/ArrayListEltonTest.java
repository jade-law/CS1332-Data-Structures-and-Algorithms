import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static junit.framework.TestCase.assertEquals;

public class ArrayListEltonTest {
    private class Node<E> {
        E element;
        public Node(E e) { element = e; }
        public E getElement() { return element; }
    }

    private static final int TIMEOUT = 200;
    private ArrayList<Node> list = new ArrayList<>();

    @Before
    public void setUp() {
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testAddAtIndexNullElement() {
        list.addAtIndex(0, null);
    }

    @Test(timeout = TIMEOUT, expected = IndexOutOfBoundsException.class)
    public void testAddAtIndexBadIndex() {
        list.addAtIndex(0, new Node<>("Monad"));
        list.addAtIndex(0, new Node<>("Functor"));
        list.addAtIndex(3, null);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testAddToFrontNullElement() {
        list.addToFront(new Node<>("Monad"));
        list.addToFront(new Node<>("Functor"));
        list.addToFront(null);
    }

    @Test(timeout = TIMEOUT, expected =  IllegalArgumentException.class)
    public void testAddToBackNullElement() {
        list.addToBack(new Node<>("Monad"));
        list.addToBack(new Node<>("Functor"));
        list.addToBack(null);
    }

    @Test(timeout = TIMEOUT, expected = IndexOutOfBoundsException.class)
    public void testRemoveAtIndexBadIndex() {
        list.addAtIndex(0, new Node<>("Monad"));
        list.addAtIndex(0, new Node<>("Functor"));
        list.removeAtIndex(5);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testRemoveFromFrontEmptyList() {
        list.removeFromFront();
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testRemoveFromBackEmptyList() {
        list.removeFromBack();
    }

    @Test(timeout = TIMEOUT, expected = IndexOutOfBoundsException.class)
    public void testGetBadIndexGreaterThanSize() {
        list.addToFront(new Node<>("Monad"));
        list.addToFront(new Node<>("Functor"));
        list.get(5);
    }

    @Test(timeout = TIMEOUT, expected = IndexOutOfBoundsException.class)
    public void testGetBadIndexLessThanZero() {
        list.addToFront(new Node<>("Monad"));
        list.addToFront(new Node<>("Functor"));
        list.get(-2);
    }
}