import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * This is a basic set of unit tests for DoublyLinkedList.
 *
 * Passing these tests doesn't guarantee any grade on these assignments. These
 * student JUnits that we provide should be thought of as a sanity check to
 * help you get started on the homework and writing JUnits in general.
 *
 * We highly encourage you to write your own set of JUnits for each homework
 * to cover edge cases you can think of for each data structure. Your code must
 * work correctly and efficiently in all cases, which is why it's important
 * to write comprehensive tests to cover as many cases as possible.
 *
 * @author CS 1332 TAs and Sameer Suri
 * @version 1.0
 */
public class DoublyLinkedListStudentTestSameer {

    private static final int TIMEOUT = 200;
    private DoublyLinkedList<String> list;

    @Before
    public void setUp() {
        list = new DoublyLinkedList<>();
    }

    @Test(timeout = TIMEOUT)
    public void testInitialization() {
        assertEquals(0, list.size());
        assertNull(list.getHead());
        assertNull(list.getTail());
    }

    @Test(timeout = TIMEOUT)
    public void testAddAtIndex() {
        list.addAtIndex(0, "2a"); // 2a
        list.addAtIndex(0, "1a"); // 1a, 2a
        list.addAtIndex(2, "4a"); // 1a, 2a, 4a
        list.addAtIndex(2, "3a"); // 1a, 2a, 3a, 4a
        list.addAtIndex(0, "0a"); // 0a, 1a, 2a, 3a, 4a

        assertEquals(5, list.size());

        DoublyLinkedListNode<String> cur = list.getHead();
        assertNotNull(cur);
        assertNull(cur.getPrevious());
        assertEquals("0a", cur.getData());

        DoublyLinkedListNode<String> prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("1a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("2a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("3a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("4a", cur.getData());
        assertSame(list.getTail(), cur);

        cur = cur.getNext();
        assertNull(cur);
    }

    @Test(timeout = TIMEOUT)
    public void testAddToFront() {
        list.addToFront("4a"); // 4a
        list.addToFront("3a"); // 3a, 4a
        list.addToFront("2a"); // 2a, 3a, 4a
        list.addToFront("1a"); // 1a, 2a, 3a, 4a
        list.addToFront("0a"); // 0a, 1a, 2a, 3a, 4a

        assertEquals(5, list.size());

        DoublyLinkedListNode<String> cur = list.getHead();
        assertNotNull(cur);
        assertNull(cur.getPrevious());
        assertEquals("0a", cur.getData());

        DoublyLinkedListNode<String> prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("1a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("2a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("3a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("4a", cur.getData());
        assertSame(list.getTail(), cur);

        cur = cur.getNext();
        assertNull(cur);
    }

    @Test(timeout = TIMEOUT)
    public void testAddToBack() {
        list.addToBack("0a"); // 0a
        list.addToBack("1a"); // 0a, 1a
        list.addToBack("2a"); // 0a, 1a, 2a
        list.addToBack("3a"); // 0a, 1a, 2a, 3a
        list.addToBack("4a"); // 0a, 1a, 2a, 3a, 4a

        assertEquals(5, list.size());

        DoublyLinkedListNode<String> cur = list.getHead();
        assertNotNull(cur);
        assertNull(cur.getPrevious());
        assertEquals("0a", cur.getData());

        DoublyLinkedListNode<String> prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("1a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("2a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("3a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("4a", cur.getData());
        assertSame(list.getTail(), cur);

        cur = cur.getNext();
        assertNull(cur);
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveAtIndex() {
        String temp = "2a";

        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, temp); // 0a, 1a, 2a
        list.addAtIndex(3, "3a"); // 0a, 1a, 2a, 3a
        list.addAtIndex(4, "4a"); // 0a, 1a, 2a, 3a, 4a
        list.addAtIndex(5, "5a"); // 0a, 1a, 2a, 3a, 4a, 5a
        assertEquals(6, list.size());

        assertSame(temp, list.removeAtIndex(2)); // 0a, 1a, 3a, 4a, 5a
        assertEquals(5, list.size());

        DoublyLinkedListNode<String> cur = list.getHead();
        assertNotNull(cur);
        assertNull(cur.getPrevious());
        assertEquals("0a", cur.getData());

        DoublyLinkedListNode<String> prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("1a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("3a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("4a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("5a", cur.getData());
        assertSame(list.getTail(), cur);

        cur = cur.getNext();
        assertNull(cur);
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveFromFront() {
        String temp = "0a";

        list.addAtIndex(0, temp); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        list.addAtIndex(3, "3a"); // 0a, 1a, 2a, 3a
        list.addAtIndex(4, "4a"); // 0a, 1a, 2a, 3a, 4a
        list.addAtIndex(5, "5a"); // 0a, 1a, 2a, 3a, 4a, 5a
        assertEquals(6, list.size());

        assertSame(temp, list.removeFromFront()); // 1a, 2a, 3a, 4a, 5a
        assertEquals(5, list.size());

        DoublyLinkedListNode<String> cur = list.getHead();
        assertNotNull(cur);
        assertNull(cur.getPrevious());
        assertEquals("1a", cur.getData());

        DoublyLinkedListNode<String> prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("2a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("3a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("4a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("5a", cur.getData());
        assertSame(list.getTail(), cur);

        cur = cur.getNext();
        assertNull(cur);
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveFromBack() {
        String temp = "5a";

        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        list.addAtIndex(3, "3a"); // 0a, 1a, 2a, 3a
        list.addAtIndex(4, "4a"); // 0a, 1a, 2a, 3a, 4a
        list.addAtIndex(5, temp); // 0a, 1a, 2a, 3a, 4a, 5a
        assertEquals(6, list.size());

        assertEquals(temp, list.removeFromBack()); // 0a, 1a, 2a, 3a, 4a
        assertEquals(5, list.size());

        DoublyLinkedListNode<String> cur = list.getHead();
        assertNotNull(cur);
        assertNull(cur.getPrevious());
        assertEquals("0a", cur.getData());

        DoublyLinkedListNode<String> prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("1a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("2a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("3a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("4a", cur.getData());
        assertSame(list.getTail(), cur);

        cur = cur.getNext();
        assertNull(cur);
    }

    @Test(timeout = TIMEOUT)
    public void testGet() {
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        list.addAtIndex(3, "3a"); // 0a, 1a, 2a, 3a
        list.addAtIndex(4, "4a"); // 0a, 1a, 2a, 3a, 4a

        assertEquals("0a", list.get(0));
        assertEquals("1a", list.get(1));
        assertEquals("2a", list.get(2));
        assertEquals("3a", list.get(3));
        assertEquals("4a", list.get(4));
    }

    @Test(timeout = TIMEOUT)
    public void testIsEmptyAndClear() {
        // Should be empty at initialization
        assertTrue(list.isEmpty());

        // Should not be empty after adding elements
        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        list.addAtIndex(3, "3a"); // 0a, 1a, 2a, 3a
        list.addAtIndex(4, "4a"); // 0a, 1a, 2a, 3a, 4a
        assertFalse(list.isEmpty());

        // Clearing the list should empty the array and reset size
        list.clear();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        assertNull(list.getHead());
        assertNull(list.getTail());
    }

    @Test(timeout = TIMEOUT)
    public void testLastOccurrence() {
        String temp = "2a";

        list.addAtIndex(0, "0a"); // 0a
        list.addAtIndex(1, "1a"); // 0a, 1a
        list.addAtIndex(2, "2a"); // 0a, 1a, 2a
        list.addAtIndex(3, temp); // 0a, 1a, 2a, 2a
        list.addAtIndex(4, "3a"); // 0a, 1a, 2a, 2a, 3a
        list.addAtIndex(5, "4a"); // 0a, 1a, 2a, 2a, 3a, 4a
        assertEquals(6, list.size());

        assertSame(temp, list.removeLastOccurrence("2a")); // 0a, 1a, 2a, 3a, 4a
        assertEquals(5, list.size());

        DoublyLinkedListNode<String> cur = list.getHead();
        assertNotNull(cur);
        assertNull(cur.getPrevious());
        assertEquals("0a", cur.getData());

        DoublyLinkedListNode<String> prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("1a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("2a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("3a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertSame(prev, cur.getPrevious());
        assertEquals("4a", cur.getData());
        assertSame(list.getTail(), cur);

        cur = cur.getNext();
        assertNull(cur);
    }

    @Test(timeout = TIMEOUT)
    public void testToArray() {
        String[] expected = new String[5];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = i + "a";
            list.addAtIndex(i, expected[i]);
        }

        assertArrayEquals(expected, list.toArray());
    }

    // Custom UT:
    // addAtIndex
    @Test(timeout = TIMEOUT)
    public void testAddAtIndexInitialElement() {
        assertNull(list.getHead());
        assertNull(list.getTail());

        list.addAtIndex(0, "Test");

        assertSame(list.getHead(), list.getTail());
        DoublyLinkedListNode<String> node = list.getHead();
        assertEquals("Test", node.getData());
        assertNull(node.getPrevious());
        assertNull(node.getNext());
    }

    /**
     * @param size size
     */
    private void checkConsistent(int size) {
        assertEquals(size, list.size());
        DoublyLinkedListNode<String> node = list.getHead();
        for (int i = 1; i <= size; i++) {
            assertEquals("Test" + i, node.getData());
            if (i != 1) {
                assertSame(node, node.getPrevious().getNext());
            }
            if (i != size) {
                assertSame(node, node.getNext().getPrevious());
            }
            node = node.getNext();
        }
        assertNull(list.getHead().getPrevious());
        assertNull(list.getTail().getNext());
    }
    @Test(timeout = TIMEOUT)
    public void testAddAtIndexToHead() {
        list.addAtIndex(0, "Test5");
        list.addAtIndex(0, "Test4");
        list.addAtIndex(0, "Test3");
        list.addAtIndex(0, "Test2");
        list.addAtIndex(0, "Test1");

        checkConsistent(5);
    }

    @Test(timeout = TIMEOUT)
    public void testAddAtIndexToTail() {
        list.addAtIndex(list.size(), "Test1");
        list.addAtIndex(list.size(), "Test2");
        list.addAtIndex(list.size(), "Test3");
        list.addAtIndex(list.size(), "Test4");
        list.addAtIndex(list.size(), "Test5");

        checkConsistent(5);
    }

    @Test(timeout = TIMEOUT)
    public void testAddAtIndexStandard() {
        list.addToBack("Test1");
        list.addToBack("Test3");
        list.addToBack("Test5");
        list.addToBack("Test7");
        list.addToBack("Test9");

        list.addAtIndex(1, "Test2");
        list.addAtIndex(3, "Test4");
        list.addAtIndex(5, "Test6");
        list.addAtIndex(7, "Test8");

        checkConsistent(9);
    }

    @Test(timeout = TIMEOUT, expected = IndexOutOfBoundsException.class)
    public void testAddAtIndexNegativeIndex() {
        list.addAtIndex(-1, "Test");
    }

    @Test(timeout = TIMEOUT, expected = IndexOutOfBoundsException.class)
    public void testAddAtIndexTooHighIndex() {
        list.addToBack("Test1");
        list.addToBack("Test2");
        list.addAtIndex(42, "Test3");
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testAddAtIndexNullElement() {
        list.addAtIndex(0, null);
    }

    // removeAtIndex
    @Test(timeout = TIMEOUT)
    public void testRemoveAtIndexOnlyElement() {
        list.addToBack("Test");
        list.removeAtIndex(0);
        assertNull(list.getHead());
        assertNull(list.getTail());
        assertEquals(0, list.size());
    }

    // removeAtIndex
    @Test(timeout = TIMEOUT)
    public void testRemoveAtIndexFromHead() {
        list.addToBack("Test");
        list.addToBack("Test1");
        list.addToBack("Test2");
        list.addToBack("Test3");

        list.removeAtIndex(0);
        checkConsistent(3);
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveAtIndexFromTail() {
        list.addToBack("Test1");
        list.addToBack("Test2");
        list.addToBack("Test3");
        list.addToBack("Test");

        list.removeAtIndex(3);
        checkConsistent(3);
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveAtIndexStandard() {
        list.addToBack("Test1");
        list.addToBack("TestA");
        list.addToBack("Test2");
        list.addToBack("TestB");
        list.addToBack("Test3");
        list.addToBack("TestC");
        list.addToBack("Test4");
        list.addToBack("TestD");
        list.addToBack("Test5");
        list.addToBack("TestE");

        list.removeAtIndex(1);
        list.removeAtIndex(2);
        list.removeAtIndex(3);
        list.removeAtIndex(4);
        list.removeAtIndex(5);

        checkConsistent(5);
    }

    @Test(timeout = TIMEOUT, expected = IndexOutOfBoundsException.class)
    public void testRemoveAtIndexNegativeIndex() {
        list.removeAtIndex(-1);
    }

    @Test(timeout = TIMEOUT, expected = IndexOutOfBoundsException.class)
    public void testRemoveAtIndexTooHighIndex() {
        list.addToBack("Test1");
        list.addToBack("Test2");
        list.removeAtIndex(42);
    }

    // removeLastOccurrence
    @Test(timeout = TIMEOUT)
    public void testRemoveLastOccurrenceOneElement() {
        list.addToBack("Test1");
        list.addToBack("Test2");
        list.addToBack("Test3");
        list.addToBack("Test5");
        list.addToBack("Test4");
        list.removeLastOccurrence("Test5");
        checkConsistent(4);
    }

    @Test(timeout = TIMEOUT)
    public void testRemoveLastOccurrenceManyElements() {
        list.addToBack("Test1");
        list.addToBack("Test2");
        list.addToBack("Test3");
        list.addToBack("Test4");
        list.addToBack("Test2");
        list.removeLastOccurrence("Test2");
        checkConsistent(4);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testRemoveLastOccurrenceNullElement() {
        list.removeLastOccurrence(null);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testRemoveLastOccurrenceNonexistentElement() {
        list.addToBack("Test1");
        list.addToBack("Test2");
        list.addToBack("Test3");
        list.addToBack("Test4");
        list.removeLastOccurrence("Test5");
    }


}