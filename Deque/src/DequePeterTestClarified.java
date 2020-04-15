import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * This is a not-so-basic set of unit tests for ArrayDeque and LinkedDeque.
 * <p>
 * Passing these tests doesn't guarantee any grade on these assignments. These
 * student JUnits that we provide should be thought of as a sanity check to
 * help you get started on the homework and writing JUnits in general.
 * <p>
 * We highly encourage you to write your own set of JUnits for each homework
 * to cover edge cases you can think of for each data structure. Your code must
 * work correctly and efficiently in all cases, which is why it's important
 * to write comprehensive tests to cover as many cases as possible.
 * @author CS 1332 TAs + Peter Wilson
 * @version 1.0
 */
public class DequePeterTestClarified {

    private static final int TIMEOUT = 200;
    private static final int BASE_CAPACITY = ArrayDeque.INITIAL_CAPACITY;
    private ArrayDeque<String> array;
    private LinkedDeque<String> linked;

    @Before
    public void setup() {
        array = new ArrayDeque<>();
        linked = new LinkedDeque<>();
    }

    @Test(timeout = TIMEOUT)
    public void testInitialization() {
        assertEquals(0, array.size());
        assertArrayEquals(new Object[BASE_CAPACITY],
                array.getBackingArray());
        assertEquals(0, linked.size());
        assertNull(linked.getHead());
        assertNull(linked.getTail());
    }

    @Test(timeout = TIMEOUT)
    public void testArrayDequeNoWrapAround() {
        array.addLast("0a"); // 0a, _, _, _, _, _, _, _, _, _, _
        array.addLast("1a"); // 0a, 1a, _, _, _, _, _, _, _, _, _
        array.addLast("2a"); // 0a, 1a, 2a,  _, _, _, _, _, _, _, _
        array.addLast("3a"); // 0a, 1a, 2a, 3a, _, _, _, _, _, _, _
        array.addLast("4a"); // 0a, 1a, 2a, 3a, 4a, _, _, _, _, _, _

        assertEquals(5, array.size());
        String[] expected = new String[BASE_CAPACITY];
        expected[0] = "0a";
        expected[1] = "1a";
        expected[2] = "2a";
        expected[3] = "3a";
        expected[4] = "4a";
        assertArrayEquals(expected, array.getBackingArray());
        assertEquals("0a", array.getFirst());
        assertEquals("4a", array.getLast());

        // _, 1a, 2a, 3a, 4a, _, _, _, _, _, _
        assertEquals("0a", array.removeFirst());
        // _, _, 2a, 3a, 4a, _, _, _, _, _, _
        assertEquals("1a", array.removeFirst());
        // _, _, 2a, 3a, _, _, _, _, _, _, _
        assertEquals("4a", array.removeLast());
        // _, _, 2a, _, _, _, _, _, _, _, _
        assertEquals("3a", array.removeLast());

        assertEquals(1, array.size());
        expected[0] = null;
        expected[1] = null;
        expected[3] = null;
        expected[4] = null;
        assertArrayEquals(expected, array.getBackingArray());
        assertEquals("2a", array.getFirst());
        assertEquals("2a", array.getLast());

        //Test adding to from with
        array.addFirst("1a"); // _, 1a, 2a, _, _, _, _, _, _, _, _
        assertEquals("1a", array.getFirst());
        expected[1] = "1a";
        assertArrayEquals(expected, array.getBackingArray());

        array.removeFirst();
        array.removeFirst();  //Should be empty now
        expected = new String[BASE_CAPACITY];
        assertArrayEquals(expected, array.getBackingArray());
    }

    @Test(timeout = TIMEOUT)
    public void testArrayDequeRemoveToEmpty() {
        //Checks that removeFirst and removeLast can handle removing down to 0
        array.addLast("0a"); // 0a, _, _, _, _, _, _, _, _, _, _
        array.addLast("1a"); // 0a, 1a, _, _, _, _, _, _, _, _, _

        // _, 1a, _, _, _, _, _, _, _, _, _
        assertEquals("0a", array.removeFirst());

        // _, _, _, _, _, _, _, _, _, _, _
        assertEquals("1a", array.removeFirst());

        array.addLast("2a"); // _, _, 2a, _, _, _, _, _, _, _, _
        String[] expected = new String[BASE_CAPACITY];
        expected[2] = "2a";
        assertArrayEquals(expected, array.getBackingArray());

        // _, _, _, _, _, _, _, _, _, _, _
        assertEquals("2a", array.removeLast());
        array.addFirst("1a"); // _, 1a, _, _, _, _, _, _, _, _, _
        expected[2] = null;
        expected[1] = "1a";
        assertArrayEquals(expected, array.getBackingArray());

        assertEquals(1, array.size());
    }

    @Test(timeout = TIMEOUT)
    public void testArrayDequeBackExpansion() {
        String[] expected = new String[BASE_CAPACITY];
        for (int i = 0; i < BASE_CAPACITY; i++) {
            String newData = i + "a";
            array.addLast(newData);
            expected[i] = newData;
        }
        assertArrayEquals(expected, array.getBackingArray());

        //Move front to 2a
        assertEquals("0a", array.removeFirst());
        array.addLast(BASE_CAPACITY + "a");
        assertEquals("1a", array.removeFirst());
        array.addLast(BASE_CAPACITY + 1 + "a");

        //Expand Array. This should 'unroll' the array
        array.addLast(BASE_CAPACITY + 2 + "a");

        expected = new String[2 * BASE_CAPACITY];
        for (int i = 0; i <= BASE_CAPACITY; i++) {
            expected[i] = i + 2 + "a";
        }
        assertArrayEquals(expected, array.getBackingArray());

        //Test array does not shrink
        for (int i = 0; i <= BASE_CAPACITY; i++) {
            array.removeFirst();
        }

        Object[] backingArray = array.getBackingArray();
        assertEquals(2 * BASE_CAPACITY, backingArray.length);
    }

    @Test(timeout = TIMEOUT)
    public void testArrayDequeFrontExpansion() {
        String[] expected = new String[BASE_CAPACITY];
        for (int i = 0; i < BASE_CAPACITY; i++) {
            String newData = i + "a";
            array.addLast(newData);
            expected[i] = newData;
        }
        // List: 0a, 1a, ... 10a
        //Front: ^
        assertArrayEquals(expected, array.getBackingArray());

        //Move front to 2a
        assertEquals("0a", array.removeFirst());
        array.addLast(BASE_CAPACITY + "a");
        // List: 11a, 1a, ... 10a
        //Front:      ^

        assertEquals("1a", array.removeFirst());
        array.addLast(BASE_CAPACITY + 1 + "a");
        // List: 11a, 12a, 2a, ... 10a
        //Front:           ^

        //Expand Array. This should 'unroll' the array
        array.addFirst("1a");
        // List: 1a, 2a, ... 10a, 11a, 12a, _, ...
        //Front: ^

        expected = new String[2 * BASE_CAPACITY];
        for (int i = 0; i <= BASE_CAPACITY; i++) {
            expected[i] = i + 1 + "a";
        }
        assertArrayEquals(expected, array.getBackingArray());
    }

    //Test exceptions
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testArrayDequeAddFirstNullException() {
        array.addFirst(null);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testArrayDequeAddLastNullException() {
        array.addLast(null);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testArrayDequeRemoveFirstEmptyException() {
        array.removeFirst();
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testArrayDequeRemoveLastEmptyException() {
        array.removeLast();
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testArrayDequeGetFirstEmptyException() {
        array.getFirst();
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testArrayDequeGetLastEmptyException() {
        array.getLast();
    }

    @Test(timeout = TIMEOUT)
    public void testArrayDequeWrapAround() {
        array.addFirst("4a"); // _, _, _, _, _, _, _, _, _, _, 4a
        array.addFirst("3a"); // _, _, _, _, _, _, _, _, _, 3a, 4a
        array.addFirst("2a"); // _, _, _, _, _, _, _, _, 2a, 3a, 4a
        array.addFirst("1a"); // _, _, _, _, _, _, _, 1a, 2a, 3a, 4a
        array.addFirst("0a"); // _, _, _, _, _, _, 0a, 1a, 2a, 3a, 4a

        assertEquals(5, array.size());
        String[] expected = new String[BASE_CAPACITY];
        expected[10] = "4a";
        expected[9] = "3a";
        expected[8] = "2a";
        expected[7] = "1a";
        expected[6] = "0a";
        assertArrayEquals(expected, array.getBackingArray());
        assertEquals("0a", array.getFirst());
        assertEquals("4a", array.getLast());

        // _, _, _, _, _, _, 0a, 1a, 2a, 3a, _
        assertEquals("4a", array.removeLast());

        assertEquals(4, array.size());
        expected[10] = null;
        assertArrayEquals(expected, array.getBackingArray());
        assertEquals("0a", array.getFirst());
        assertEquals("3a", array.getLast());

        array.addLast("5a"); // _, _, _, _, _, _, 0a, 1a, 2a, 3a, 5a
        array.addLast("6a"); // 6a, _, _, _, _, _, 0a, 1a, 2a, 3a, 5a

        assertEquals(6, array.size());
        expected[10] = "5a";
        expected[0] = "6a";
        assertArrayEquals(expected, array.getBackingArray());
        assertEquals("0a", array.getFirst());
        assertEquals("6a", array.getLast());
    }

    @Test(timeout = TIMEOUT)
    public void testLinkedDequeAdd() {
        linked.addFirst("1a"); // 1a
        linked.addFirst("0a"); // 0a, 1a
        linked.addLast("2a"); // 0a, 1a, 2a
        linked.addLast("3a"); // 0a, 1a, 2a, 3a

        assertEquals(4, linked.size());

        LinkedNode<String> cur = linked.getHead();
        assertNotNull(cur);
        assertNull(cur.getPrevious());
        assertEquals("0a", cur.getData());

        LinkedNode<String> prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertEquals(prev, cur.getPrevious());
        assertEquals("1a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotEquals(null, cur);
        assertEquals(prev, cur.getPrevious());
        assertEquals("2a", cur.getData());

        prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertEquals(prev, cur.getPrevious());
        assertEquals("3a", cur.getData());
        assertEquals(linked.getTail(), cur);
        assertNull(cur.getNext());
    }

    @Test(timeout = TIMEOUT)
    public void testLinkedDequeRemove() {
        linked.addFirst("1a"); // 1a
        linked.addFirst("0a"); // 0a, 1a
        linked.addLast("2a"); // 0a, 1a, 2a
        linked.addLast("3a"); // 0a, 1a, 2a, 3a

        assertEquals(4, linked.size());

        assertEquals("0a", linked.removeFirst()); // 1a, 2a, 3a
        assertEquals("3a", linked.removeLast()); // 1a, 2a

        assertEquals(2, linked.size());

        LinkedNode<String> cur = linked.getHead();
        assertNotNull(cur);
        assertNull(cur.getPrevious());
        assertEquals("1a", cur.getData());

        LinkedNode<String> prev = cur;
        cur = cur.getNext();
        assertNotNull(cur);
        assertEquals(prev, cur.getPrevious());
        assertEquals("2a", cur.getData());
        assertEquals(linked.getTail(), cur);

        cur = cur.getNext();
        assertNull(cur);
    }

    @Test(timeout = TIMEOUT)
    public void testLinkedDequeRemoveFrontToEmpty() {
        linked.addLast("1a");
        linked.addFirst("0a");


        assertEquals("0a", linked.removeFirst());
        //Length 1, so head and tail should be same
        assertSame(linked.getTail(), linked.getHead());
        assertEquals("1a", linked.removeFirst());

        assertNull(linked.getHead());
        assertNull(linked.getTail());
        assertEquals(0, linked.size());
    }

    @Test(timeout = TIMEOUT)
    public void testLinkedDequeRemoveBackToEmpty() {
        linked.addFirst("0a");
        linked.addLast("1a");

        assertEquals("1a", linked.removeLast());
        //Length 1, so head and tail should be same
        assertSame(linked.getTail(), linked.getHead());
        assertEquals("0a", linked.removeLast());

        assertNull(linked.getHead());
        assertNull(linked.getTail());
        assertEquals(0, linked.size());
    }

    @Test(timeout = TIMEOUT)
    public void testLinkedDequeGet() {
        linked.addLast("0a"); // 0a
        linked.addLast("1a"); // 0a, 1a
        linked.addLast("2a"); // 0a, 1a, 2a
        linked.addLast("3a"); // 0a, 1a, 2a, 3a

        assertEquals("0a", linked.getFirst());
        assertEquals("3a", linked.getLast());
    }

    //Test exceptions
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testLinkedDequeAddFirstNullException() {
        linked.addFirst(null);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void testLinkedDequeAddLastNullException() {
        linked.addLast(null);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testLinkedDequeRemoveFirstEmptyException() {
        linked.removeFirst();
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testLinkedDequeRemoveLastEmptyException() {
        linked.removeLast();
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testLinkedDequeGetFirstEmptyException() {
        linked.getFirst();
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void testLinkedDequeGetLastEmptyException() {
        linked.getLast();
    }
}
